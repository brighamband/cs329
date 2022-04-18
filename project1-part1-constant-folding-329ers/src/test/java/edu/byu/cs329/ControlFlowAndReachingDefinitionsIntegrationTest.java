package edu.byu.cs329;

import edu.byu.cs329.cfg.ControlFlowGraph;
import edu.byu.cs329.cfg.ControlFlowGraphBuilder;
import edu.byu.cs329.cfg.StatementTracker;
import edu.byu.cs329.rd.ReachingDefinitions;
import edu.byu.cs329.rd.ReachingDefinitions.Definition;
import edu.byu.cs329.rd.ReachingDefinitionsBuilder;
import java.util.ArrayList;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Integration test class for control flow and reaching definitions")
public class ControlFlowAndReachingDefinitionsIntegrationTest {
  ControlFlowGraphBuilder cfgBuilder = null;
  ControlFlowGraph cfg = null;
  StatementTracker statementTracker = null;
  ReachingDefinitionsBuilder reachingDefsBuilder = null;

  @BeforeEach
  void beforeEach() {
    cfgBuilder = new ControlFlowGraphBuilder();
    reachingDefsBuilder = new ReachingDefinitionsBuilder();
  }

  void init(String fileName) {
    ASTNode node = TestUtils.getASTNodeFor(this, fileName);
    List<ControlFlowGraph> cfgList = cfgBuilder.build(node);
    assertEquals(1, cfgList.size());
    cfg = cfgList.get(0);
    statementTracker = new StatementTracker(node);
  }

  // CFG Helper

  private boolean hasEdge(Statement source, Statement dest) {
    Set<Statement> successors = cfg.getSuccs(source);
    Set<Statement> predecessors = cfg.getPreds(dest);
    return successors != null && successors.contains(dest)
            && predecessors != null && predecessors.contains(source);
  }

  // RD Helpers

  private ReachingDefinitions getReachingDefinitions(ControlFlowGraph controlFlowGraph) {
    List<ControlFlowGraph> list = new ArrayList<ControlFlowGraph>();
    list.add(controlFlowGraph);
    List<ReachingDefinitions> reachingDefinitionsList = reachingDefsBuilder.build(list);
    assertEquals(1, reachingDefinitionsList.size());
    return reachingDefinitionsList.get(0);
  }

  private boolean doesDefineStatement(String name, final Set<Definition> definitions) {
    for (Definition definition : definitions) {
      if (definition.name.getIdentifier().equals(name) && definition.statement != null) {
        return true;
      }
    }
    return false;
  }

  @Test
  @Tag("IntegrationTest")
  @DisplayName("Integration test for control flow and reaching definitions")
  void should_BuildControlFlowAndReachingDefinitions_when_Combined() {
    String fileName = "cfgInputs/CFGIntegration.java";
    init(fileName);

    // Test Control Flow Builder
    assertNotNull(cfg.getMethodDeclaration());
    Statement s1 = statementTracker.getVariableDeclarationStatement(0);
    Statement i2 = statementTracker.getIfStatement(0);
    Statement w3 = statementTracker.getWhileStatement(0);
    Statement s4 = statementTracker.getExpressionStatement(0);
    Statement s5 = statementTracker.getExpressionStatement(1);
    Statement r6 = statementTracker.getReturnStatement(0);
    // Test edges all connect
    assertTrue(hasEdge(s1, i2));
    assertTrue(hasEdge(i2, w3));
    assertTrue(hasEdge(w3, s4));
    assertTrue(hasEdge(s4, w3));
    assertTrue(hasEdge(i2, s5));
    assertTrue(hasEdge(s5, r6));
    assertTrue(hasEdge(w3, r6));

    // Test Reaching Definitions
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(cfg);

    // Assert s1 is empty
    s1 = cfg.getStart();
    Set<Definition> s1Definitions = reachingDefinitions.getReachingDefinitions(s1);
    assertEquals(0, s1Definitions.size());

    // Assert i2 has x
    i2 = cfg.getSuccs(s1).iterator().next();
    Set<Definition> i2Definitions = reachingDefinitions.getReachingDefinitions(i2);
    assertAll("Assert i2 defines x",
            () -> assertEquals(1, i2Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", i2Definitions))
    );

    // Assert w3 has (x, s1),(x, s4)
    Set<Definition> w3Definitions = reachingDefinitions.getReachingDefinitions(w3);
    assertAll("Assert w3 has (x, s1), (x, s4)",
            () -> assertEquals(2, w3Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", w3Definitions))
    );

    // Assert s4 has (x, s1), (x, s4)
    Set<Definition> s4Definitions = reachingDefinitions.getReachingDefinitions(s4);
    assertAll("Assert that s4 has (x, s1), (x, s4)",
            () -> assertEquals(2, s4Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", s4Definitions))
    );

    // Assert s5 has (x, s1)
    Set<Definition> s5Definitions = reachingDefinitions.getReachingDefinitions(s5);
    assertAll("Assert s5 has (x, s1)",
            () -> assertEquals(1, s5Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", s5Definitions))
    );

    // Assert r6 has (x,s1), (x, s4), (x, s5)
    r6 = cfg.getSuccs(s5).iterator().next();
    Set<Definition> r6Definitions = reachingDefinitions.getReachingDefinitions(r6);
    assertAll("Assert r6 has (x,s1), (x, s4), (x, s5)",
            () -> assertEquals(3, r6Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", r6Definitions))
    );


//        assertAll("Method declaration with empty block",
//                () -> assertNotNull(controlFlowGraph.getMethodDeclaration()),
//                () -> assertEquals(controlFlowGraph.getStart(), controlFlowGraph.getEnd())
//        );
  }
}

// Write an interesting system level test that uses the ControlFlowBuilder
// to generate a ControlFlowGraph instance for input to the code that builds a ReachingDefinitions instance.