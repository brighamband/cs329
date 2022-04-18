package edu.byu.cs329.constantfolding;

import edu.byu.cs329.TestUtils;
import java.net.URI;
import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tests for folding Ifs with BooleanLiteral types")
public class IfBoolFoldingTests {
  IfBoolFolding folderUnderTest = null;

  @BeforeEach
  void beforeEach() {
    folderUnderTest = new IfBoolFolding();
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
  @DisplayName("Should not fold when there's an if statement but doesn't have a bool condition")
  void should_NotFold_when_NotIfBool() {
    String rawName = "foldingInputs/ifBool/Should_NotFold_when_NotIfBool.java";
    String expectedName = "foldingInputs/ifBool/Should_NotFold_when_NotIfBool.java";
    TestUtils.assertDidNotFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold and extract the block if if statement has a 'true' bool condition (always true)")
  void should_FoldAndExtractBlock_when_IfTrue() {
    String rawName = "foldingInputs/ifBool/Should_FoldAndExtractBlock_when_IfTrue-raw.java";
    String expectedName = "foldingInputs/ifBool/Should_FoldAndExtractBlock_when_IfTrue-expected.java";
    TestUtils.assertDidFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold if statement entirely if if statement has a 'false' bool condition (always false)")
  void should_FoldIfStatement_when_IfFalse() {
    String rawName = "foldingInputs/ifBool/Should_FoldIfStatement_when_IfFalse-raw.java";
    String expectedName = "foldingInputs/ifBool/Should_FoldIfStatement_when_IfFalse-expected.java";
    TestUtils.assertDidFold(this, rawName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold if else statement to just be else statement if if statement has a 'false' bool condition (always false), therefore else is always true")
  void should_FoldToElseBlock_when_IfFalse() {
    String rawName = "foldingInputs/ifBool/Should_FoldToElseBlock_when_IfFalse-raw.java";
    String expectedName = "foldingInputs/ifBool/Should_FoldToElseBlock_when_IfFalse-expected.java";
    TestUtils.assertDidFold(this, rawName, expectedName, folderUnderTest);
  }
}