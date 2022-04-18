package misc;

public class Misc {

  /**
   * Adds an element to an array if it is not already in the array.
   * 
   * @requires x != null
   * @requires arr != null
   * @requires exists i in arr (i = null || i = x)
   * 
   * @ensures exists i in arr (i = x)
   *      ---- array will have x
   * @ensures exists x in old(arr) ==> arr == old(arr)
   *      --- array will stay the exact same if element already exists
   * @ensures size(old(arr)) == size(arr) ---- size will stay the same
   * 
   * @param x   element to add
   * @param arr array to which x is added
   */
  public static void ff(Object x, Object[] arr) {
    int i;
    boolean exists = false;

    for (i = 0; i < arr.length; i++) { // A1
      if (x.equals(arr[i])) { // B1
        exists = true;
        break;
      } // B2
    } // A2
    if (!exists) { // C1
      for (i = 0;; i++) { // D1
        if (arr[i] == null) { // E1
          arr[i] = x;
          break;
        } // E2
      } // D2
    } // C2
  }
}
