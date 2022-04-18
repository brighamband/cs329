package edu.byu.cs329.mcdc;

/**
 * Class containing methods for problems 1 and 2.
 */
public class McDc {

  /**
   * Private constructor. Methods should only be called statically.
   * Example:
   * {@code boolean ans = McDc.f(true, true, true, true)}
   */
  private McDc() {
  }

  /**
   * Problem 1
   * 
   * <p>Write a minimal number of tests to achieve MC/DC coverage. 
   * Justify your tests.
   *
   * @param a parameter 1
   * @param b parameter 2
   * @param c parameter 3
   * @param d parameter 4
   * @return (out: boolean)
   */
  public static boolean problem1(boolean a, boolean b, boolean c, boolean d) {
    return (a && b) || (c && d);
  }

  /**
   * Problem 2
   * 
   * <p>Write a minimal number of tests to achieve MC/DC coverage.
   * Justify your tests.
   *
   * @param array input array to sort
   * @param from index to start sorting from
   * @param to index to sort to
   * @return (result: int[]) subset of array that got sorted
   */
  public static int[] problem2(int[] array, int from, int to) {
    if (from >= to) {
      return new int[] { array[from] };
    }

    int m = (from + to) / 2;
    int[] left = problem2(array, from, m);
    int[] right = problem2(array, m + 1, to);

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
