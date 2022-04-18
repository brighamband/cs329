package edu.byu.cs329.pbt.stateful;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Test suite for the hash map.
 */
@DisplayName("Tests for Linked-List HashMap")
public class LlHashMapTest {
    private static Random rand = new Random();
    private static int NUM_METHOD_CALLS = 15;
    private static int RAND_NUM_BOUND = 99;

    static LlHashMap internal;
    static HashMap oracle;

    @BeforeEach
    public void reinitialize() {
        int numBuckets = generatePositiveInt();
        internal = new LlHashMap(numBuckets);
        oracle = new HashMap(numBuckets);
    }

    private static Stream<Arguments> generateNegativeIntArgs() {
        Stream<Arguments> argStream = Stream.of(arguments(generateNegativeInt()));
        for (int i = 0; i < NUM_METHOD_CALLS - 1; i++) {
            argStream = Stream.concat(argStream, Stream.of(
                    arguments(generateNegativeInt())));
        }
        return argStream;
    }

    @ParameterizedTest
    @MethodSource("generateNegativeIntArgs")
    @DisplayName("Should throw an exception when there's zero or less buckets")
    public void should_ThrowException_when_ZeroOrLessBuckets(int numBuckets) {
        assertThrows(IllegalArgumentException.class, () -> new LlHashMap(numBuckets));
        assertThrows(IllegalArgumentException.class, () -> new HashMap<>(numBuckets));
    }

    private static Stream<Arguments> generatePositiveIntArgs() {
        Stream<Arguments> argStream = Stream.of(arguments(generatePositiveInt()));
        for (int i = 0; i < NUM_METHOD_CALLS - 1; i++) {
            argStream = Stream.concat(argStream, Stream.of(
                    arguments(generatePositiveInt())));
        }
        return argStream;
    }

    @ParameterizedTest
    @MethodSource("generatePositiveIntArgs")
    @DisplayName("Should not throw an exception when there's a positive amount of buckets")
    public void should_NotThrowException_when_PositiveBuckets(int numBuckets) {
        assertDoesNotThrow(() -> new LlHashMap(numBuckets));
        assertDoesNotThrow(() -> new HashMap<>(numBuckets));
    }

    private static Stream<Arguments> generateKeyValueArgs() {
        Stream<Arguments> argStream = Stream.of(arguments(generatePositiveInt(), generatePositiveInt()));
        for (int i = 0; i < NUM_METHOD_CALLS - 1; i++) {
            argStream = Stream.concat(argStream, Stream.of(
                    arguments(generatePositiveInt(), generatePositiveInt())));
        }
        return argStream;
    }

    @ParameterizedTest
    @MethodSource("generateKeyValueArgs")
    @DisplayName("Should contains true when one item is put")
    public void should_ContainsTrue_when_OneItemPut(int key, int value) {
        putItemInMaps(key, value);
        assertMapsAreEquivalent();
        assertMapsHaveKey(key);
    }

    @ParameterizedTest
    @MethodSource("generateKeyValueArgs")
    @DisplayName("Should be exact same when identical put called again")
    public void should_BeExactSame_when_IdenticalPutCalledAgain(int key, int value) {
        putItemInMaps(key, value);
        putItemInMaps(key, value);
        assertMapsAreEquivalent();
        assertMapsHaveKey(key);
    }


    @ParameterizedTest
    @MethodSource("generateNegativeIntArgs")
    @DisplayName("Should return null when get called on item missing from map")
    public void should_ReturnNull_when_GetCalledOnItemMissingFromMap(int key) {
        // Call get with invalid key
        assertNull(internal.get(key));
        assertNull(oracle.get(key));
    }

    @ParameterizedTest
    @MethodSource("generateKeyValueArgs")
    @DisplayName("Should return value when get called on existing item")
    public void should_ReturnValue_when_GetCalledOnExistingItem(int key, int value) {
        // Setup
        putItemInMaps(key, value);
        assertMapsAreEquivalent();
        assertMapsHaveKey(key);

        // Item should be found
        assertEquals(value, internal.get(key));
        assertEquals(value, oracle.get(key));
        assertMapsAreEquivalent();
        assertMapsHaveKey(key);
    }

    @ParameterizedTest
    @MethodSource("generateKeyValueArgs")
    @DisplayName("Should contains false when existing item removed")
    public void should_ContainsFalse_when_ExistingItemRemoved(int key, int value) {
        // Load item into map so you have something to remove
        putItemInMaps(key, value);
        assertMapsAreEquivalent();
        assertMapsHaveKey(key);

        removeItemFromMaps(key);
        assertMapsAreEquivalent();
        assertKeyMissingInMaps(key);
    }

    @ParameterizedTest
    @MethodSource("generateKeyValueArgs")
    @DisplayName("Should make no changes false when item already removed")
    public void should_MakeNoChanges_when_ItemAlreadyRemoved(int key, int value) {
        // Setup

        putItemInMaps(key, value);
        assertMapsAreEquivalent();
        assertMapsHaveKey(key);

        removeItemFromMaps(key);
        assertMapsAreEquivalent();
        assertKeyMissingInMaps(key);

        // Try removing item again
        removeItemFromMaps(key);
        assertMapsAreEquivalent();
        // Key should still be missing
        assertKeyMissingInMaps(key);
    }

    @ParameterizedTest
    @MethodSource("generateKeyValueArgs")
    @DisplayName("Should be exact same when identical put called again")
    public void should_Contain_when_PutRemovedThenPut(int key, int value) {
        putItemInMaps(key, value);
        assertMapsAreEquivalent();
        // Assert maps contain item
        assertEquals(internal.contains(key), oracle.containsKey(key));
        assertTrue(internal.contains(key));
        assertTrue(oracle.containsKey(key));

        removeItemFromMaps(key);
        assertMapsAreEquivalent();
        // Assert maps do not contain item
        assertEquals(internal.contains(key), oracle.containsKey(key));
        assertFalse(internal.contains(key));
        assertFalse(oracle.containsKey(key));

        putItemInMaps(key, value);
        assertMapsAreEquivalent();
        // Assert maps contain item again
        assertEquals(internal.contains(key), oracle.containsKey(key));
        assertTrue(internal.contains(key));
        assertTrue(oracle.containsKey(key));
    }

    // Helper functions

    private static int generatePositiveInt() {
        return rand.nextInt(RAND_NUM_BOUND) + 1;
    }

    private static int generateNegativeInt() {
        return rand.nextInt(RAND_NUM_BOUND) - (2 * RAND_NUM_BOUND);
    }

    private static void putItemInMaps(int key, int value) {
        internal.put(key, value);
        oracle.put(key, value);
    }

    private static void removeItemFromMaps(int key) {
        internal.remove(key);
        oracle.remove(key);
    }

    private static void assertMapsAreEquivalent() {
        for (Object key : oracle.keySet()) {
            // Keys exist in both
            assertTrue(internal.contains((Integer) key));
            // Values match
            assertEquals(oracle.get(key), internal.get((Integer) key));
        }
    };

    private static void assertMapsHaveKey(int key) {
        assertEquals(internal.contains(key), oracle.containsKey(key));
        assertTrue(internal.contains(key));
        assertTrue(oracle.containsKey(key));
    }

    private static void assertKeyMissingInMaps(int key) {
        assertEquals(internal.contains(key), oracle.containsKey(key));
        assertFalse(internal.contains(key));
        assertFalse(oracle.containsKey(key));
    }
}
