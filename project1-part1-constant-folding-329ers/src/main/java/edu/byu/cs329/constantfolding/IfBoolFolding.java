package edu.byu.cs329.constantfolding;

import edu.byu.cs329.utils.ExceptionUtils;
import edu.byu.cs329.utils.TreeModificationUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replaces if statements that bool literals as conditions.
 *
 * @author Joshua Higgins and Brigham Andersen
 */
public class IfBoolFolding implements Folding {
  static final Logger log = LoggerFactory.getLogger(PrefixNotBoolFolding.class);

  public IfBoolFolding() {
  }

  /**
   * Replaces if statements that bool literals as conditions.
   *
   * <p>Visits the root and any reachable nodes from the root to replace
   * any IfStatement reachable node containing a BooleanLiteral condition
   * with nothing (if false) or the statements within the block.
   *
   * <p>top := all nodes reachable from root such that each node
   * is an outermost IfStatement that contains a BooleanLiteral
   *
   * <p>parents := all nodes such that each one is the parent
   * of some node in top
   *
   * <p>isFoldable(n) := isIfStatement(n)
   * /\ isBooleanLiteral(expression(isIfStatement(n)))
   * || isFoldable(expression(n)))
   *
   * <p>literal(n) := if isLiteral(n) then n else literal(expression(n))
   *
   * @param root the root of the tree to traverse.
   * @return true if IfStatment with boolean literal was replaced in the rooted
   *         tree
   * @modifies nodes in parents
   * @requires root != null
   * @requires (root instanceof CompilationUnit) \/ parent(root) != null
   *         (children(parent(n)) setminus {n})
   * @ensures forall n in old(top), exists n' in nodes
   *         fresh(n')
   *         /\ isBooleanLiteral(n')
   *         /\ odd number of nested prefixes ==> boolVal(n') == !boolVal(n)
   *         /\ even number of nested prefixes ==> boolVal(n') == boolVal(n)
   *         /\ children(parent(n')) == (children(parent(n)) setminus {n})
   *         /\ isBooleanLiteral(expression(isIfStatement(n))) == false =>
   *         (children(parent(n)) setminus {n})
   *         /\ isBooleanLiteral(expression(isIfStatement(n))) == true =>
   *         n' is the List of Statement in the block
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

    private boolean isIfBoolExpression(IfStatement exp) {
      return exp.getExpression() instanceof BooleanLiteral;
    }

    @Override
    public void endVisit(IfStatement node) {
      if (!isIfBoolExpression(node)) {
        return;
      }

      // Handle false case
      BooleanLiteral boolLiteral = (BooleanLiteral) node.getExpression();
      if (boolLiteral.booleanValue() == false) {
        if (node.getElseStatement() != null) {
          // replace the child with else
          AST ast = node.getAST();
          ASTNode exp = node.getElseStatement();
          ASTNode newExp = ASTNode.copySubtree(ast, exp);
          TreeModificationUtils.replaceChildInParent(node, newExp);
          didFold = true;
        } else {
          // We want to remove entire if statement
          TreeModificationUtils.removeChildInParent(node);
          didFold = true;
        }
      } else {
        // Handle true case
        AST ast = node.getAST();
        ASTNode exp = node.getThenStatement();
        ASTNode newExp = ASTNode.copySubtree(ast, exp);
        TreeModificationUtils.replaceChildInParent(node, newExp);
        didFold = true;
      }
    }
  }
}
