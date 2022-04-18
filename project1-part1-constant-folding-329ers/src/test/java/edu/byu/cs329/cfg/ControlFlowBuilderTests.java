package edu.byu.cs329.cfg;

import edu.byu.cs329.TestUtils;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests for ControlFlowBuilder")
public class ControlFlowBuilderTests {
  ControlFlowGraphBuilder unitUnderTest = null;
  ControlFlowGraph controlFlowGraph = null;
  StatementTracker statementTracker = null;

  @BeforeEach
  void beforeEach() {
    unitUnderTest = new ControlFlowGraphBuilder();
  }

  void init(String fileName) {
    ASTNode node = TestUtils.getASTNodeFor(this, fileName);
    List<ControlFlowGraph> cfgList = unitUnderTest.build(node);
    assertEquals(1, cfgList.size());
    controlFlowGraph = cfgList.get(0);
    statementTracker = new StatementTracker(node);
  }

  @Test
  @Tag("MethodDeclaration")
  @DisplayName("Should set start and end same when empty method declaration")
  void should_SetStartAndEndSame_when_EmptyMethodDeclaration() {
    String fileName = "cfgInputs/should_SetStartAndEndSame_when_EmptyMethodDeclaration.java";
    init(fileName);
    assertAll("Method declaration with empty block",
            () -> assertNotNull(controlFlowGraph.getMethodDeclaration()),
            () -> assertEquals(controlFlowGraph.getStart(), controlFlowGraph.getEnd())
    );
  }

  @Test
  @Tag("MethodDeclaration")
  @DisplayName("Should set start to first statement and end different when non-empty method declaration")
  void should_SetStartToFirstStatementAndEndDifferent_when_NonEmptyMethodDeclaration() {
    String fileName = "cfgInputs/should_SetStartToFirstStatementAndEndDifferent_when_NonEmptyMethodDeclaration.java";
    init(fileName);
    Statement start = controlFlowGraph.getStart();
    Statement end = controlFlowGraph.getEnd();
    Statement variableDeclStatement = statementTracker.getVariableDeclarationStatement(0);
    assertAll("Method declaration with non-empty block",
            () -> assertNotNull(controlFlowGraph.getMethodDeclaration()),
            () -> assertNotEquals(start, end),
            () -> assertTrue(start == variableDeclStatement),
            () -> assertTrue(hasEdge(variableDeclStatement, end))
    );
  }

  @Test
  @Tag("Block")
  @DisplayName("Should link all when block has no return")
  void should_LinkAll_when_BlockHasNoReturn() {
    String fileName = "cfgInputs/should_LinkAll_when_BlockHasNoReturn.java";
    init(fileName);
    Statement variableDeclaration = statementTracker.getVariableDeclarationStatement(0);
    Statement expressionStatement = statementTracker.getExpressionStatement(0);
    assertTrue(hasEdge(variableDeclaration, expressionStatement));
  }

  @Test
  @Tag("Block")
  @DisplayName("Should link to return when block has return")
  void should_LinkToReturn_when_BlockHasReturn() {
    String fileName = "cfgInputs/should_LinkToReturn_when_BlockHasReturn.java";
    init(fileName);
    Statement variableDeclaration = statementTracker.getVariableDeclarationStatement(0);
    Statement expressionStatement = statementTracker.getExpressionStatement(0);
    Statement returnStatement = statementTracker.getReturnStatement(0);
    assertAll(
            () -> assertTrue(hasEdge(variableDeclaration, returnStatement)),
            () -> assertFalse(hasEdge(returnStatement, expressionStatement))
    );
  }

  private boolean hasEdge(Statement source, Statement dest) {
    Set<Statement> successors = controlFlowGraph.getSuccs(source);
    Set<Statement> predecessors = controlFlowGraph.getPreds(dest);
    return successors != null && successors.contains(dest)
            && predecessors != null && predecessors.contains(source);
  }

