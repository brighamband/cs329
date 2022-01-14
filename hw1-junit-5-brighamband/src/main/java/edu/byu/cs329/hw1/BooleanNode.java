package edu.byu.cs329.hw1;

public class BooleanNode extends ListNode {
  
  private boolean value;

  public BooleanNode(boolean value) {
    this.value = value;
  }

  public BooleanNode(boolean value, ListNode next) {
    this.value = value;
    this.next = next;
  }

  public boolean getBoolean() {
    return value;
  }

  public void accept(ListNodeVisitor v) {
    boolean doTraverseNext = v.visit(this);

    if (doTraverseNext) {
      if (next != null) {
        next.accept(v);
      }
    }
    
    v.endVisit(this);
  }

}
