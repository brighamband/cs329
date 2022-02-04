package edu.byu.cs329.constantfolding;

import edu.byu.cs329.utils.ExceptionUtils;
import edu.byu.cs329.utils.TreeModificationUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replaces parenthesized literals with the literal.
 * 
 * @author Eric Mercer
 */
public class ParenthesizedExpressionFolding implements Folding {
  static final Logger log = LoggerFactory.getLogger(ParenthesizedExpressionFolding.class);
  
  class Visitor extends ASTVisitor {
    public boolean didFold = false;

    private boolean isLiteralExpression(ASTNode exp) {
      return (exp instanceof BooleanLiteral) 
        || (exp instanceof CharacterLiteral)
        || (exp instanceof NullLiteral)
        || (exp instanceof StringLiteral)
        || (exp instanceof TypeLiteral)
        || (exp instanceof NumberLiteral);
    }
    
    @Override
    public void endVisit(ParenthesizedExpression node) {
      ASTNode exp = node.getExpression();
      if (!isLiteralExpression(exp)) {
        return;
      }
      AST ast = node.getAST();
      ASTNode newExp = ASTNode.copySubtree(ast, exp);
      TreeModificationUtils.replaceChildInParent(node, newExp);
      didFold = true;
    }
  }

  public ParenthesizedExpressionFolding() {
  } 
  
  /**
   * Replaces parenthesized literals in the tree with the literals.
   * 
   * <p>Visits the root and any reachable nodes from the root to replace
   * any ParenthesizedExpression reachable node containing a literal
   * with the literal itself.
   *
   * <p>top := all nodes reachable from root such that each node 
   *           is an outermost parenthesized expression that ends
   *           in a literal
   * 
   * <p>parents := all nodes such that each one is the parent
   *               of some node in top
   * 
   * <p>isFoldable(n) :=    isParenthesizedExpression(n)
   *                     /\ (   isLiteral(expression(n))
   *                         || isFoldable(expression(n)))
   * 
   * <p>literal(n) := if isLiteral(n) then n else literal(expression(n))
   * 
   * @modifies nodes in parents
   * 
   * @requires root != null
   * @requires (root instanceof CompilationUnit) \/ parent(root) != null
   * 
   * @ensures fold(root) == (old(top) != emptyset)
   * @ensures forall n in old(top), exists n' in nodes 
   *             fresh(n')
   *          /\ isLiteral(n')
   *          /\ value(n') == value(literal(n))
   *          /\ parent(n') == parent(n)
   *          /\ children(parent(n')) == (children(parent(n)) setminus {n}) union {n'}
   *   
   * @param root the root of the tree to traverse.
   * @return true if parenthesized literals were replaced in the rooted tree
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
          "Non-CompilationUnit root with no parent passed to ParenthesizedExpressionFolding.fold"
      );
    }
  }
}
