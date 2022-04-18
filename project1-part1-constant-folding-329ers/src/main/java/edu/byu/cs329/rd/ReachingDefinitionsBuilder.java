package edu.byu.cs329.rd;

import edu.byu.cs329.cfg.ControlFlowGraph;
import edu.byu.cs329.rd.ReachingDefinitions.Definition;
import edu.byu.cs329.utils.AstNodePropertiesUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;


public class ReachingDefinitionsBuilder {
  private List<ReachingDefinitions> rdList = null;
  private Map<Statement, Set<Definition>> genSetMap = null;
  private Map<Statement, Set<Definition>> entrySetMap = null;
  private Map<Statement, Set<Definition>> exitSetMap = null;

  /**
   * Computes the reaching definitions for each control flow graph.
   *
   * @param cfgList the list of control flow graphs.
   * @return the corresponding reaching definitions for each graph.
   */
  public List<ReachingDefinitions> build(List<ControlFlowGraph> cfgList) {
    rdList = new ArrayList<ReachingDefinitions>();
    for (ControlFlowGraph cfg : cfgList) {
      ReachingDefinitions rd = computeReachingDefinitions(cfg);
      rdList.add(rd);
    }
    return rdList;
  }

  private ReachingDefinitions computeReachingDefinitions(ControlFlowGraph cfg) {
    genSetMap = new HashMap<Statement, Set<Definition>>();      // Kill set is same but wild cards
    entrySetMap = new HashMap<Statement, Set<Definition>>();
    exitSetMap = new HashMap<Statement, Set<Definition>>();

    // Step 1: Initialization
    initMaps(cfg);
    // Init param definitions
    Set<Definition> parameterDefinitions = createParameterDefinitions(cfg.getMethodDeclaration());
    entrySetMap.put(cfg.getStart(), parameterDefinitions);

    // Step 2: Iteration
    runWorklistAlgorithm(cfg);

    return new ReachingDefinitions() {
      final Map<Statement, Set<Definition>> reachingDefinitions
              = Collections.unmodifiableMap(entrySetMap);

      @Override
      public Set<Definition> getReachingDefinitions(final Statement s) {
        Set<Definition> returnValue = null;
        if (reachingDefinitions.containsKey(s)) {
          returnValue = reachingDefinitions.get(s);
        }
        return returnValue;
      }
    };
  }

  private void initMaps(ControlFlowGraph cfg) {
    // Init stack
    Stack<Statement> stack = new Stack<>();
    List<Statement> visited = new ArrayList<>();

    // Add start to stack
    Statement start = cfg.getStart();
    stack.add(start);

    // Loop while queue has items
    while (!stack.isEmpty()) {
      Statement current = stack.pop();
      visited.add(current);
      if (entrySetMap.containsKey(cfg.getSuccs(current))) {
        continue;
      }
      Set<Statement> succs = cfg.getSuccs(current);
      if (succs != null) {
        for (Statement statement : succs) {
          if (!visited.contains(statement)) {
            stack.add(statement);
          }
        }
        // stack.addAll(succs);
      }
      initNullEntryAndExitSets(current);
      initGenStatement(current, genSetMap);
    }
  }

  private void initNullEntryAndExitSets(Statement statement) {
    Set<Definition> definitions = new HashSet<>();
    Definition definition = createDefinition(null, null);
    definitions.add(definition);
    entrySetMap.put(statement, definitions);
    exitSetMap.put(statement, definitions);
  }

  private void initGenStatement(Statement statement, Map<Statement, Set<Definition>> genSetMap) {
    Set<Definition> definitions = new HashSet<>();

    // If VariableDeclaration
    if (statement instanceof VariableDeclarationStatement) {
      SimpleName name = AstNodePropertiesUtils
              .getSimpleName(((VariableDeclarationStatement) statement));
      Definition definition = createDefinition(name, statement);
      definitions.add(definition);
    } else if (statement instanceof ExpressionStatement  // If ExpressionStatement with Assignment
            && (((ExpressionStatement) statement).getExpression() instanceof Assignment)) {
      Expression exp = ((Assignment) ((ExpressionStatement) statement)
              .getExpression()).getLeftHandSide();
      SimpleName name = (SimpleName) exp;
      Definition definition = createDefinition(name, statement);
      definitions.add(definition);
    }

    // Put into genSet
    genSetMap.put(statement, definitions);
  }

  private void runWorklistAlgorithm(ControlFlowGraph cfg) {
    // Init worklist stack
    Stack<Statement> worklist = new Stack<>();

    // Add start to worklist
    Statement start = cfg.getStart();
    worklist.add(start);

    // Loop while we still have basic blocks on the worklist
    while (!worklist.isEmpty()) {
      // Make copies beforehand, so we can see how it has changed later
      final Map<Statement, Set<Definition>> oldEntrySetMap = cloneMap(entrySetMap);

      // Pop the top basic block off the worklist
      Statement poppedStatement = worklist.pop();

      if (poppedStatement instanceof Block) {
        continue;
      }

      // Compute the current exit set
      computeExitSet(poppedStatement, cfg.getPreds(poppedStatement));


      // Compute the next statement's entry set
      computeNextEntrySet(poppedStatement, cfg.getSuccs(poppedStatement));

      // If the entry set changed, then push all successors to the worklist
      if (!(entrySetMap.equals(oldEntrySetMap))) {
        worklist.addAll(cfg.getSuccs(poppedStatement));
      }
    }
  }

  private void computeNextEntrySet(Statement poppedStatement, Set<Statement> succs) {
    for (Statement statement : succs) {
      if (entrySetMap.get(statement).isEmpty()) {
        continue;
      }
      Set<Definition> definitions = new HashSet<>();
      definitions.addAll(exitSetMap.get(poppedStatement));
      entrySetMap.put(statement, definitions);
    }
  }

  private Map<Statement, Set<Definition>> cloneMap(Map<Statement, Set<Definition>> mapToClone) {
    Map<Statement, Set<Definition>> newClone = new HashMap<>();
    newClone.putAll(mapToClone);
    return newClone;
  }

  private void computeExitSet(Statement poppedStatement, Set<Statement> nodes) {
    Set<Definition> definitions = new HashSet<>();
    definitions.addAll(entrySetMap.get(poppedStatement));

    for (Definition entryDef : entrySetMap.get(poppedStatement)) {
      for (Definition genDef: genSetMap.get(poppedStatement)) {
        if (entryDef.name.getIdentifier().equals(genDef.name.getIdentifier())) {
          definitions.remove(entryDef);
        }
      }
    }
    definitions.addAll(genSetMap.get(poppedStatement));

    exitSetMap.put(poppedStatement, definitions);
  }


  private Set<Definition> createParameterDefinitions(MethodDeclaration methodDeclaration) {
    List<VariableDeclaration> parameterList = getParameterList(methodDeclaration.parameters());
    Set<Definition> set = new HashSet<Definition>();

    for (VariableDeclaration parameter : parameterList) {
      Definition definition = createDefinition(parameter.getName(), null);
      set.add(definition);
    }

    return set;
  }

  private Definition createDefinition(SimpleName name, Statement statement) {
    Definition definition = new Definition();
    definition.name = name;
    definition.statement = statement;
    return definition;
  }

  private List<VariableDeclaration> getParameterList(Object list) {
    @SuppressWarnings("unchecked")
    List<VariableDeclaration> statementList = (List<VariableDeclaration>) (list);
    return statementList;
  }
}
