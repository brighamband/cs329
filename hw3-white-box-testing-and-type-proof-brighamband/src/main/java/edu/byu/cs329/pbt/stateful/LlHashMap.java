package edu.byu.cs329.pbt.stateful;

import java.util.Arrays;

/**
 * Linked-List implementation of a hash-map.
 */
public class LlHashMap {

  private class HashEntry {
    public final int key;
    public int value;
    public HashEntry next;

    public HashEntry(int key, int value, HashEntry next) {
      this.key = key;
      this.value = value;
      this.next = next;
    }

    public String toString() {
      return "(" + key + ", " + value + ") :: " + (next != null ? next.toString() : "null");
    }

    private boolean contains(int key, int value) {
      if (this.key == key && this.value == value) {
        return true;
      }
      if (this.next == null) {
        return false;
      }
      return this.next.contains(key, value);
    }

    private boolean containedBy(HashEntry other) {
      if (other == null) {
        return false;
      }

      if (!other.contains(key, value)) {
        return false;
      }

      if (this.next != null) {
        return this.next.containedBy(other);
      }

      return true;
    }

    public boolean equals(Object o) {
      if (!(o instanceof HashEntry)) {
        return false;
      }
      HashEntry other = (HashEntry) o;

      return this.containedBy(other) && other.containedBy(this);
    }
  }

  private HashEntry[] buffer;

  private int nrBuckets;

  /**
   * Constructor.
   *
   * @param nrBuckets Number of buckets to create
   * @throws IllegalArgumentException thrown if buckets not more than one
   */
  public LlHashMap(int nrBuckets) throws IllegalArgumentException {
    if (nrBuckets < 1) {
      throw new IllegalArgumentException();
    }
    this.nrBuckets = nrBuckets;
    this.buffer = new HashEntry[nrBuckets];
  }

  public void put(int key, int value) {
    int index = hashOf(key);
    this.buffer[index] = new HashEntry(key, value, this.buffer[index]);
  }

  private HashEntry find(int key) {
    int index = hashOf(key);
    HashEntry el = this.buffer[index];
    while (el != null && el.key != key) {
      el = el.next;
    }
    return el;
  }

  public boolean contains(int key) {
    return find(key) != null;
  }

  /**
   * Gets the value for a key from the table if it exists.
   *
   * @param key the key to search for
   * @return (out: Integer) the value of the key, or null if not found.
   */
  public Integer get(int key) {
    HashEntry found = find(key);
    if (found == null) {
      return null;
    } else {
      return found.value;
      // Defect -- was returning the key (found.key) instead of the value (found.value)
    }
  }

  /**
   * Takes a key out of the table.
   *
   * @param key the key to remove
   */
  public void remove(int key) {
    int index = hashOf(key);
    HashEntry el = this.buffer[index];
    // Defect -- wouldn't remove the head properly
    if (el != null && el.key == key) { // If key is at head
      this.buffer[index] = el.next;
    }
    while (el != null) {
      if (el.next != null && el.next.key == key) {
        el.next = el.next.next;
      }
      el = el.next;
    }
  }

  public String toString() {
    return "{ nrBuckets: " + this.nrBuckets + ", buffer: " + Arrays.toString(this.buffer) + "}";
  }

  /**
   * Compares two objects.
   */
  public boolean equals(Object o) {
    if (!(o instanceof LlHashMap)) {
      return false;
    }
    LlHashMap other = (LlHashMap) o;

    if (other.nrBuckets != this.nrBuckets || this.buffer.length != other.buffer.length) {
      return false;
    }

    return Arrays.equals(this.buffer, other.buffer);
  }

  private int hashOf(int key) {
    return Math.abs(key) % this.nrBuckets;
  }
}
