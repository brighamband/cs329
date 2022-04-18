package edu.byu.cs329.rd;

import edu.byu.cs329.cfg.ControlFlowGraph;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockUtils {
  public static ControlFlowGraph newMockForEmptyMethodWithTwoParameters(String first, String second) {
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    Statement statement = mock(Statement.class);
    when(cfg.getStart()).thenReturn(statement);
    MethodDeclaration methodDeclaration = mock(MethodDeclaration.class);
    VariableDeclaration firstParameter = newMockForVariableDeclaration(first);
    VariableDeclaration secondParameter = newMockForVariableDeclaration(second);
    List<VariableDeclaration> parameterList = new ArrayList<>();
    parameterList.add(firstParameter);
    parameterList.add(secondParameter);
    when(methodDeclaration.parameters()).thenReturn(parameterList);
    when(cfg.getMethodDeclaration()).thenReturn(methodDeclaration);
    return cfg;
  }

  public static ControlFlowGraph newMockForEmptyMethodWithNoParameters() {
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    Statement statement = mock(Statement.class);
    when(cfg.getStart()).thenReturn(statement);
    MethodDeclaration methodDeclaration = mock(MethodDeclaration.class);
    List<VariableDeclaration> parameterList = new ArrayList<>();
    when(methodDeclaration.parameters()).thenReturn(parameterList);
    when(cfg.getMethodDeclaration()).thenReturn(methodDeclaration);
    return cfg;
  }

  /**
   * VariableDeclaration
   *    Mock that declaration.getName() returns SimpleName
   *    Mock that simpleName.getIdentifier() returns String (name)
   */
  public static VariableDeclaration newMockForVariableDeclaration(String name) {
    VariableDeclaration declaration = mock(VariableDeclaration.class);
    SimpleName simpleName = mock(SimpleName.class);
    when(declaration.getName()).thenReturn(simpleName);
    when(simpleName.getIdentifier()).thenReturn(name);
    return declaration;
  }

  /**
   * VariableDeclarationStatement
   *    Mock that fragments() returns List<VariableDeclaration>
   */
  public static VariableDeclarationStatement newMockForVariableDeclarationStatement(String name) {
    VariableDeclarationStatement declaration = mock(VariableDeclarationStatement.class);
    List<VariableDeclaration> vdList = new ArrayList<>();
    // Mock name and identifier via call
    VariableDeclaration variableDeclaration = newMockForVariableDeclaration(name);
    vdList.add(variableDeclaration);
    // Mock fragments()
    when(declaration.fragments()).thenReturn(vdList);
    return declaration;
  }

  /**
   * Assignment
   *    Mock that getLeftHandSide() returns Expression  (in our case, SimpleName)
   *    Mock that getOperator() returns Assignment.Operator
   *    Mock that getRightHandSide() returns Expression  (in our case, NumberLiteral)
   */
  public static Assignment newMockForAssignment(String name, String value) {
    Assignment assignment = mock(Assignment.class);
    // Mock left side
    SimpleName simpleName = newMockForSimpleName(name);
    when(assignment.getLeftHandSide()).thenReturn(simpleName);
    // Mock operator
    when(assignment.getOperator()).thenReturn(Assignment.Operator.ASSIGN);
    // Mock right side
    NumberLiteral numberLiteral = mock(NumberLiteral.class);  // We'll just mock NumLiterals, not Strings
    when(assignment.getRightHandSide()).thenReturn(numberLiteral);
    when(numberLiteral.getToken()).thenReturn(value);
    return assignment;
  }

  /**
   * ExpressionStatement
   *    Mock that getExpression() returns Expression
   */
  public static ExpressionStatement newMockForExpressionStatement(String name, String value) {
    ExpressionStatement expressionStatement = mock(ExpressionStatement.class);
    Assignment assignment = newMockForAssignment(name, value);
    when(expressionStatement.getExpression()).thenReturn(assignment);
    return expressionStatement;
  }

  /**
   * SimpleName
   *    Mock that getIdentifier() returns SimpleName
   */
  public static SimpleName newMockForSimpleName(String name) {
    SimpleName simpleName = mock(SimpleName.class);
    when(simpleName.getIdentifier()).thenReturn(name);
    return simpleName;
  }

  /**
   * MethodDeclaration
   *    Mock that getName() returns SimpleName
   *    Mock that getBody() returns Block
   */
  public static MethodDeclaration newMockForMethodDeclaration(String name) {
    MethodDeclaration methodDeclaration = mock(MethodDeclaration.class);
    SimpleName simpleName = MockUtils.newMockForSimpleName(name);
    when(methodDeclaration.getName()).thenReturn(simpleName);
    Block emptyBlock = MockUtils.newMockForEmptyBlock();
    when(methodDeclaration.getBody()).thenReturn(emptyBlock);
    return methodDeclaration;
  }

  /**
   * Block
   *    Mock that statements() returns List
   */
  public static Block newMockForBlock(Statement statement) {
    Block block = mock(Block.class);
    List<Statement> statementList = new ArrayList<>();
    if (statement != null) {
      statementList.add(statement);
    }
    when(block.statements()).thenReturn(statementList);
    return block;
  }

  public static Block newMockForEmptyBlock() {
    return newMockForBlock(null);
  }

  /**
   * IfStatement
   *    Mock that getExpression() returns Expression
   *    Mock that getThenStatement() returns Statement
   *    Mock that getElseStatement() returns Statement
   */
  public static IfStatement newMockForIfStatement(Statement thenStatement,
                                                  Statement elseStatement) {
    IfStatement ifStatement = mock(IfStatement.class);
    Expression expression = mock(Expression.class);
    when(ifStatement.getExpression()).thenReturn(expression);
    when(ifStatement.getThenStatement()).thenReturn(thenStatement);
    when(ifStatement.getElseStatement()).thenReturn(elseStatement);
    return ifStatement;
  }

  /**
   * ReturnStatement
   *    Mock that getExpression returns Expression
   */
  public static ReturnStatement newMockForReturnStatement() {
    ReturnStatement returnStatement = mock(ReturnStatement.class);
    Expression expression = mock(Expression.class);
    when(returnStatement.getExpression()).thenReturn(expression);
    return returnStatement;
  }

  /**
   * WhileStatement
   *    Mock that getBody() returns Statement
   *    Mock that getExpression returns Expression
   */
  public static WhileStatement newMockForWhileStatement(Statement statement) {
    WhileStatement whileStatement = mock(WhileStatement.class);
    BooleanLiteral booleanLiteral = mock(BooleanLiteral.class);
    when(booleanLiteral.booleanValue()).thenReturn(true);
    when(whileStatement.getExpression()).thenReturn(booleanLiteral);
    Block block = newMockForBlock(statement);
    when(whileStatement.getBody()).thenReturn(block);
    return whileStatement;
  }
}
