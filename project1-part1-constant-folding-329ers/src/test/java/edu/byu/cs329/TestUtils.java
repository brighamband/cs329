package edu.byu.cs329;

import edu.byu.cs329.constantfolding.ConstantFolding;
import edu.byu.cs329.constantfolding.Folding;
import edu.byu.cs329.constantpropagation.ConstantPropagation;
import edu.byu.cs329.utils.JavaSourceUtils;
import java.net.URI;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils extends JavaSourceUtils {
  static Logger log = LoggerFactory.getLogger(TestUtils.class);

  public static ASTNode getASTNodeFor(final Object t, String name) {
    URI uri = JavaSourceUtils.getUri(t, name);
    assertNotNull(uri);
    ASTNode root = JavaSourceUtils.getCompilationUnit(uri);
    return root;
  }

  public static void assertDidFold(final Object t, String rawName, String expectedName, Folding folderUnderTest) {
    ASTNode root = getASTNodeFor(t, rawName);
    Boolean didFold = folderUnderTest.fold(root);
    log.debug(root.toString());
    assertTrue(didFold);
    ASTNode expected = getASTNodeFor(t, expectedName);
    assertTrue(expected.subtreeMatch(new ASTMatcher(), root));
  }

  public static void assertDidNotFold(final Object t, String rawName, String expectedName, Folding folderUnderTest) {
    ASTNode root = getASTNodeFor(t, rawName);
    assertFalse(folderUnderTest.fold(root));
    ASTNode expected = getASTNodeFor(t, expectedName);
    assertTrue(expected.subtreeMatch(new ASTMatcher(), root));
  }

  public static void assertDidFoldIntegration(final Object t, String rawName, String expectedName) {
    ASTNode root = getASTNodeFor(t, rawName);
    ConstantFolding.fold(root);
    log.debug(root.toString());
    ASTNode expected = getASTNodeFor(t, expectedName);
    assertTrue(expected.subtreeMatch(new ASTMatcher(), root));
  }

  public static void assertDidPropagate(final Object t, String rawName, String expectedName) {
    ASTNode root = getASTNodeFor(t, rawName);
    boolean didPropagate = ConstantPropagation.propagate(root);
    log.debug(root.toString());
    assertTrue(didPropagate);
    ASTNode expected = getASTNodeFor(t, expectedName);
    assertTrue(expected.subtreeMatch(new ASTMatcher(), root));
  }

  public static void assertDidNotPropagate(final Object t, String rawName, String expectedName) {
    ASTNode root = getASTNodeFor(t, rawName);
    assertFalse(ConstantPropagation.propagate(root));
    ASTNode expected = getASTNodeFor(t, expectedName);
    assertTrue(expected.subtreeMatch(new ASTMatcher(), root));
  }

}
