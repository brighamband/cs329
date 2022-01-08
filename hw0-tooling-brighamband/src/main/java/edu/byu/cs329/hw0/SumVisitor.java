package edu.byu.cs329.hw0;

public class SumVisitor extends ListNodeVisitor {

  private int sum;

  public SumVisitor() {
    sum = 0;
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
  public void endVisit(NumberNode n) {
    // Do nothing
  }

}
