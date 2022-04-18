package edu.byu.cs329.pbt.stateless;

import java.util.Random;

/**
 * Test suite for the MergeSort class.
 */
public class MergeSortTest {
  public static Random rand = new Random();

  // Add tests for MergeSort.sort() here

  /**
   * Counts the number of times x is in arr.
   * 
   * @param x   a value
   * @param arr an array
   * @return count of x in arr
   */
  static int count(int x, int[] arr) {
    int c = 0;
    for (int i : arr) {
      if (x == i) {
        ++c;
      }
    }
    return c;
  }
}