  @Test
  @Tag("ReturnStatement")
  @DisplayName("Should add edge to end when has return")
  void should_AddEdgeToEnd_when_HasReturn() {
    String fileName = "cfgInputs/should_AddEdgeToEnd_when_HasReturn.java";
    init(fileName);

    Statement returnStatement = statementTracker.getReturnStatement(0);
    assertTrue(hasEdge(returnStatement, controlFlowGraph.getEnd()));
  }

  @Test
  @Tag("ReturnStatement")
  @DisplayName("Should union statements edges to the return when one incoming statement edge")
  void should_UnionEdges_when_OneIncomingEdge() {
    String fileName = "cfgInputs/should_UnionEdges_when_OneIncomingEdge.java";
    init(fileName);

    Statement variableDeclaration = statementTracker.getVariableDeclarationStatement(0);
    Statement returnStatement = statementTracker.getReturnStatement(0);
    assertTrue(hasEdge(variableDeclaration, returnStatement));
  }

  @Test
  @Tag("ReturnStatement")
  @DisplayName("Should union statements edges to the return when multi incoming statement edge")
  void should_UnionEdges_when_MultiIncomingEdges() {
    String fileName = "cfgInputs/should_UnionEdges_when_MultiIncomingEdges.java";
    init(fileName);

    Statement expressionStatementInIf = statementTracker.getExpressionStatement(0);
    Statement expressionStatementInElse = statementTracker.getExpressionStatement(1);
    Statement returnStatement = statementTracker.getReturnStatement(0);
    assertTrue(hasEdge(expressionStatementInIf, returnStatement));
    assertTrue(hasEdge(expressionStatementInElse, returnStatement));
  }

  @Test
  @Tag("WhileStatement")
  @DisplayName("Should loop back to the while when there's no return in while")
  void should_LoopBack_when_NoReturnInWhile() {
    String fileName = "cfgInputs/should_LoopBack_when_NoReturnInWhile.java";
    init(fileName);

    Statement whileStatement = statementTracker.getWhileStatement(0);
    Statement innerExpressionStatement = statementTracker.getExpressionStatement(0);
    Statement returnStatement = statementTracker.getReturnStatement(0);
    assertTrue(hasEdge(whileStatement, innerExpressionStatement));
    assertTrue(hasEdge(innerExpressionStatement, whileStatement));
    // Shouldn't have edge from inner to return
    assertFalse(hasEdge(innerExpressionStatement, returnStatement));
  }

  @Test
  @Tag("WhileStatement")
  @DisplayName("Should connect to end when there's a return in while")
  void should_ConnectToEnd_when_ReturnInWhile() {
    String fileName = "cfgInputs/should_ConnectToEnd_when_ReturnInWhile.java";
    init(fileName);

    Statement whileStatement = statementTracker.getWhileStatement(0);
    Statement innerReturnStatement = statementTracker.getReturnStatement(0);
    Statement end = controlFlowGraph.getEnd();
    assertTrue(hasEdge(whileStatement, innerReturnStatement));
    assertTrue(hasEdge(innerReturnStatement, end));
    // Shouldn't have edge from inner back to while
    assertFalse(hasEdge(innerReturnStatement, whileStatement));
  }

  @Test
  @Tag("WhileStatement")
  @DisplayName("Should add self loop when no statements in while")
  void should_AddSelfLoop_when_NoStatementsInWhile() {
    String fileName = "cfgInputs/should_AddSelfLoop_when_NoStatementsInWhile.java";
    init(fileName);

    Statement whileStatement = statementTracker.getWhileStatement(0);
    // Has self edge/loop
    assertTrue(hasEdge(whileStatement, whileStatement));
  }

  @Test
  @Tag("IfStatement")
  @DisplayName("Should branch and merge when then and else")
  void should_BranchAndMerge_when_ThenAndElse() {
    String fileName = "cfgInputs/should_BranchAndMerge_when_ThenAndElse.java";
    init(fileName);

    Statement ifStatement = statementTracker.getIfStatement(0);
    Statement thenExpressionStatement = statementTracker.getExpressionStatement(0);
    Statement elseExpressionStatement = statementTracker.getExpressionStatement(1);
    Statement returnStatement = statementTracker.getReturnStatement(0);

    // Has branch to then
    assertTrue(hasEdge(ifStatement, thenExpressionStatement));
    // Has branch to else
    assertTrue(hasEdge(ifStatement, elseExpressionStatement));
    // Then merges to return
    assertTrue(hasEdge(thenExpressionStatement, returnStatement));
    // Else merges to return
    assertTrue(hasEdge(elseExpressionStatement, returnStatement));
  }

