package edu.byu.cs329.hw0;

public class NumberNode extends ListNode {

  private int value;

  public NumberNode(int value) {
    this.value = value;
  }

  public NumberNode(int value, ListNode next) {
    this.value = value;
    this.next = next;
  }

  public int getNumber() {
    return value;
  }

  public void accept(ListNodeVisitor v) {

    Boolean doTraverseNext = v.visit(this);

    if (doTraverseNext) {
      if (next != null) {
        next.accept(v);
      }
    }

    v.endVisit(this);
  }
  
}

