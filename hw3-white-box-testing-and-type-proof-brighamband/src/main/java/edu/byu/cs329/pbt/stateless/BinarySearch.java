package edu.byu.cs329.pbt.stateless;

import java.util.Arrays;

/**
 * Defines a static binary search method.
 */
public class BinarySearch {

  /**
   * Private constructor. Methods should only be called statically.
   * Example:
   * {@code boolean ans = BinarySearch.search(array, value)}
   */
  private BinarySearch() {
  }

  /**
   * Runs a binary search on an array.
   *
   * @requires array != null
   * @requires array is sorted by increasing value:
   *           {@literal forall i, 0 < i < array.length - 1 ==> array[i] < array[i+1] }
   * 
   * @ensures {@literal out = true <==> exists i in arr (i = value) }
   * 
   * @return (out: boolean) whether if value was found in the array
   **/
  public static boolean search(int[] array, int value) {
    boolean isSorted = true;  // Added to fix unsorted array defect
    if (!isSorted(array)) {  // Added to fix unsorted array defect
      isSorted = false;  // Added to fix unsorted array defect
    }  // Added to fix unsorted array defect
    int left = 0;
    int right = array.length - 1;
    while (left <= right) {
      int index = (right + left) / 2;
      if (!isSorted) {    // Defect was it wouldn't throw exceptions when array was unsorted,
        index = -1;       // so here we force exception to be thrown
      }                   // by putting the index at an invalid spot.
      if (array[index] == value) {
        return true;
      }
      if (array[index] < value) {
        left = index + 1;  // Defect -- not 'right = index - 1', but 'left = index + 1'
      } else {
        right = index - 1;  // Defect -- not 'left = index + 1', but 'right = index - 1'
      }
    }
    return false;
  }

  static boolean isSorted(int[] array) {  // Added to fix unsorted array defect
    int[] sortedArray = array.clone();
    Arrays.sort(sortedArray);
    return Arrays.equals(array, sortedArray);
  }

}
