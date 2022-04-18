package edu.byu.cs329.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaSourceUtils {
  static final Logger log = LoggerFactory.getLogger(JavaSourceUtils.class);

  /**
   * Get the URI for a file from the class path.
   * 
   * @param t non-null object to get the class loader.
   * @param fileName the file to find on the class path.
   * @return URI to the file.
   */
  public static URI getUri(final Object t, final String fileName) {
    final URL url = t.getClass().getClassLoader().getResource(fileName);
    Objects.requireNonNull(url, "\'" + fileName + "\'" + " not found in classpath");
    URI uri = null;
    try {
      uri = url.toURI();
    } catch (final URISyntaxException e) {
      log.error("Failed to get URI for " + fileName);
      e.printStackTrace();
    }
    return uri;
  }

  /**
   * Get the ASTNode for program in the file.
   * 
   * @param file URI to the file.
   * @return ASTNode for the CompilationUnit in the file.
   */
  public static ASTNode getCompilationUnit(final URI file) {
    String inputFileAsString = readFile(file);
    ASTNode node = parse(inputFileAsString);
    return node;
  }

  /**
   * Get the ASTNode for the program in the file.
   * 
   * @param t reference to calling object.
   * @param name name of file.
   * @return AST node for the CompilationUnit in the file
   */
  public static ASTNode getAstNodeFor(final Object t, String name) {
    URI uri = getUri(t, name);
    ExceptionUtils.requiresNonNull(uri, "failed to find " + name + "in the class path");
    ASTNode root = getCompilationUnit(uri);
    return root;
  }

  private static String readFile(final URI path) {
    try {
      return String.join("\n", Files.readAllLines(Paths.get(path)));
    } catch (IOException ioe) {
      log.error("File not readable " + ioe.getMessage());
    }
    return "";
  }

  private static ASTNode parse(final String sourceString) {
    ASTParser parser = ASTParser.newParser(AST.JLS3);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setSource(sourceString.toCharArray());
    Map<?, ?> options = JavaCore.getOptions();
    JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
    parser.setCompilerOptions(options);
    return parser.createAST(null);
  }
}
