package edu.byu.cs329.constantfolding;

import edu.byu.cs329.utils.ExceptionUtils;
import edu.byu.cs329.utils.TreeModificationUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replaces logical not (!) PrefixExpressions containing a BooleanLiteral with a
 * BooleanLiteral.
 *
 * @author Joshua Higgins and Brigham Andersen
 */
public class PrefixNotBoolFolding implements Folding {
  static final Logger log = LoggerFactory.getLogger(PrefixNotBoolFolding.class);

  public PrefixNotBoolFolding() {
  }

  /**
   * Replaces the combination of logical not prefix expressions and boolean
   * literals in the tree with boolean literals.
   *
   * <p>Visits the root and any reachable nodes from the root to replace
   * any logical not PrefixExpression reachable node containing a BooleanLiteral
   * with the BooleanLiteral itself.
   *
   * <p>top := all nodes reachable from root such that each node
   * is an outermost prefix expression that ends
   * in a BooleanLiteral
   *
   * <p>parents := all nodes such that each one is the parent
   * of some node in top
   *
   * <p>isFoldable(n) := isPrefixExpression(n)
   * /\ isExclamationMark(operator(n))
   * /\ isBooleanLiteral(expression(operator(n)))
   * || isFoldable(expression(n)))
   *
   * <p>boolVal := leafNode Boolean value under n (expression node)
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
   *         /\ isBooleanLiteral(n')
   *         /\ odd number of nested prefixes ==> boolVal(n') == !boolVal(n)
   *         /\ even number of nested prefixes ==> boolVal(n') == boolVal(n)
   *         /\ children(parent(n')) == (children(parent(n)) setminus {n})
   *         union {n'}
   */
  public boolean fold(final ASTNode root) {
    checkRequires(root);
    Visitor visitor = new Visitor();
    root.accept(visitor);
    return visitor.didFold;
  }

  private void checkRequires(final ASTNode root) {
    ExceptionUtils.requiresNonNull(root, "Null root passed to PrefixExpressionFolding.fold");

    if (!(root instanceof CompilationUnit) && root.getParent() == null) {
      ExceptionUtils.throwRuntimeException(
              "Non-CompilationUnit root with no parent passed to PrefixExpressionFolding.fold");
    }
  }

  class Visitor extends ASTVisitor {
    public boolean didFold = false;

    private boolean isExclamationMark(PrefixExpression exp) {
      return exp.getOperator().equals(PrefixExpression.Operator.NOT);
    }

    @Override
    public void endVisit(PrefixExpression node) {
      if (!isExclamationMark(node)) {
        return;
      }
      ASTNode exp = node.getOperand();

      if (!(exp instanceof BooleanLiteral)) {
        return;
      }

      BooleanLiteral boolLiteral = (BooleanLiteral) exp;

      boolean boolVal = boolLiteral.booleanValue();
      AST ast = node.getAST();
      // Replace with a flipped BooleanLiteral
      BooleanLiteral newLit = ast.newBooleanLiteral(!boolVal);
      TreeModificationUtils.replaceChildInParent(node, newLit);

      didFold = true;
    }
  }
}
