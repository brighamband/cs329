package misc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testing ff method using JUnit 5")
public class FTests {

  private Queue queue;
  private final int MAX_SIZE = 2;
  private final int X_VAL = 10;

  @BeforeEach
  public void setup() {
    queue = new Queue(MAX_SIZE);
  }

  @Test
  @DisplayName("Tests Queue constructor based on specifications")
  void testConstructor() {
    // Test edge case of a negative input
    int INVALID_INPUT = -10;
    assertThrows(NegativeArraySizeException.class, () -> new Queue(INVALID_INPUT));

    assertTrue(queue.arr.length > 0);
    // Ensure queue is empty
    for (Object item : queue.arr) {
      assertNull(item);
    }
    // Make sure that the queue gets initialized
    assertNotNull(queue.arr);
    assertEquals(0, queue.size);
    assertEquals(0, queue.first);
    assertEquals(0, queue.last);
    assertEquals(MAX_SIZE, queue.arr.length);
  }

  @Test
  @DisplayName("Tests Queue size() method based on specifications") 
  void testSizeMethod() {
    assertEquals(queue.size, queue.size());
    assertTrue(queue.size() >= 0);
  }

  @Test
  @DisplayName("Tests Queue max() method based on specifications") 
  void testMaxMethod() {
    assertEquals(queue.arr.length, queue.max());
    assertTrue(queue.max() >= 0);
  }

  @Test
  @DisplayName("Tests Queue enqueue() method based on specifications") 
  void testEnqueueMethod() {
    Integer x = X_VAL;
    int origSize = queue.size;
    int origLast = queue.last;
    queue.enqueue(x);
    
    assertEquals(x, queue.arr[queue.last - 1]);
    assertEquals(origSize + 1, queue.size);

    // Test last when there's space in queue
    assertEquals(origLast + 1, queue.last);

    // Test last when there isn't space in queue
    queue.enqueue(x);
    assertEquals(0, queue.last);
  }

  @Test
  @DisplayName("Tests Queue dequeue() method based on specifications") 
  void testDequeueMethod() {
    // Test returns null when empty
    assertEquals(null, queue.dequeue());

    Integer x = X_VAL;
    queue.enqueue(x);

    // Test returns x when queue has one or more items
    assertEquals(x, queue.dequeue());

    assertEquals(x, queue.arr[queue.first - 1]);
  }

  @Test
  @DisplayName("Tests that array has null element")
  void testArrayHasNullElement() {
    boolean hasNullElement = false;
    for (Object item : queue.arr) {
      if (item == null) {
        hasNullElement = true;
      }
    }
    assertTrue(hasNullElement);
  }

}
