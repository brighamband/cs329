package edu.byu.cs329.constantfolding;

import edu.byu.cs329.utils.JavaSourceUtils;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements constant folding.
 *
 * @author James Wasson
 * @author Eric Mercer
 */
public class ConstantFolding {

  static final Logger log = LoggerFactory.getLogger(ConstantFolding.class);

  /**
   * Performs constant folding.
   *
   * @param compilationUnit ASTNode for the compilation unit.
   * @return boolean indicating if the AST folded.
   */
  public static boolean fold(ASTNode compilationUnit) {
    boolean didChangeAtAll = false;
    boolean isChanged = true;
    List<Folding> foldingList = List.of(
            new BlockFolding(),
            new ParenthesizedExpressionFolding(),
            new PrefixNotBoolFolding(),
            new NumericPlusInfixFolding(),
            new BinaryRelationFolding(),
            new IfBoolFolding());

    while (isChanged) {
      isChanged = false;
      for (Folding folding : foldingList) {
        isChanged = isChanged || folding.fold(compilationUnit);

        if (isChanged) {
          didChangeAtAll = true;
        }
      }
    }

    return didChangeAtAll;
  }

  /**
   * Performs constant folding on a Java file.
   *
   * @param args The args[0] is the file to fold and the args[1] is where to write
   *             the output
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      log.error("Missing Java input file or output file or both on command line");
      System.out.println("usage: java ConstantFolding <input file> <output file>");
      System.exit(1);
    }

    File inputFile = new File(args[0]);
    ASTNode compilationUnit = JavaSourceUtils.getCompilationUnit(inputFile.toURI());
    fold(compilationUnit);

    try {
      PrintWriter writer = new PrintWriter(args[1], "UTF-8");
      writer.print(compilationUnit.toString());
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
