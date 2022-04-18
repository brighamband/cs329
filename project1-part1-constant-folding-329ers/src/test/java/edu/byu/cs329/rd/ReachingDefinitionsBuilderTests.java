package edu.byu.cs329.rd;

import edu.byu.cs329.TestUtils;
import edu.byu.cs329.cfg.ControlFlowGraph;
import edu.byu.cs329.cfg.ControlFlowGraphBuilder;
import edu.byu.cs329.rd.ReachingDefinitions.Definition;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests for ReachingDefinitionsBuilder")
public class ReachingDefinitionsBuilderTests {

  ReachingDefinitionsBuilder unitUnderTest = null;
  ControlFlowGraph controlFlowGraph;
  ControlFlowGraphBuilder controlFlowGraphBuilder;


  @BeforeEach
  void beforeEach() {
    unitUnderTest = new ReachingDefinitionsBuilder();
    controlFlowGraphBuilder = new ControlFlowGraphBuilder();
  }

  void init(String fileName) {
    ASTNode node = TestUtils.getASTNodeFor(this, fileName);
    List<ControlFlowGraph> cfgList = controlFlowGraphBuilder.build(node);
    assertEquals(1, cfgList.size());
    controlFlowGraph = cfgList.get(0);
  }

  private boolean doesDefineParam(String name, final Set<Definition> definitions) {
    for (Definition definition : definitions) {
      if (definition.name.getIdentifier().equals(name) && definition.statement == null) {
        return true;
      }
    }
    return false;
  }

  private ReachingDefinitions getReachingDefinitions(ControlFlowGraph controlFlowGraph) {
    List<ControlFlowGraph> list = new ArrayList<>();
    list.add(controlFlowGraph);
    List<ReachingDefinitions> reachingDefinitionsList = unitUnderTest.build(list);
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
  @Tag("Parameters")
  @DisplayName("Should have a definition for each parameter at start when the method declaration has parameters.")
  void should_HaveDefinitionForEachParameterAtStart_when_MethodDeclarationHasParameters() {
    ControlFlowGraph controlFlowGraph = MockUtils.newMockForEmptyMethodWithTwoParameters("a", "b");
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(controlFlowGraph);
    Statement start = controlFlowGraph.getStart();
    Set<Definition> paramDefinitions = reachingDefinitions.getReachingDefinitions(start);
    assertEquals(2, paramDefinitions.size());
    assertAll("Parameters Defined at Start",
            () -> assertTrue(doesDefineParam("a", paramDefinitions)),
            () -> assertTrue(doesDefineParam("b", paramDefinitions))
    );
  }

  @Test
  @Tag("Sequential")
  @DisplayName("Should have predecessor definitions when sequential shape.")
  void should_HaveDefinitions_when_SequentialShape() {
    String fileName = "rdInputs/SequentialRD.java";
    init(fileName);
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(controlFlowGraph);

    // Start -- no definitions
    Statement start = controlFlowGraph.getStart();
    Set<Definition> startDefinitions = reachingDefinitions.getReachingDefinitions(start);
    assertEquals(0, startDefinitions.size());

    // End
    Statement end = controlFlowGraph.getEnd();
    Set<Definition> endDefinitions = reachingDefinitions.getReachingDefinitions(end);
    assertAll("C and D defined at end",
            () -> assertEquals(2, endDefinitions.size()),
            () -> assertTrue(doesDefineStatement("c", endDefinitions)),
            () -> assertTrue(doesDefineStatement("d", endDefinitions))
    );
  }

  @Test
  @Tag("Sequential2")
  @DisplayName("Should have one reaching definition for the return")
  void should_HaveDefinitions_when_SequentialShape2() {
    String fileName = "rdInputs/Sequential2RD.java";
    init(fileName);
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(controlFlowGraph);

    // Start -- no definitions
    Statement start = controlFlowGraph.getStart();
    Set<Definition> startDefinitions = reachingDefinitions.getReachingDefinitions(start);
    assertEquals(0, startDefinitions.size());

    // End
    Statement end = controlFlowGraph.getEnd();
    Set<Definition> endDefinitions = reachingDefinitions.getReachingDefinitions(end);
    assertAll("C and D defined at end",
            () -> assertEquals(1, endDefinitions.size()),
            () -> assertTrue(doesDefineStatement("c", endDefinitions))
    );
  }

  @Test
  @Tag("Branching")
  @DisplayName("Should have a definition for predecessor in each branch.")
  void should_HaveDefinitions_when_BranchingShape() {
    String fileName = "rdInputs/BranchingRD.java";
    init(fileName);
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(controlFlowGraph);

    Statement end = controlFlowGraph.getEnd();
    Set<Statement> ifChildStatements = controlFlowGraph.getPreds(end);

    // S3 - First child
    Statement s3 = ifChildStatements.iterator().next();
    Set<Definition> s3Definitions = reachingDefinitions.getReachingDefinitions(s3);
    assertAll("Assert child contains a",
            () -> assertEquals(1, s3Definitions.size()),
            () -> assertTrue(doesDefineStatement("a", s3Definitions))
    );


    // S4 - Second child
    Statement s4 = ifChildStatements.iterator().next();
    Set<Definition> s4Definitions = reachingDefinitions.getReachingDefinitions(s4);
    assertAll("Assert child contains a",
            () -> assertEquals(1, s4Definitions.size()),
            () -> assertTrue(doesDefineStatement("a", s4Definitions))
    );
  }

  @Test
  @Tag("Merging")
  @DisplayName("Should have updated definitions of branched statements after merge.")
  void should_HaveDefinitions_when_MergingShape() {
    String fileName = "rdInputs/MergingRD.java";
    init(fileName);
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(controlFlowGraph);

    // R6
    Statement end = controlFlowGraph.getEnd();
    Statement r6 = controlFlowGraph.getPreds(end).iterator().next();
    Set<Definition> r6Definitions = reachingDefinitions.getReachingDefinitions(r6);

    assertAll("Assert return contains both instances of x and y",
            () -> assertEquals(2, r6Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", r6Definitions)),
            () -> assertTrue(doesDefineStatement("y", r6Definitions))
    );
  }

  @Test
  @Tag("Looping")
  @DisplayName("Should have correct predecessor definitions in a loop.")
  void should_HaveDefinitions_when_LoopingShape() {
    String fileName = "rdInputs/LoopingRD.java";
    init(fileName);
    ReachingDefinitions reachingDefinitions = getReachingDefinitions(controlFlowGraph);

    // W2
    Statement w2 = controlFlowGraph.getSuccs(controlFlowGraph.getStart()).iterator().next();
    Set<Definition> w2Definitions = reachingDefinitions.getReachingDefinitions(w2);

    // S3
    Statement s3 = controlFlowGraph.getSuccs(w2).iterator().next();
    Set<Definition> s3Definitions = reachingDefinitions.getReachingDefinitions(s3);

    assertAll("Assert w2 has x and z",
            () -> assertEquals(2, w2Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", w2Definitions)),
            () -> assertTrue(doesDefineStatement("z", w2Definitions))
    );

    assertAll("Assert s3 defines z",
            () -> assertEquals(2, s3Definitions.size()),
            () -> assertTrue(doesDefineStatement("x", w2Definitions)),
            () -> assertTrue(doesDefineStatement("z", s3Definitions))
    );
  }
}
