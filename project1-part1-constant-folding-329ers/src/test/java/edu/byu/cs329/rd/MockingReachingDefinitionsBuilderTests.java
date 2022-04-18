package edu.byu.cs329.rd;

import edu.byu.cs329.cfg.ControlFlowGraph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Calls to mock:
 *
 * getStart()
 * getPreds() --  for each node
 * getSuccs() --  for each node
 * getEnd()
  */

@DisplayName("Tests for ReachingDefinitionsBuilder using Mocked Control Flow Graphs")
public class MockingReachingDefinitionsBuilderTests {
  ReachingDefinitionsBuilder unitUnderTest = null;

  @BeforeEach
  void beforeEach() {
    unitUnderTest = new ReachingDefinitionsBuilder();
  }

  private ReachingDefinitions getReachingDefinitions(ControlFlowGraph controlFlowGraph) {
    List<ControlFlowGraph> list = new ArrayList<>();
    list.add(controlFlowGraph);
    List<ReachingDefinitions> reachingDefinitionsList = unitUnderTest.build(list);
    assertEquals(1, reachingDefinitionsList.size());
    return reachingDefinitionsList.get(0);
  }

  private boolean doesDefineParam(String name, final Set<ReachingDefinitions.Definition> definitions) {
    for (ReachingDefinitions.Definition definition : definitions) {
      if (definition.name.getIdentifier().equals(name) && definition.statement == null) {
        return true;
      }
    }
    return false;
  }

  private boolean doesDefineStatement(String name, final Set<ReachingDefinitions.Definition> definitions) {
    for (ReachingDefinitions.Definition definition : definitions) {
      if (definition.name.getIdentifier().equals(name) && definition.statement != null) {
        return true;
      }
    }
    return false;
  }

  private Set<Statement> createSingleSet(Statement statement) {
    Set<Statement> statementSet = new HashSet<>();
    statementSet.add(statement);
    return statementSet;
  }

  private Set<Statement> createDoubleSet(Statement statement1, Statement statement2) {
    Set<Statement> statementSet = new HashSet<>();
    statementSet.add(statement1);
    statementSet.add(statement2);
    return statementSet;
  }

  @Test
  @Tag("Parameters")
  @DisplayName("Should have a definition for each parameter at start when the method declaration has parameters.")
  void should_HaveDefinitionForEachParameterAtStart_when_MockedMethodDeclarationHasParameters() {
    ControlFlowGraph cfg = MockUtils.newMockForEmptyMethodWithTwoParameters("a", "b");
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(cfg);
    Statement start = cfg.getStart();
    Set<ReachingDefinitions.Definition> paramDefinitions = reachingDefinitions.getReachingDefinitions(start);
    assertEquals(2, paramDefinitions.size());
    assertAll("Parameters Defined at Start",
            () -> assertTrue(doesDefineParam("a", paramDefinitions)),
            () -> assertTrue(doesDefineParam("b", paramDefinitions))
    );
  }

  @Test
  @Tag("Empty")
  @DisplayName("Should start and end at empty block when empty")
  void should_StartAndEndAtEmptyBlock_when_Empty() {
    ControlFlowGraph cfg = MockUtils.newMockForEmptyMethodWithNoParameters();

    /**
     * Mocking
     */
    Block b0 = MockUtils.newMockForEmptyBlock();

    /**
     * Start (b0)
     */
    when(cfg.getStart()).thenReturn(b0);
    Statement start = cfg.getStart();

    // Start preds
    when(cfg.getPreds(start)).thenReturn(null);

    // Start succs
    when(cfg.getSuccs(start)).thenReturn(null);

    /**
     * End (b0)
     */
    when(cfg.getEnd()).thenReturn(b0);
    Statement end = cfg.getEnd();

    // End preds
    when(cfg.getPreds(end)).thenReturn(null);

    // End succs
    when(cfg.getSuccs(end)).thenReturn(null);

    /**
     * Testing
     */
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(cfg);

    // Start (b0)
    Set<ReachingDefinitions.Definition> startDefinitions = reachingDefinitions.getReachingDefinitions(start);
    assertEquals(0, startDefinitions.size());
    // End (b0)
    Set<ReachingDefinitions.Definition> endDefinitions = reachingDefinitions.getReachingDefinitions(end);
    assertEquals(0, endDefinitions.size());
  }

