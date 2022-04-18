package edu.byu.cs329.constantfolding;

import edu.byu.cs329.utils.ExceptionUtils;
import edu.byu.cs329.utils.TreeModificationUtils;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Folds infix expression with + operator into a single value.
 *
 * @author Joshua Higgins and Brigham Andersen
 */
public class NumericPlusInfixFolding implements Folding {
  static final Logger log = LoggerFactory.getLogger(ParenthesizedExpressionFolding.class);

  public NumericPlusInfixFolding() {
  }

  /**
   * Folds infix expression with + operator into a single value
   *
   * <p>Visits the root and any reachable nodes from the root to replace
   * any InfixExpression reachable node containing NumberLiterals and the plus
   * operator with a folded sum NumberLiteral.
   *
   * <p>top := all nodes reachable from root such that each node
   * is an outermost infix expression that ends
   * in a Numberliteral
   *
   * <p>parents := all nodes such that each one is the parent
   * of some node in top
   *
   * <p>isFoldable(n) := isInfixExpression(n)
   * /\ ( hasPlusOperator(operator(n))
   * /\ has 2+ number literals
   * || isFoldable(expression(n)))
   *
   * <p>literal(n) := if isLiteral(n) then n else literal(expression(n))
   *
   * @param root the root of the tree to traverse.
   * @return true if parenthesized literals were replaced in the rooted tree
   * @modifies nodes in parents
   * @requires root != null
   * @requires (root instanceof CompilationUnit) \/ parent(root) != null
   * @ensures fold(root) == (old(top) != emptyset)
   * @ensures forall n in old(top), exists n' in nodes
   *         fresh(n')
   *         /\ isNumberLiteral(expression(n'))
   *         /\ parent(n') == parent(n)
   *         /\ n' extended operands is null (if there were extendedOperands,
   *         their number literal values will be added to sum for new
   *         NumberLiteral)
   *         /\ not exists extended operands ==> old(n1 Number Literal value) +
   *         old(n2 Number Literal value) = n' NumberLiteral value
   */
  public boolean fold(final ASTNode root) {
    checkRequires(root);
    Visitor visitor = new Visitor();
    root.accept(visitor);
    return visitor.didFold;
  }

  private void checkRequires(final ASTNode root) {
    ExceptionUtils.requiresNonNull(root, "Null root passed to ParenthesizedExpressionFolding.fold");

    if (!(root instanceof CompilationUnit) && root.getParent() == null) {
      ExceptionUtils.throwRuntimeException(
              "Non-CompilationUnit root with no parent "
                      + "passed to ParenthesizedExpressionFolding.fold");
    }
  }

  class Visitor extends ASTVisitor {
    public boolean didFold = false;

    private boolean isPlusOperator(InfixExpression exp) {
      return exp.getOperator().equals(InfixExpression.Operator.PLUS);
    }

    private boolean checkForNumLits(InfixExpression exp) {
      return (exp.getLeftOperand() instanceof NumberLiteral)
              && (exp.getRightOperand() instanceof NumberLiteral);
    }

    @Override
    public void endVisit(InfixExpression node) {
      if (!isPlusOperator(node)) {
        return;
      }
      if (!checkForNumLits(node)) {
        return;
      }

      int sum = 0;

      AST ast = node.getAST();
      // Add child Number Literals
      int leftOperand = Integer.parseInt(node.getLeftOperand().toString());
      int rightOperand = Integer.parseInt(node.getRightOperand().toString());

      sum = leftOperand + rightOperand;

      // Add extended Number Literals
      @SuppressWarnings("unchecked")
      List<Expression> extendedOperands = (List<Expression>) node.extendedOperands();
      if (extendedOperands.size() > 0) {
        for (Expression exp : extendedOperands) {
          sum += Integer.parseInt(exp.toString());
        }
      }
      // Replace with the resulting number literal
      NumberLiteral newLit = ast.newNumberLiteral(String.valueOf(sum));
      TreeModificationUtils.replaceChildInParent(node, newLit);
      didFold = true;
    }
  }
}
