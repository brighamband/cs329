package edu.byu.cs329.utils;

import java.util.List;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AstNodePropertiesUtils {
  static final Logger log = LoggerFactory.getLogger(AstNodePropertiesUtils.class);

  /**
   * Get the string identifier of the class declaration.
   * 
   * @param classDeclaration TypeDeclaration object to get identifier from
   * @return string identifier
   */
  public static String getName(TypeDeclaration classDeclaration) {
    return getName(classDeclaration.getName());
  }

  /**
   * Get the string identifier of a field declaration.
   * 
   * @param field FieldDeclaration object to get identifier from
   * @return string identifier
   */
  public static String getName(FieldDeclaration field) {
    return getName(field.fragments());
  }

  /**
   * Get the string identifier of the method declaration.
   * 
   * @param method MethodDeclaration object to get identifier from
   * @return string identifier
   */
  public static String getName(MethodDeclaration method) {
    return getName(method.getName());
  }

  /**
   * Get the string identifier of the variable declaration.
   * 
   * @param declaration VariableDeclaration object to get identifier
   * @return string identifier
   */
  public static String getName(VariableDeclaration declaration) {
    return getName(declaration.getName());
  }

  /**
   * Get the string identifier of the variable declaration.
   * 
   * @param declaration VariableDeclarationStatement object to get identifier from
   * @return string identifier
   */
  public static String getName(VariableDeclarationStatement declaration) {
    return getName(declaration.fragments());
  }

  /**
   * Get the string identifier from the SimpleName.
   * 
   * @param name SimpleName object to get the identifier
   * @return string identifier
   */
  public static String getName(SimpleName name) {
    return name.getIdentifier();
  }

  private static String getName(List<?> fragments) {
    VariableDeclaration declaration = AstNodePropertiesUtils.getFragment(fragments);
    return getName(declaration.getName());
  }

  /**
   * Get the Expression object that initializes the first VariableDeclaration in a 
   * VariableDeclarationStatement.
   * 
   * @param declarationStatement VariableDeclarationStatement to get the initializer from
   * @return Expression which initializes a VariableDeclaration
   */
  public static Expression getInitializer(VariableDeclarationStatement declarationStatement) {
    VariableDeclaration declaration = getFragment(declarationStatement.fragments());
    return declaration.getInitializer();   
  }

  /**
   * Get the SimpleName from the first VariableDeclaration in a VariableDeclarationStatement.
   * 
   * @param declarationStatement VariableDeclarationStatement to get the SimpleName from
   * @return SimpleName of a VariableDeclaration
   */
  public static SimpleName getSimpleName(VariableDeclarationStatement declarationStatement) {
    VariableDeclaration declaration = getFragment(declarationStatement.fragments());
    return declaration.getName();   
  }
  
  private static VariableDeclaration getFragment(List<?> fragments) {
    if (fragments.size() > 1) {
      ExceptionUtils.throwRuntimeException("only one VariableDeclaration is allowed in fragments");
    }
    return (VariableDeclaration) fragments.get(0);
  }
}
