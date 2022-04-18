package misc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing ff method using JUnit 5")
public class FTests {

  @Test
  @DisplayName("Should throw exception when x is null (1st @requires)")
  void should_ThrowException_when_XIsNull() {
    Object[] arr = { 1, 2, 3 };
    Object x = null;  // Null x (will cause the exception)
    assertThrows(NullPointerException.class, () -> Misc.ff(x, arr));
  }

  @Test
  @DisplayName("Should throw exception when array is null (2nd @requires)")
  void should_ThrowException_when_ArrIsNull() {
    Object[] arr = null;  // Null array (will cause the exception)
    Object x = 3;
    assertThrows(NullPointerException.class, () -> Misc.ff(x, arr));
  }

  @Test
  @DisplayName("Should throw exception when there's no null or x element in the array (3rd @requires)")
  void should_ThrowException_when_NoNullOrXInArr() {
    Object[] arr = {1, 2,  3};  // Array has no null elements
    Object x = 7; // X does not match any elements in array
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> Misc.ff(x, arr));
  }

  @Test
  @DisplayName("Should have x in array when passed in (1st @ensures)")
  void should_HaveXInArr_when_PassedIn() {
    Object[] arr = {1, null,  null};  // Array has room for x
    Object x = 7;
    Misc.ff(x, arr);
    assertTrue(Arrays.asList(arr).contains(x)); // X can be find in array
  }

  @Test
  @DisplayName("Should be the same exact array when x already exists in the array (2nd @ensures)")
  void should_BeSameArr_when_XAlreadyExistsInArr() {
    Object[] arr = { 1, 2, 3 };
    Object x = 2; // This x already exists in the array
    Misc.ff(x, arr);
    assertEquals(1, arr[0]);
    assertEquals(2, arr[1]);
    assertEquals(3, arr[2]);
  }

  @Test
  @DisplayName("Should be the same size as before when ff gets ran (3rd @ensures)")
  void should_BeSameSizeAsBefore_when_Ran() {
    Object[] arr = { 1, 2, 3 };
    int oldSize = arr.length;
    Object x = 2;
    Misc.ff(x, arr);  // Running this shouldn't change the size of the array
    int newSize = arr.length;
    assertEquals(oldSize, newSize);
  }

  @Test
  @DisplayName("Should work the same when non int type (String) is used for array and x")
  void should_WorkSame_when_NonIntTypeForArrAndX() {
    Object[] arr = { "a", "b",  "c"}; // Array of strings instead of ints this time
    Object x = "a"; // X exists in the array
    Misc.ff(x, arr);
    assertDoesNotThrow(() -> Misc.ff(x, arr));
    assertEquals("a", arr[0]);
    assertEquals("b", arr[1]);
    assertEquals("c", arr[2]);
  }
}
