package edu.byu.cs329.hw0;

/**
 * Parent class off which BooleanNode, CharacterNode, and NumberNode are based.
 *
 * @author Brigham Andersen
 */
public abstract class ListNode {

  protected ListNode next;
  
  /** 
   * Public constructor (empty).
  */
  public ListNode() {
    this.next = null;
  }

  /** 
   * Public constructor (parameterized).
   *
   * @param next node to use for initialization
  */
  public ListNode(ListNode next) {
    this.next = next;
  }
    
  /**
   * Function accessible to other Node classes so that they can
   * traverse ListNode-type data structures.
   *
   * @param v visitor object used for traversal
   */
  public abstract void accept(ListNodeVisitor v);
    
}
