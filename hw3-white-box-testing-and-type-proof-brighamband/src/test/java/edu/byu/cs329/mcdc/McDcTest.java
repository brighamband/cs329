package edu.byu.cs329.mcdc;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite for the McDc static methods.
 */
@DisplayName("Tests for McDc Methods")
public class McDcTest {

    @Nested
    @Tag("Problem1")
    @DisplayName("Tests for Problem 1")
    class Problem1Tests {

        // Table
        //
        //      |   A   B   C   D   |  Res
        // -------------------------
        //  R1  |   T   T   F   F   |   T
        //  R2  |   F   T   F   F   |   F
        //  R3  |   T   F   F   F   |   F
        //  R4  |   F   F   T   T   |   T
        //  R5  |   F   F   F   T   |   F
        //  R6  |   F   F   T   F   |   F

        // A -> R1, R2
        // B -> R1, R3
        // C -> R4, R5
        // D -> R4, R6

        boolean row1Result; // T
        boolean row2Result; // F
        boolean row3Result; // F
        boolean row4Result; // T
        boolean row5Result; // F
        boolean row6Result; // F

        @BeforeEach
        void beforeEach() {
            computeTable();
        }

        void computeTable() {
            // Row 1
            row1Result = McDc.problem1(true, true, false, false);
             // Row 2
            row2Result = McDc.problem1(false, true, false, false);
             // Row 3
            row3Result = McDc.problem1(true, false, false, false);
             // Row 4
            row4Result = McDc.problem1(false, false, true, true);
             // Row 5
            row5Result = McDc.problem1(false, false, false, true);
             // Row 6
            row6Result = McDc.problem1(false, false, true, false);
        }

        @Test
        @Tag("Problem1A")
        @DisplayName("Should MC/DC cover A with rows 1 and 2")
        void should_CoverA_with_Rows1And2() {
            // Row 1 - T
            assertTrue(row1Result);
            // Row 2 - F
            assertFalse(row2Result);
        }

        @Test
        @Tag("Problem1B")
        @DisplayName("Should MC/DC cover B with rows 1 and 3")
        void should_CoverB_with_Rows1And3() {
            // Row 1 - T
            assertTrue(row1Result);
            // Row 3 - F
            assertFalse(row3Result);
        }

        @Test
        @Tag("Problem1C")
        @DisplayName("Should MC/DC cover C with rows 4 and 5")
        void should_CoverC_with_Rows4And5() {
            // Row 4 - T
            assertTrue(row4Result);
            // Row 5 - F
            assertFalse(row5Result);
        }

        @Test
        @Tag("Problem1D")
        @DisplayName("Should MC/DC cover D with rows 4 and 6")
        void should_CoverD_with_Rows4And6() {
            // Row 4 - T
            assertTrue(row4Result);
            // Row 6 - F
            assertFalse(row6Result);
        }
    }

    @Nested
    @Tag("Problem2")
    @DisplayName("Tests for Problem 2")
    class Problem2Tests {
        // Table for If Statement (A, B, C are the 3 conditions)
        //
        //      |   A   B   C   |  Res
        // -------------------------
        //  R1  |   T   T   T   |   T
        //  R2  |   F   T   T   |   F
        //  R3  |   T   F   T   |   T
        //  R4  |   T   F   F   |   F

        // A -> R1, R2
        // B -> R1, R3
        // C -> R3, R4

        @Test
        @Tag("Problem2Pre")
        @DisplayName("Should MC/DC cover first if when from is less than and bigger")
        void should_CoverFirstIf_when_FromIsLessThanAndBigger() {
            int[] expected = {1, 2};
            int[] actual = McDc.problem2(new int[]{1, 2}, 0, 1);
            // True case for if statement (on 1st iteration):  from: 0, to: 1
            // False case for if statement (on 2nd iteration):  from: 0, to: 0
            assertTrue(Arrays.equals(expected, actual));
        }

        @Test
        @Tag("Problem1A")
        @DisplayName("Should MC/DC cover A and still sort correctly")
        void should_CoverA_and_StillSortCorrectly() {
            int[] expected = {0, 1, 3, 8};
            int[] actual = McDc.problem2(new int[]{3, 1, 0, 8}, 0, 3);
            //  First case for A:  T   T   T
            //  Second case for A: F   T   T
            assertTrue(Arrays.equals(expected, actual));
        }

        @Test
        @Tag("Problem1B")
        @DisplayName("Should MC/DC cover B and still sort correctly")
        void should_CoverB_and_StillSortCorrectly() {
            int[] expected = {1, 2, 3, 4};
            int[] actual = McDc.problem2(new int[]{1, 2, 4, 3}, 0, 3);
            //  First case for B (on 4th iteration):   T   T   T (technically C will be undefined since B and C are mutually exclusive)
            //  Second case for B (on 1st iteration):  T   F   T
            assertTrue(Arrays.equals(expected, actual));
        }

        @Test
        @Tag("Problem1C")
        @DisplayName("Should MC/DC cover C and still sort correctly")
        void should_CoverC_and_StillSortCorrectly() {
            int[] expected = {3, 4, 6};
            int[] actual = McDc.problem2(new int[]{3, 6, 4}, 0, 2);
            //  First case for C (on 1st iteration):   T   F   T
            //  Second case for C (on 4th iteration):  T   F   F
            assertTrue(Arrays.equals(expected, actual));
        }
    }
}
