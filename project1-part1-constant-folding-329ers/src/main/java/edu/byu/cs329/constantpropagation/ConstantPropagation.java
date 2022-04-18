package edu.byu.cs329.constantpropagation;

import edu.byu.cs329.cfg.ControlFlowGraph;
import edu.byu.cs329.cfg.ControlFlowGraphBuilder;
import edu.byu.cs329.constantfolding.ConstantFolding;
import edu.byu.cs329.rd.ReachingDefinitions;
import edu.byu.cs329.rd.ReachingDefinitionsBuilder;
import edu.byu.cs329.utils.JavaSourceUtils;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Constant Propagation.
 *
 * @author Eric Mercer
 */
public class ConstantPropagation {

  static final Logger log = LoggerFactory.getLogger(ConstantPropagation.class);

  /**
   * Performs constant propagation.
   *
   * @param node the root node for constant propagation.
   */
  public static boolean propagate(ASTNode node) {
    // For each method, repeat until no changes
    boolean didChangeAtAll = false;
    boolean isChanged = true;

    while (isChanged) {
      // 1. Constant folding
      boolean didConstantFold = ConstantFolding.fold(node);

      // 2. Construct the control flow graph
      ControlFlowGraphBuilder cfgBuilder = new ControlFlowGraphBuilder();
      List<ControlFlowGraph> cfgList = cfgBuilder.build(node);

      // 3. Perform reaching definitions
      ReachingDefinitionsBuilder rdBuilder = new ReachingDefinitionsBuilder();
      List<ReachingDefinitions> rdList = rdBuilder.build(cfgList);

      // 4. Replace / propagate
      ConstantPropagator propagator = new ConstantPropagator(rdList);
      boolean didPropagateConstants = propagator.replace(node);

      if (didPropagateConstants) {
        didChangeAtAll = true;
      }

      isChanged = didConstantFold || didPropagateConstants;
    }

    return didChangeAtAll;
  }

  /**
   * Performs constant folding on a Java file.
   *
   * @param args args[0] is the file to fold and args[1] is where to write the
   *             output
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      log.error("Missing Java input file or output file on command line");
      System.out.println("usage: java DomViewer <java file to parse> <html file to write>");
      System.exit(1);
    }

    File inputFile = new File(args[0]);
    // String inputFileAsString = readFile(inputFile.toURI());
    ASTNode node = JavaSourceUtils
            .getCompilationUnit(inputFile.toURI());// parse(inputFileAsString);
    ConstantPropagation.propagate(node);

    try {
      PrintWriter writer = new PrintWriter(args[1], "UTF-8");
      writer.print(node.toString());
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
