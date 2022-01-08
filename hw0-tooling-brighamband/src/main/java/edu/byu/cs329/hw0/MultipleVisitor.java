package edu.byu.cs329.hw0;

public class MultipleVisitor extends ListNodeVisitor {

  private int trueCounter;
  private String value;
  private int sum;

  public MultipleVisitor() {
    trueCounter = 0;
    value = "";
    sum = 0;
  }

  public int getCount() {
    return trueCounter;
  }

  public String getResult() {
    return value;
  }

  public int getTotal() {
    return sum;
  }

  @Override
  public boolean visit(NumberNode n) {
    sum += n.getNumber();
    return true;
  }

  @Override
  public boolean visit(CharacterNode n) {
    value += n.getCharacter();
    return true;
  }

  @Override
  public boolean visit(BooleanNode n) {
    boolean keepTraversing = true;
    if (n.getBoolean()) {
      trueCounter++;
    } else {
      keepTraversing = false; // Stop traversal at first seen false
    }
    return keepTraversing;
  }

  @Override
  public void endVisit(NumberNode n) {
    // Do nothing
  }

  @Override
  public void endVisit(CharacterNode n) {
    value += n.getCharacter();
  }

  @Override
  public void endVisit(BooleanNode n) {
    // Do nothing
  }
  
}