  @Test
  @Tag("IfStatement")
  @DisplayName("Should merge when then but no else")
  void should_Merge_when_ThenButNoElse() {
    String fileName = "cfgInputs/should_Merge_when_ThenButNoElse.java";
    init(fileName);

    Statement ifStatement = statementTracker.getIfStatement(0);
    Statement thenExpressionStatement = statementTracker.getExpressionStatement(0);
    Statement returnStatement = statementTracker.getReturnStatement(0);

    // Has edge to then
    assertTrue(hasEdge(ifStatement, thenExpressionStatement));
    // Has edge to return
    assertTrue(hasEdge(ifStatement, returnStatement));
    // Then has edge to return
    assertTrue(hasEdge(thenExpressionStatement, returnStatement));
  }

  @Test
  @Tag("IfStatement")
  @DisplayName("Should connect to end when return in then")
  void should_ConnectToEnd_when_ReturnInThen() {
    String fileName = "cfgInputs/should_ConnectToEnd_when_ReturnInThen.java";
    init(fileName);

    Statement ifStatement = statementTracker.getIfStatement(0);
    Statement thenExpressionStatement = statementTracker.getExpressionStatement(0);
    Statement firstThenExpression = statementTracker.getExpressionStatement(0);
    Statement innerReturnStatement = statementTracker.getReturnStatement(0);
    Statement secondThenExpression = statementTracker.getExpressionStatement(1);
    Statement elseExpression = statementTracker.getExpressionStatement(2);
    Statement finalReturnStatement = statementTracker.getReturnStatement(1);
    Statement end = controlFlowGraph.getEnd();

    // If statement should have edge to then
    assertTrue(hasEdge(ifStatement, thenExpressionStatement));
    // Expression should have edge to return 0
    assertTrue(hasEdge(firstThenExpression, innerReturnStatement));
    // Inner return should have edge to end
    assertTrue(hasEdge(innerReturnStatement, end));
    // Inner return should not have edge to next expression
    assertFalse(hasEdge(innerReturnStatement, secondThenExpression));
    // If statement should have edge to else
    assertTrue(hasEdge(ifStatement, elseExpression));
    // Else should have edge to statement after if block
    assertTrue(hasEdge(elseExpression, finalReturnStatement));
  }

  @Test
  @Tag("IfStatement")
  @DisplayName("Should connect to end when return in else")
  void should_ConnectToEnd_when_ReturnInElse() {
    String fileName = "cfgInputs/should_ConnectToEnd_when_ReturnInElse.java";
    init(fileName);

    Statement ifStatement = statementTracker.getIfStatement(0);
    Statement thenExpressionStatement = statementTracker.getExpressionStatement(0);
    Statement firstElseExpression = statementTracker.getExpressionStatement(1);
    Statement innerReturnStatement = statementTracker.getReturnStatement(0);
    Statement secondElseExpression = statementTracker.getExpressionStatement(2);
    Statement finalReturnStatement = statementTracker.getReturnStatement(1);
    Statement end = controlFlowGraph.getEnd();

    // If statement should have edge to then
    assertTrue(hasEdge(ifStatement, thenExpressionStatement));
    // If statement should have edge to else
    assertTrue(hasEdge(ifStatement, firstElseExpression));
    // Expression should have edge to return 0
    assertTrue(hasEdge(firstElseExpression, innerReturnStatement));
    // Inner return should have edge to end
    assertTrue(hasEdge(innerReturnStatement, end));
    // Inner return should not have edge to next expression
    assertFalse(hasEdge(innerReturnStatement, secondElseExpression));
    // If statement should have edge to statement after if block
    assertTrue(hasEdge(ifStatement, finalReturnStatement));
  }
}
