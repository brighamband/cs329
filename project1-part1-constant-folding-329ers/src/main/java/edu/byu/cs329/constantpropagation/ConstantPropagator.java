package edu.byu.cs329.constantpropagation;

import edu.byu.cs329.rd.ReachingDefinitions;
import edu.byu.cs329.utils.ExceptionUtils;
import edu.byu.cs329.utils.TreeModificationUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class ConstantPropagator {
  List<ReachingDefinitions> rdList;

  public ConstantPropagator(List<ReachingDefinitions> rdList) {
    this.rdList = rdList;
  }

  /**
   * Sets up visitor at the given node.
   */
  public boolean replace(final ASTNode root) {
    checkRequires(root);
    Visitor visitor = new Visitor();
    root.accept(visitor);
    return visitor.didReplace;
  }

  private void checkRequires(final ASTNode root) {
    ExceptionUtils.requiresNonNull(
            root, "Null root passed to ParenthesizedExpressionFolding.fold");

    if (!(root instanceof CompilationUnit) && root.getParent() == null) {
      ExceptionUtils.throwRuntimeException(
              "Non-CompilationUnit root with no parent "
                      + "passed to ParenthesizedExpressionFolding.fold");
    }
  }

  class Visitor extends ASTVisitor {
    public boolean didReplace = false;

    @Override
    public boolean visit(VariableDeclarationStatement node) {
      VariableDeclarationFragment nodeFrag = (VariableDeclarationFragment) node.fragments().get(0);
      Expression expression = nodeFrag.getInitializer();

      // Just skip if variable declared but not defined
      if (expression == null) {
        return true;
      }

      // If right side is literal, save it
      int rightNodeType = expression.getNodeType();
      if (rightNodeType == ASTNode.SIMPLE_NAME) {  // If right side is simple name, replace it
        List<Expression> definitionsForVar = new ArrayList<>();
        Set<ReachingDefinitions.Definition> entrySet = rdList.get(0).getReachingDefinitions(node);
        for (ReachingDefinitions.Definition current : entrySet) {
          if (current.name.getIdentifier()
                  .equals(((SimpleName) nodeFrag.getInitializer()).getIdentifier())) {
            Expression currentExpression = null;
            if (current.statement instanceof VariableDeclarationStatement) {  // VarDecStatement
              currentExpression = ((VariableDeclarationFragment)
                      ((VariableDeclarationStatement) current.statement)
                              .fragments().get(0)).getInitializer();
            }

            if (currentExpression != null && !definitionsForVar.contains(currentExpression)) {
              definitionsForVar.add(currentExpression);
            }
          }
        }

        if (definitionsForVar.size() == 1) {
          replaceWithLiteral(nodeFrag.getInitializer(), definitionsForVar.get(0));
          return false;
        }

        return true;
      }

      return true;
    }

    @Override
    public boolean visit(ExpressionStatement node) {
      Assignment assignment = (Assignment) node.getExpression();
      Expression expression = assignment.getRightHandSide();

      // If right side is literal, save it
      int rightNodeType = expression.getNodeType();
      if (rightNodeType == ASTNode.SIMPLE_NAME) { // If right side is simple name, replace it
        List<Expression> definitionsForVar = new ArrayList<>();
        Set<ReachingDefinitions.Definition> entrySet = rdList.get(0).getReachingDefinitions(node);
        for (ReachingDefinitions.Definition current : entrySet) {
          if (current.name.getIdentifier().equals(((SimpleName) expression).getIdentifier())) {
            Expression currentExpression;
            if (current.statement instanceof VariableDeclarationStatement) {  // VarDecStatement
              currentExpression = ((VariableDeclarationFragment)
                      ((VariableDeclarationStatement) current.statement)
                              .fragments().get(0)).getInitializer();
            } else {  // ExpressionStatement
              currentExpression = ((Assignment)
                      ((ExpressionStatement) current.statement).getExpression()).getRightHandSide();
            }

            if (currentExpression != null && !definitionsForVar.contains(currentExpression)) {
              definitionsForVar.add(currentExpression);
            }
          }
        }

        if (definitionsForVar.size() == 1) {
          replaceWithLiteral(expression, definitionsForVar.get(0));
          return false;
        }
      }

      return true;
    }

    @Override
    public boolean visit(InfixExpression node) {
      Expression leftExp = node.getLeftOperand();
      Expression rightExp = node.getRightOperand();
      List<Expression> expressions = new ArrayList<>();
      expressions.add(leftExp);
      expressions.add(rightExp);
      expressions.addAll(node.extendedOperands());

      for (Expression exp : expressions) {
        // If type is literal, ignore it
        // If right side is simple name, replace it
        if (exp.getNodeType() == ASTNode.SIMPLE_NAME) {
          List<Expression> definitionsForVar = new ArrayList<>();
          Statement infixStatement;
          ASTNode findStatement = node.getParent();
          while (!(findStatement instanceof Statement)) {
            findStatement = findStatement.getParent();
          }
          infixStatement = (Statement) findStatement;
          Set<ReachingDefinitions.Definition> entrySet
                  = rdList.get(0).getReachingDefinitions(infixStatement);
          for (ReachingDefinitions.Definition current : entrySet) {
            if (current.name.getIdentifier().equals(((SimpleName) exp).getIdentifier())) {
              Expression currentExpression = null;
              if (current.statement instanceof VariableDeclarationStatement) {  // VarDecStatement
                currentExpression = ((VariableDeclarationFragment)
                        ((VariableDeclarationStatement) current.statement)
                                .fragments().get(0)).getInitializer();
              } else if (current.statement instanceof ExpressionStatement) {  // ExpressionStatement
                currentExpression = ((Assignment)
                        ((ExpressionStatement) current.statement)
                                .getExpression()).getRightHandSide();
              }

              if (currentExpression != null && !definitionsForVar.contains(currentExpression)) {
                definitionsForVar.add(currentExpression);
              }
            }
          }

          if (definitionsForVar.size() == 1) {
            if (definitionsForVar.get(0) instanceof InfixExpression) {
              return true;
            }
            replaceWithLiteral(exp, definitionsForVar.get(0));
            return false;
          }
        }
      }

      return true;
    }

    @Override
    public boolean visit(ReturnStatement node) {
      // Replace SimpleName with NumberLiteral
      if (node.getExpression() instanceof SimpleName) {
        List<Expression> definitionsForVar = new ArrayList<>();
        Set<ReachingDefinitions.Definition> entrySet = rdList.get(0).getReachingDefinitions(node);
        for (ReachingDefinitions.Definition current : entrySet) {
          if (current.name.getIdentifier().equals(
                  ((SimpleName) node.getExpression()).getIdentifier())) {
            Expression currentExpression;
            if (current.statement instanceof VariableDeclarationStatement) {  // VarDecStatement
              currentExpression = ((VariableDeclarationFragment)
                      ((VariableDeclarationStatement) current.statement)
                              .fragments().get(0)).getInitializer();
            } else if (current.statement instanceof ReturnStatement) {
              currentExpression = ((ReturnStatement) current.statement).getExpression();
            } else {  // ExpressionStatement
              currentExpression = ((ExpressionStatement) current.statement).getExpression();
            }

            if (!definitionsForVar.contains(currentExpression)) {
              definitionsForVar.add(currentExpression);
            }
          }
        }

        if (definitionsForVar.size() == 1) {
          replaceWithLiteral(node.getExpression(), definitionsForVar.get(0));
          return false;
        }
      }

      return true;
    }

    private void replaceWithLiteral(ASTNode oldNode, ASTNode nodeToMake) {
      // oldNode is SimpleName, nodeToMake is Expression
      // Get old AST
      AST ast = oldNode.getAST();

      if (nodeToMake.getNodeType() == ASTNode.NUMBER_LITERAL) { // NumberLiteral
        String newNumVal = ((NumberLiteral) nodeToMake).getToken();
        NumberLiteral newNode = ast.newNumberLiteral(newNumVal);
        TreeModificationUtils.replaceChildInParent(oldNode, newNode);
      } else if (nodeToMake.getNodeType() == ASTNode.BOOLEAN_LITERAL) {  // BooleanLiteral
        boolean newBoolVal = ((BooleanLiteral) nodeToMake).booleanValue();
        BooleanLiteral newNode = ast.newBooleanLiteral(newBoolVal);
        TreeModificationUtils.replaceChildInParent(oldNode, newNode);
      } else if (nodeToMake.getNodeType() == ASTNode.STRING_LITERAL) {  // StringLiteral
        String newStrVal = ((StringLiteral) nodeToMake).getEscapedValue();
        StringLiteral newNode = ast.newStringLiteral();
        newNode.setEscapedValue(newStrVal);
        TreeModificationUtils.replaceChildInParent(oldNode, newNode);
      } else {
        System.out.println("No matching type");
      }

      didReplace = true;
    }
  }
}