  @Test
  @Tag("Sequential")
  @DisplayName("Should have predecessor definitions when sequential shape.")
  void should_HaveDefinitions_when_SequentialShape() {
    ControlFlowGraph cfg = MockUtils.newMockForEmptyMethodWithNoParameters();

    /**
     * Mocking
     */
    VariableDeclarationStatement s1 = MockUtils.newMockForVariableDeclarationStatement("c");
    // int x = 0;
    VariableDeclarationStatement s2 = MockUtils.newMockForVariableDeclarationStatement("d");
    /*
      int y = 2;
     */
    Block b0 = MockUtils.newMockForEmptyBlock();

    /**
     * Start (s1)
     */
    when(cfg.getStart()).thenReturn(s1);
    Statement start = cfg.getStart();

    // Start preds
    when(cfg.getPreds(start)).thenReturn(null);

    // Start succs
    when(cfg.getSuccs(start)).thenReturn(createSingleSet(s2));

    /**
     * S2
     */
    // S2 preds
    when(cfg.getPreds(s2)).thenReturn(createSingleSet(s1));

    // S2 succs
    when(cfg.getSuccs(s2)).thenReturn(createSingleSet(b0));

    /**
     * End (b0)
      */
    when(cfg.getEnd()).thenReturn(b0);
    Statement end = cfg.getEnd();

    // End preds
    when(cfg.getPreds(end)).thenReturn(createSingleSet(s2));

    // End succs
    when(cfg.getSuccs(end)).thenReturn(null);

    /**
     * Testing
     */
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(cfg);

    // Start (s1)
    Set<ReachingDefinitions.Definition> startDefinitions = reachingDefinitions.getReachingDefinitions(start);
    assertEquals(0, startDefinitions.size());
    // S2
    Set<ReachingDefinitions.Definition> s2Definitions = reachingDefinitions.getReachingDefinitions(s2);
    assertAll("'c' defined at s2",
            () -> assertEquals(1, s2Definitions.size()),
            () -> assertTrue(doesDefineStatement("c", s2Definitions))
    );
    // End (b0)
    Set<ReachingDefinitions.Definition> endDefinitions = reachingDefinitions.getReachingDefinitions(end);
    assertAll("'c' and 'd' defined at end",
            () -> assertEquals(2, endDefinitions.size()),
            () -> assertTrue(doesDefineStatement("c", endDefinitions)),
            () -> assertTrue(doesDefineStatement("d", endDefinitions))
    );
  }

  @Test
  @Tag("Branching")
  @DisplayName("Should have a definition for predecessor in each branch.")
  void should_HaveDefinitions_when_BranchingShape() {
    ControlFlowGraph cfg = MockUtils.newMockForEmptyMethodWithNoParameters();

    /**
     * Mocking
     */
    VariableDeclarationStatement s1 = MockUtils.newMockForVariableDeclarationStatement("a");
    VariableDeclarationStatement s3 = MockUtils.newMockForVariableDeclarationStatement("x");
    VariableDeclarationStatement s4 = MockUtils.newMockForVariableDeclarationStatement("y");
    IfStatement i2 = MockUtils.newMockForIfStatement(s3, s4);
    Block b0 = MockUtils.newMockForEmptyBlock();

    /**
     * Start (s1)
     */
    when(cfg.getStart()).thenReturn(s1);
    Statement start = cfg.getStart();

    // Start preds
    when(cfg.getPreds(start)).thenReturn(null);

    // Start succs
    when(cfg.getSuccs(start)).thenReturn(createSingleSet(i2));

    /**
     * I2
     */

    // I2 preds
    when(cfg.getPreds(i2)).thenReturn(createSingleSet(s1));

    // I2 succs
    when(cfg.getSuccs(i2)).thenReturn(createDoubleSet(s3, s4));

    /**
     * S3
     */

    // S3 preds
    when(cfg.getPreds(s3)).thenReturn(createSingleSet(i2));

    // S3 succs
    when(cfg.getSuccs(s3)).thenReturn(createSingleSet(b0));

    /**
     * S4
     */

    // S4 preds
    when(cfg.getPreds(s4)).thenReturn(createSingleSet(i2));

    // S4 succs
    when(cfg.getSuccs(s4)).thenReturn(createSingleSet(b0));

    /**
     * End (b0)
     */
    when(cfg.getEnd()).thenReturn(b0);
    Statement end = cfg.getEnd();

    // End preds
    when(cfg.getPreds(end)).thenReturn(createDoubleSet(s3, s4));

    // End succs
    when(cfg.getSuccs(end)).thenReturn(null);

    /**
     * Testing
     */
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(cfg);

    // Start (s1)
    Set<ReachingDefinitions.Definition> startDefinitions = reachingDefinitions.getReachingDefinitions(start);
    assertEquals(0, startDefinitions.size());
    // I2
    Set<ReachingDefinitions.Definition> i2Definitions = reachingDefinitions.getReachingDefinitions(i2);
    assertAll("'a' defined at i2",
            () -> assertEquals(1, i2Definitions.size()),
            () -> assertTrue(doesDefineStatement("a", i2Definitions))
    );
    // S3
    Set<ReachingDefinitions.Definition> s3Definitions = reachingDefinitions.getReachingDefinitions(s3);
    assertAll("'a' defined at s3",
            () -> assertEquals(1, s3Definitions.size()),
            () -> assertTrue(doesDefineStatement("a", s3Definitions))
    );
    // S4
    Set<ReachingDefinitions.Definition> s4Definitions = reachingDefinitions.getReachingDefinitions(s4);
    assertAll("'a' defined at s3",
            () -> assertEquals(1, s4Definitions.size()),
            () -> assertTrue(doesDefineStatement("a", s4Definitions))
    );
    // End (b0)
    Set<ReachingDefinitions.Definition> endDefinitions = reachingDefinitions.getReachingDefinitions(end);
    assertAll("'a', 'x', and 'y' defined at end",
            () -> assertEquals(3, endDefinitions.size()),
            () -> assertTrue(doesDefineStatement("a", endDefinitions)),
            () -> assertTrue(doesDefineStatement("x", endDefinitions)),
            () -> assertTrue(doesDefineStatement("y", endDefinitions))
    );
  }


