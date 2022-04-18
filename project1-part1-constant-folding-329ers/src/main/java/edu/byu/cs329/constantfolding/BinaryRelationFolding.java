package edu.byu.cs329.constantfolding;

import edu.byu.cs329.utils.ExceptionUtils;
import edu.byu.cs329.utils.TreeModificationUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replaces binary relational InfixExpressions (<) where both are NumberLiterals
 * with a BooleanLiteral.
 *
 * @author Joshua Higgins and Brigham Andersen
 */
public class BinaryRelationFolding implements Folding {
  static final Logger log = LoggerFactory.getLogger(ParenthesizedExpressionFolding.class);

  public BinaryRelationFolding() {
  }

  /**
   * Replaces binary relational InfixExpressions (<) where both are NumberLiterals
   * with a BooleanLiteral.
   *
   * <p>Visits the root and any reachable nodes from the root to replace
   * any InfixExpression reachable node containing NumberLiterals and the less
   * than
   * operator with a folded BooleanLiteral.
   *
   * <p>top := all nodes reachable from root such that each node
   * is an outermost infix expression that ends
   * in a Numberliteral
   *
   * <p>parents := all nodes such that each one is the parent
   * of some node in top
   *
   * <p>isFoldable(n) := isInfixExpression(n)
   * /\ ( hasLessThanOperator(operator(n))
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
   *         /\ n' leafNode == BooleanLiteral (if n' leafNode was ever
   *         NumberLiteral, it will be changed to BooleanLiteral)
   *         /\ if n leftLeafNode > n rightLeafNode ==> n' leafNode
   *         BooleanLiteral == true
   *         /\ if n leftLeafNode < n rightLeafNode ==> n' leafNode
   *         BooleanLiteral == false
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

    private boolean isLessThanOperator(InfixExpression exp) {
      return exp.getOperator().equals(InfixExpression.Operator.LESS);
    }

    private boolean checkForNumLits(InfixExpression exp) {
      return (exp.getLeftOperand() instanceof NumberLiteral)
              && (exp.getRightOperand() instanceof NumberLiteral);
    }

    @Override
    public void endVisit(InfixExpression node) {
      if (!isLessThanOperator(node)) {
        return;
      }
      if (!checkForNumLits(node)) {
        return;
      }

      AST ast = node.getAST();
      int leftOperand = Integer.parseInt(node.getLeftOperand().toString());
      int rightOperand = Integer.parseInt(node.getRightOperand().toString());

      boolean boolVal = leftOperand < rightOperand;
      BooleanLiteral newLit = ast.newBooleanLiteral(boolVal);
      TreeModificationUtils.replaceChildInParent(node, newLit);
      didFold = true;
    }
  }
}
