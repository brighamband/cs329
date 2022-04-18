package edu.byu.cs329.constantfolding;

import edu.byu.cs329.TestUtils;
import java.net.URI;
import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tests for folding BinaryRelation less than types")
public class BinaryRelationFoldingTests {
  BinaryRelationFolding folderUnderTest = null;

  @BeforeEach
  void beforeEach() {
    folderUnderTest = new BinaryRelationFolding();
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
  @DisplayName("Should not fold when there's no less than operator")
  void should_NotFold_when_NoLessThan() {
    String rawName = "foldingInputs/binaryRelation/Should_NotFold_when_NoLessThan.java";
    String expectedName = "foldingInputs/binaryRelation/Should_NotFold_when_NoLessThan.java";
    TestUtils.assertDidNotFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold to true when right is less than left")
  void should_FoldToTrue_When_RightIsGreater() {
    String rawName = "foldingInputs/binaryRelation/Should_FoldToTrue_When_RightIsGreater-raw.java";
    String expectedName = "foldingInputs/binaryRelation/Should_FoldToTrue_When_RightIsGreater-expected.java";
    TestUtils.assertDidFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold to false when left is greater than right")
  void should_FoldToFalse_When_LeftIsGreater() {
    String rawName = "foldingInputs/binaryRelation/Should_FoldToFalse_When_LeftIsGreater-raw.java";
    String expectedName = "foldingInputs/binaryRelation/Should_FoldToFalse_When_LeftIsGreater-expected.java";
    TestUtils.assertDidFold(this, rawName, expectedName, folderUnderTest);
  }
}