  @Test
  @Tag("Merging")
  @DisplayName("Should have updated definitions of branched statements after merge.")
  void should_HaveDefinitions_when_MergingShape() {
    ControlFlowGraph cfg = MockUtils.newMockForEmptyMethodWithNoParameters();

    /**
     * Mocking
     */
    VariableDeclarationStatement s1 = MockUtils.newMockForVariableDeclarationStatement("x");
    VariableDeclarationStatement s2 = MockUtils.newMockForVariableDeclarationStatement("y");
    ExpressionStatement s4 = MockUtils.newMockForExpressionStatement("x", "1");
    ExpressionStatement s5 = MockUtils.newMockForExpressionStatement("y", "2");
    IfStatement i3 = MockUtils.newMockForIfStatement(s4, s5);
    ReturnStatement r6 = MockUtils.newMockForReturnStatement();
    Block b0 = MockUtils.newMockForEmptyBlock();

    /**
     * Start (s1)
     */
    when(cfg.getStart()).thenReturn(s1);
    Statement start = cfg.getStart();

    // Start preds
    when(cfg.getPreds(start)).thenReturn(null);

    // Start succs
    when(cfg.getSuccs(start)).thenReturn(createSingleSet(s2));

    /**
     * S2
     */

    // S2 preds
    when(cfg.getPreds(s2)).thenReturn(createSingleSet(s1));

    // S2 succs
    when(cfg.getSuccs(s2)).thenReturn(createSingleSet(i3));


    /**
     * I3
     */

    // I3 preds
    when(cfg.getPreds(i3)).thenReturn(createSingleSet(s2));

    // I3 succs
    when(cfg.getSuccs(i3)).thenReturn(createDoubleSet(s4, s5));

    /**
     * S4
     */

    // S4 preds
    when(cfg.getPreds(s4)).thenReturn(createSingleSet(i3));

    // S4 succs
    when(cfg.getSuccs(s4)).thenReturn(createSingleSet(r6));

    /**
     * S5
     */

    // S5 preds
    when(cfg.getPreds(s5)).thenReturn(createSingleSet(i3));

    // S5 succs
    when(cfg.getSuccs(s5)).thenReturn(createSingleSet(r6));

    /**
     * R6
     */

    // R6 preds
    when(cfg.getPreds(r6)).thenReturn(createDoubleSet(s4, s5));

    // R6 succs
    when(cfg.getSuccs(r6)).thenReturn(createSingleSet(b0));

    /**
     * End (b0)
     */
    when(cfg.getEnd()).thenReturn(b0);
    Statement end = cfg.getEnd();

    // End preds
    when(cfg.getPreds(end)).thenReturn(createSingleSet(r6));

    // End succs
    when(cfg.getSuccs(end)).thenReturn(null);


    /**
     * Testing
     */
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(cfg);

    // Start (s1)
    Set<ReachingDefinitions.Definition> startDefinitions = reachingDefinitions.getReachingDefinitions(start);
    assertEquals(0, startDefinitions.size());
    // S2
    Set<ReachingDefinitions.Definition> s2Definitions = reachingDefinitions.getReachingDefinitions(s2);
    assertAll("'x' defined at s2",
            () -> assertEquals(1, s2Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", s2Definitions))
    );
    // I3
    Set<ReachingDefinitions.Definition> i3Definitions = reachingDefinitions.getReachingDefinitions(i3);
    assertAll("'x' and 'y' defined at i3",
            () -> assertEquals(2, i3Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", i3Definitions)),
            () -> assertTrue(doesDefineStatement("y", i3Definitions))
    );
    // S4
    Set<ReachingDefinitions.Definition> s4Definitions = reachingDefinitions.getReachingDefinitions(s4);
    assertAll("'x' and 'y' defined at s4",
            () -> assertEquals(2, s4Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", s4Definitions)),
            () -> assertTrue(doesDefineStatement("y", s4Definitions))
    );
    // S5
    Set<ReachingDefinitions.Definition> s5Definitions = reachingDefinitions.getReachingDefinitions(s5);
    assertAll("'x' and 'y' defined at s5",
            () -> assertEquals(2, s5Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", s5Definitions)),
            () -> assertTrue(doesDefineStatement("y", s5Definitions))
    );
    // R6
    Set<ReachingDefinitions.Definition> r6Definitions = reachingDefinitions.getReachingDefinitions(r6);
    assertAll("'x' and 'y' defined at r6",
            () -> assertEquals(4, r6Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", r6Definitions)),
            () -> assertTrue(doesDefineStatement("y", r6Definitions))
    );
    // End (b0)
    Set<ReachingDefinitions.Definition> endDefinitions = reachingDefinitions.getReachingDefinitions(end);
    assertEquals(4, endDefinitions.size());
  }

  @Test
  @Tag("Looping")
  @DisplayName("Should have correct predecessor definitions in a loop.")
  void should_HaveDefinitions_when_LoopingShape() {
    ControlFlowGraph cfg = MockUtils.newMockForEmptyMethodWithNoParameters();

    /**
     * Mocking
     */
    VariableDeclarationStatement s1 = MockUtils.newMockForVariableDeclarationStatement("x");
    VariableDeclarationStatement s3 = MockUtils.newMockForVariableDeclarationStatement("z");
    WhileStatement w2 = MockUtils.newMockForWhileStatement(s3);
    Block b0 = MockUtils.newMockForEmptyBlock();

    /**
     * Start (s1)
     */
    when(cfg.getStart()).thenReturn(s1);
    Statement start = cfg.getStart();

    // Start preds
    when(cfg.getPreds(start)).thenReturn(null);

    // Start succs
    when(cfg.getSuccs(start)).thenReturn(createSingleSet(w2));

    /**
     * W2
     */
    // W2 preds
    when(cfg.getPreds(w2)).thenReturn(createSingleSet(s1));

    // W2 succs
    when(cfg.getSuccs(w2)).thenReturn(createDoubleSet(s3, b0));

    /**
     * S3
     */
    // S3 preds
    when(cfg.getPreds(s3)).thenReturn(createSingleSet(w2));

    // S3 succs
    when(cfg.getSuccs(s3)).thenReturn(createSingleSet(w2));

    /**
     * End (b0)
     */
    when(cfg.getEnd()).thenReturn(b0);
    Statement end = cfg.getEnd();

    // End preds
    when(cfg.getPreds(end)).thenReturn(createSingleSet(w2));

    // End succs
    when(cfg.getSuccs(end)).thenReturn(null);

    /**
     * Testing
     */
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(cfg);

    // Start (s1)
    Set<ReachingDefinitions.Definition> startDefinitions = reachingDefinitions.getReachingDefinitions(start);
    assertEquals(0, startDefinitions.size());
    // W2
    Set<ReachingDefinitions.Definition> w2Definitions = reachingDefinitions.getReachingDefinitions(w2);
    assertAll("'x' and 'z' defined at w2",
            () -> assertEquals(2, w2Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", w2Definitions)),
            () -> assertTrue(doesDefineStatement("z", w2Definitions))
    );
    // S3
    Set<ReachingDefinitions.Definition> s3Definitions = reachingDefinitions.getReachingDefinitions(s3);
    assertAll("'x' and 'z' defined at s3",
            () -> assertEquals(2, s3Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", s3Definitions)),
            () -> assertTrue(doesDefineStatement("z", s3Definitions))
    );
    // End (b0)
    Set<ReachingDefinitions.Definition> endDefinitions = reachingDefinitions.getReachingDefinitions(end);
    assertAll("'x' and 'z' defined at end",
            () -> assertEquals(2, endDefinitions.size()),
            () -> assertTrue(doesDefineStatement("x", endDefinitions)),
            () -> assertTrue(doesDefineStatement("z", endDefinitions))
    );
  }
}
