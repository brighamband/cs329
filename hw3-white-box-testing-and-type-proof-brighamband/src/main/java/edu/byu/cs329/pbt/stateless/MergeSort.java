package edu.byu.cs329.pbt.stateless;

/**
 * Defines a sort method implemented using the merge-sort algorithm.
 */
public class MergeSort {

  /**
   * Private constructor. Methods should only be called statically.
   * Example:
   * {@code int[] sorted = MergeSort.sort(array)}
   */
  private MergeSort() {
  }

  /**
   * Method that sorts an array using merge sort.
   *
   * @requires array != null
   * 
   * @ensures a sorted permutation of array, rv, is returned:
   *          {@literal 
   *          rv.length = array.length /\
   *          forall i, 0 < i < rv.length ==> rv[i-1] <= r[i] (is sorted) /\
   *          count(rv[i], rv) = count(rv[i], array) (is a permutation)}
   * 
   * @param array array to sort
   * @return (rv: int[]) a sorted version of array
   *
   **/
  public static int[] sort(int[] array) {
    return sort(array, 0, array.length - 1);
  }

  private static int[] sort(int[] array, int from, int to) {
    if (from >= to) {
      return new int[] { array[from] };
    }

    int m = (from + to) / 2;
    int[] left = sort(array, from, m); // Defect -- 3rd param was 'm - 1', but should just be 'm'
    int[] right = sort(array, m + 1, to);

    int[] result = new int[left.length + right.length];
    int li = 0;
    int ri = 0;
    for (int i = 0; i < result.length; i++) {
      if (li < left.length && (ri >= right.length || left[li] < right[ri])) {
        result[i] = left[li];
        li++;
      } else {
        result[i] = right[ri];
        ri++;
      }
    }

    return result;
  }

}
