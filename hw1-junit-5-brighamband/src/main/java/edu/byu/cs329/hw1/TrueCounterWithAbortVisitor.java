package edu.byu.cs329.hw1;

public class TrueCounterWithAbortVisitor extends ListNodeVisitor {

  private int trueCounter;

  public TrueCounterWithAbortVisitor() {
    trueCounter = 0;
  }

  public int getCount() {
    return trueCounter;
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
  public void endVisit(BooleanNode n) {
    // Do nothing
  }

}

















/*
  private int trueCounter;

  public TrueCounterWithAbortVisitor() {
    trueCounter = 0;
  }

  public int getCount() {
    return trueCounter;
  }

  @Override
  public boolean visit(BooleanNode n) {
    boolean keepTraversing = true;
    if (n.getBoolean()) {
      trueCounter++;
    }
    else {
      keepTraversing = false; // Stop traversal at first seen false
    }
    return keepTraversing;
  }

  @Override
  public void endVisit(BooleanNode n) {
    // Do nothing
  }
*/
