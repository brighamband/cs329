package edu.byu.cs329.constantfolding;

import edu.byu.cs329.TestUtils;
import java.net.URI;
import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tests for folding ParenthesizedExpression types")
public class ParenthesizedExpressionFoldingTests {
  ParenthesizedExpressionFolding folderUnderTest = null;

  @BeforeEach
  void beforeEach() {
    folderUnderTest = new ParenthesizedExpressionFolding();
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
  @DisplayName("Should not fold anything when there are no parenthesized literals")
  void should_NotFold_when_NoParenthesizedLiterals() {
    String rawName = "foldingInputs/parenthesizedLiterals/Should_NotFold_when_NoParenthesizedLiterals.java";
    String expectedName = "foldingInputs/parenthesizedLiterals/Should_NotFold_when_NoParenthesizedLiterals.java";
    TestUtils.assertDidNotFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should only fold parenthesized literals when given multiple types")
  void should_OnlyFoldParenthesizedLiterals_when_GivenMultipleTypes() {
    String rawName = "foldingInputs/parenthesizedLiterals/Should_OnlyFoldParenthesizedLiterals_when_GivenMultipleTypes-raw.java";
    String expectedName = "foldingInputs/parenthesizedLiterals/Should_OnlyFoldParenthesizedLiterals_when_GivenMultipleTypes-expected.java";
    TestUtils.assertDidFold(this, rawName, expectedName, folderUnderTest);
  }
}