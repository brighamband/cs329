package edu.byu.cs329.hw1;

/**
 * Child class of ListNodeVisitor that keeps track of sum
 *
 * @author Brigham Andersen
 */
public class SumVisitor extends ListNodeVisitor {

  /**
   * Private variable which keeps track of sum.
   */
  private int sum;

  /**
   * Public default constructor.
   */
  public SumVisitor() {
    sum = 0;
  }

  /**
   * Getter method that grabs the total
   * 
   * @return the total/sum
   */
  public int getTotal() {
    return sum;
  }

  /**
   * Function which overrides ListNodeVisitor to visit nodes.
   *
   * @param n NumberNode you are visiting
   * @return boolean indicating success/failure of visit
   */
  @Override
  public boolean visit(NumberNode n) {
    sum += n.getNumber();
    return true;
  }  

  /**
   * Function which ends a visit to a node.
   *
   * @param n NumberNode you are visiting
   */
  @Override
  public void endVisit(NumberNode n) {
    // Do nothing
  }

}
