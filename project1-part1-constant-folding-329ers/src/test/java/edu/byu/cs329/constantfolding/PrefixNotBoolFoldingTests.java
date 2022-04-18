package edu.byu.cs329.constantfolding;

import edu.byu.cs329.TestUtils;
import java.net.URI;
import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tests for folding PrefixExpressions with !bool types")
public class PrefixNotBoolFoldingTests {
  PrefixNotBoolFolding folderUnderTest = null;

  @BeforeEach
  void beforeEach() {
    folderUnderTest = new PrefixNotBoolFolding();
  }

  @Test
  @DisplayName("Should throw RuntimeException when root is null")
  void should_ThrowRuntimeException_when_RootIsNull() {
    assertThrows(RuntimeException.class, () -> {
      folderUnderTest.fold(null);
    });
  }

  @Test
  @DisplayName("Should throw RuntimeException when root is not a CompilationUnit and has no parent")
  void should_ThrowRuntimeException_when_RootIsNotACompilationUnitAndHasNoParent() {
    assertThrows(RuntimeException.class, () -> {
      URI uri = TestUtils.getUri(this, "");
      ASTNode compilationUnit = TestUtils.getCompilationUnit(uri);
      ASTNode root = compilationUnit.getAST().newNullLiteral();
      folderUnderTest.fold(root);
    });
  }

  @Test
  @DisplayName("Should not fold when there are no not bools")
  void should_NotFold_when_NoNotBools() {
    String rawName = "foldingInputs/prefixNotBool/Should_NotFold_when_NoNotBools.java";
    String expectedName = "foldingInputs/prefixNotBool/Should_NotFold_when_NoNotBools.java";
    TestUtils.assertDidNotFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold to flipped bool when a bool has a single not operator")
  void should_FoldToFlippedBool_when_BoolHasSingleNot() {
    String rawName = "foldingInputs/prefixNotBool/Should_FoldToFlippedBool_when_BoolHasSingleNot-raw.java";
    String expectedName = "foldingInputs/prefixNotBool/Should_FoldToFlippedBool_when_BoolHasSingleNot-expected.java";
    TestUtils.assertDidFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold to same bool when a bool has a multiple not operators")
  void should_FoldToSameBool_when_BoolHasMultiNot() {
    String rawName = "foldingInputs/prefixNotBool/Should_FoldToSameBool_when_BoolHasMultiNot-raw.java";
    String expectedName = "foldingInputs/prefixNotBool/Should_FoldToSameBool_when_BoolHasMultiNot-expected.java";
    TestUtils.assertDidFold(this, rawName, expectedName, folderUnderTest);
  }
}