package edu.byu.cs329.hw0;

public class ListNodeVisitor {

  public boolean visit(NumberNode n) {
    return true;
  }

  public boolean visit(CharacterNode n) {
    return true;
  }
  
  public boolean visit(BooleanNode n) {
    return true;
  }

  public void endVisit(NumberNode n) {
    // No default implementation
  }

  public void endVisit(CharacterNode n) {
    // No default implementation
  }

  public void endVisit(BooleanNode n) {
    // No default implementation
  }
}
