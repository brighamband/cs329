package edu.byu.cs329.pbt.stateless;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Test suite for BinarySearch.
 */
@DisplayName("Tests for BinarySearch")
public class BinarySearchTest {
  private static Random rand = new Random();
  private static int NUM_METHOD_CALLS = 15;
  private static int RAND_NUM_BOUND = 99;

  // Tests for BinarySearch.sort()

  private static Stream<Arguments> getNullArrayArgs() {
    Stream<Arguments> argStream = Stream.of(arguments(null, rand.nextInt(RAND_NUM_BOUND)));
    for (int i = 0; i < NUM_METHOD_CALLS - 1; i++) {
      argStream = Stream.concat(argStream, Stream.of(arguments(null, rand.nextInt(RAND_NUM_BOUND))));
    }
    return argStream;
  }

  @ParameterizedTest
  @MethodSource("getNullArrayArgs")
  @DisplayName("Should throw (null pointer) exception when array is null (1st requires)")
  void should_ThrowException_when_ArrayIsNull(int[] array, int value) {
    assertThrows(NullPointerException.class, () -> BinarySearch.search(array, value));
  }

  private static Stream<Arguments> getUnsortedArrayArgs() {
    Stream<Arguments> argStream = Stream.of(arguments(generateRandomUnsortedArray(), rand.nextInt(RAND_NUM_BOUND)));
    for (int i = 0; i < NUM_METHOD_CALLS - 1; i++) {
      argStream = Stream.concat(argStream, Stream.of(arguments(generateRandomUnsortedArray(), rand.nextInt(RAND_NUM_BOUND))));
    }
    return argStream;
  }

  @ParameterizedTest
  @MethodSource("getUnsortedArrayArgs")
  @DisplayName("Should throw (null pointer) exception when array is not sorted in ascending order (2nd requires)")
  void should_ThrowException_when_ArrayIsUnsorted(int[] array, int value) {
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> BinarySearch.search(array, value));
  }

  private static Stream<Arguments> getItemFoundArgs() {
    int[] array = generateRandomSortedArray();
    int randIdx = rand.nextInt(array.length);
    Stream<Arguments> argStream = Stream.of(arguments(array, array[randIdx]));
    for (int i = 0; i < NUM_METHOD_CALLS - 1; i++) {
      array = generateRandomSortedArray();
      randIdx = rand.nextInt(array.length);
      argStream = Stream.concat(argStream, Stream.of(arguments(array, array[randIdx])));
    }
    return argStream;
  }

  @ParameterizedTest
  @MethodSource("getItemFoundArgs")
  @DisplayName("Should return true when the item is in the array (ensures)")
  void should_ReturnTrue_when_ItemInArray(int[] array, int value) {
    boolean foundInOurs = BinarySearch.search(array, value);
    assertTrue(foundInOurs);
    // Test oracle
    List<Integer> oracle = Arrays.stream(array).boxed().collect(Collectors.toList());
    boolean foundInOracle = oracle.contains(value);
    assertTrue(foundInOracle);
    // Assert ours is same as oracle
    assertEquals(foundInOurs, foundInOracle);
  }

  private static Stream<Arguments> getItemNotFoundArgs() {
    Stream<Arguments> argStream = Stream.of(arguments(generateRandomSortedArray(), rand.nextInt(50) + 300));
    for (int i = 0; i < NUM_METHOD_CALLS - 1; i++) {
      argStream = Stream.concat(argStream, Stream.of(arguments(generateRandomSortedArray(), rand.nextInt(50) + 300)));
    }
    return argStream;
  }

  @ParameterizedTest
  @MethodSource("getItemNotFoundArgs")
  @DisplayName("Should return false when the item is not in the array (ensures)")
  void should_ReturnFalse_when_ItemInArray(int[] array, int value) {
    boolean foundInOurs = BinarySearch.search(array, value);
    assertFalse(foundInOurs);
    // Test oracle
    List<Integer> oracle = Arrays.stream(array).boxed().collect(Collectors.toList());
    boolean foundInOracle = oracle.contains(value);
    assertFalse(foundInOracle);
    // Assert ours is same as oracle
    assertEquals(foundInOurs, foundInOracle);
  }

  // Algebraic property tests

  @ParameterizedTest
  @MethodSource("getItemFoundArgs")
  @DisplayName("Should have a sorted array in ascending order when item is found (algebraic property)")
  void should_HaveSortedArray_when_ItemIsFound(int[] array, int value) {
    boolean foundInOurs = BinarySearch.search(array, value);
    assertTrue(foundInOurs);
    // Test oracle
    List<Integer> oracle = Arrays.stream(array).boxed().collect(Collectors.toList());
    boolean foundInOracle = oracle.contains(value);
    assertTrue(foundInOracle);
    // Assert sorting
    assertTrue(isSorted(array));
  }

  @ParameterizedTest
  @MethodSource("getItemFoundArgs")
  @DisplayName("Should throw an exception when searching a flipped version of an array " +
          "after an item is found in its normal version")
  void should_ThrowException_when_SearchingFlippedArrayAfterItemIsFound(int[] array, int value) {
    // Ensure item is first found

    boolean foundInOurs = BinarySearch.search(array, value);
    assertTrue(foundInOurs);
    // Test oracle
    List<Integer> oracle = Arrays.stream(array).boxed().collect(Collectors.toList());
    boolean foundInOracle = oracle.contains(value);
    assertTrue(foundInOracle);
    // Assert sorting
    assertTrue(isSorted(array));

    // Ensure that throws an exception after flipping array and trying to find the same item

    int[] reversedArr = reverseArray(array);
    assertFalse(isSorted(reversedArr));
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> BinarySearch.search(reversedArr, value));
  }

  @ParameterizedTest
  @MethodSource("getItemFoundArgs")
  @DisplayName("Should still find item when searched for multiple times (algebraic property)")
  void should_StillFindItem_when_SearchedForMultipleTimes(int[] array, int value) {
    boolean foundInOurs = BinarySearch.search(array, value);
    assertTrue(foundInOurs);
    // Test oracle
    List<Integer> oracle = Arrays.stream(array).boxed().collect(Collectors.toList());
    boolean foundInOracle = oracle.contains(value);
    assertTrue(foundInOracle);

    // Search again and verify same results
    foundInOurs = BinarySearch.search(array, value);
    assertTrue(foundInOurs);

    foundInOracle = oracle.contains(value);
    assertTrue(foundInOracle);
  }

  // Helper Functions

  /**
   * Generates a randomized integer array with 4-20 digits where each number is between 0-99.
   */
  private static int[] generateRandomArray() {
    int[] array = new Random().ints(rand.nextInt(20) + 4, 0, 99).toArray();
    return array;
  }

  private static boolean isSorted(int[] array) {  // Added to fix unsorted array defect
    int[] sortedArray = array.clone();
    Arrays.sort(sortedArray);
    return Arrays.equals(array, sortedArray);
  }

  private static int[] generateRandomUnsortedArray() {
    int[] array = generateRandomArray();
    while(isSorted(array)) {
      array = generateRandomArray();
    }
    return array;
  }

  private static int[] generateRandomSortedArray() {
    int[] array = generateRandomArray();
    Arrays.sort(array);
    return array;
  }

  private int[] reverseArray(int[] array) {
    int[] reversedArr = new int[array.length];
    int j = array.length;
    for (int i = 0; i < array.length; i++) {
      reversedArr[j - 1] = array[i];
      j = j - 1;
    }
    return reversedArr;
  }
}
