package misc;

/**
 * Queue of objects.
 */
public class Queue {
  Object[] arr;
  int size;
  int first;
  int last;

  /**
   * Creates a queue with a bounded size.
   * 
   * @requires max > 0
   * 
   * @ensures Q = fresh()
   * 
   * @param max the size of the queue
   */
  public Queue(int max) {
    arr = new Object[max];
    size = 0;
    first = 0;
    last = 0;
  }

  /**
   * Finds the size of the queue.
   *
   * @ensures return value = size(Q)
   * @ensures return value >= 0
   * 
   * @return the size of the queue
   */
  public int size() {
    return size;
  }

  /**
   * Finds the max array length.
   *
   * @return the max value in terms of the length of the array
   * 
   * @ensures return value >= 0
   * @ensures return value = max(Q)
   *          --- max length of Q
   */
  public int max() {
    return arr.length;
  }

  /**
   * Adds a new object into the queue.
   * 
   * @param x the object to add to the queue
   * 
   * @requires size(Q) < max()
   *           --- Q isn't already full
   * 
   * @ensures forall i (0 <= i < size(old(Q)) ==> Q[i] = old(Q)[i])
   * @ensures size(Q) = size(old(Q)) + 1
   * @ensures Q[last] = x
   */
  public void enqueue(Object x) {
    arr[last] = x;
    last++;
    if (last == arr.length) {
      last = 0;
    }
    size++;
  }

  /**
   * Removes an object from the queue.
   * 
   * @requires size(Q) > 0
   * 
   * @ensures forall i (0 <= i < size(Q) ==> Q[i] = old(Q)[i + 1])
   *          --- every element moves one
   * @ensures size(old(Q)) = 0 ==> return value == null
   * @ensures size(old(Q)) != 0 ==> return value == x
   * @ensures size(Q) = size(old(Q)) - 1
   * @ensures foreach exists element in old(Q) where old(Q)[first] != element ==>
   *          exists element in Q
   *          --- all other elements in queue stay
   * @ensures return value = old(Q)[first]
   * 
   * @return the removed object
   */
  public Object dequeue() {
    if (size == 0) {
      return null;
    }
    final Object x = arr[first];
    first++;
    if (first == arr.length) {
      first = 0;
    }
    size--;
    return x;
  }
}
