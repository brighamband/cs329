package edu.byu.cs329.hw0;

public class CharacterNode extends ListNode {
  
  private char value;

  public CharacterNode(char value) {
    this.value = value;
  }

  public CharacterNode(char value, ListNode next) {
    this.value = value;
    this.next = next;
  }

  public char getCharacter() {
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
