package edu.byu.cs329.constantfolding;

import edu.byu.cs329.TestUtils;
import java.net.URI;
import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tests for folding Numeric Plus Infix Expressions")
public class NumericPlusInfixFoldingTests {
  NumericPlusInfixFolding folderUnderTest = null;

  @BeforeEach
  void beforeEach() {
    folderUnderTest = new NumericPlusInfixFolding();
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
  @DisplayName("Should not fold anything when standard infix uses the wrong operator (minus, not plus)")
  void should_NotFold_when_StdInfixWrongOperator() {
    String rawName = "foldingInputs/numericPlusInfix/Should_NotFold_when_StdInfixWrongOperator.java";
    String expectedName = "foldingInputs/numericPlusInfix/Should_NotFold_when_StdInfixWrongOperator.java";
    TestUtils.assertDidNotFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold when standard infix plus operator is used")
  void should_FoldToNum_when_StdPlusInfix() {
    String rawName = "foldingInputs/numericPlusInfix/Should_FoldToNum_when_StdPlusInfix-raw.java";
    String expectedName = "foldingInputs/numericPlusInfix/Should_FoldToNum_when_StdPlusInfix-expected.java";
    TestUtils.assertDidFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should not fold anything when extended infix uses the wrong operator (minus, not plus)")
  void should_NotFold_when_ExtendedInfixWrongOperator() {
    String rawName = "foldingInputs/numericPlusInfix/should_NotFold_when_ExtendedInfixWrongOperator.java";
    String expectedName = "foldingInputs/numericPlusInfix/should_NotFold_when_ExtendedInfixWrongOperator.java";
    TestUtils.assertDidNotFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should completely fold when extended infix plus operator is used")
  void should_FoldToNum_when_ExtendedPlusInfix() {
    String rawName = "foldingInputs/numericPlusInfix/Should_FoldToNum_when_ExtendedPlusInfix-raw.java";
    String expectedName = "foldingInputs/numericPlusInfix/Should_FoldToNum_when_ExtendedPlusInfix-expected.java";
    TestUtils.assertDidFold(this, rawName, expectedName, folderUnderTest);
  }
}
