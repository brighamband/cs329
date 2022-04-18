package edu.byu.cs329.utils;

import java.util.List;
import java.util.Objects;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreeModificationUtils {
  static final Logger log = LoggerFactory.getLogger(TreeModificationUtils.class);

  /**
   * Replaces an existing child with a new child in the AST.
   * 
   * @param oldChild the old child.
   * @param newChild the new child.
   */
  public static void replaceChildInParent(ASTNode oldChild, ASTNode newChild) {
    Objects.requireNonNull(newChild);
    StructuralPropertyDescriptor location = getLocationInParent(oldChild);
    if (location.isChildProperty()) {
      oldChild.getParent().setStructuralProperty(location, newChild);
    } else if (location.isChildListProperty()) {
      @SuppressWarnings("unchecked")
      List<ASTNode> propertyListForLocation = 
          (List<ASTNode>)(oldChild.getParent().getStructuralProperty(location));
      propertyListForLocation.set(propertyListForLocation.indexOf(oldChild), newChild);
    } 
  }

  /**
   * Removes an existing child from its parent.
   * 
   * @param child the child to remove
   */
  public static void removeChildInParent(ASTNode child) {
    StructuralPropertyDescriptor location = getLocationInParent(child);
    if (location.isChildProperty()) {
      child.getParent().setStructuralProperty(location, null);
    } else if (location.isChildListProperty()) {
      @SuppressWarnings("unchecked")
      List<ASTNode> propertyListForLocation = 
          (List<ASTNode>)(child.getParent().getStructuralProperty(location));
      propertyListForLocation.remove(child);
    }
  }

  private static StructuralPropertyDescriptor getLocationInParent(ASTNode node) {
    StructuralPropertyDescriptor location = node.getLocationInParent();
    Objects.requireNonNull(location); 
    if (location.isChildProperty() || location.isChildListProperty()) {
      return location;
    }
    String msg = new String("Location \'" + location.toString() + "\' is not supported");
    RuntimeException exception = new UnsupportedOperationException(msg);
    log.error(msg, exception);
    throw exception;
  }
}
