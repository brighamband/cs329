package com.makotojava.learn.hellojunit5;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.junit.Assume.assumeTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit 5 (with JUnitPlatform.class)
 *
 */
@RunWith(JUnitPlatform.class)
@DisplayName("Testing using JUnit 5")
public class JUnit5AppTest {

  // Create a JDK Logger here
  private static final Logger log = LoggerFactory.getLogger(JUnit5AppTest.class);

  // Create a fixture for the class under test
  private App classUnderTest;

  // Do something before ANY test is run in this class
  @BeforeAll
  public static void init() {
    log.info("@BeforeAll: init()");
  }
  
  // Do something after ALL tests in this class are run
  @AfterAll
  public static void done() {
    log.info("@AfterAll: done()");
  }
  
  // Create an instance of the test class before each @Test method is executed
  @BeforeEach
  public void setup() throws Exception {
    log.info("@BeforeEach: setup()");
    classUnderTest = new App();
  }

  // Destroy reference to the instance of the test class after each @Test method is executed
  @AfterEach
  public void tearDown() throws Exception {
    log.info("@AfterEach: tearDown()");
    classUnderTest = null;
  }

  // Disabled test
  @Test
  @Disabled
  @DisplayName("A disabled test")
  void testNotRun() {
    log.info("This test will not run since it is disabled");
  }

  @Test
  @DisplayName("Tests addition for positive numbers")
  public void testAdd() {
    log.info("@Test: testAdd()");
    assertNotNull(classUnderTest);
    assertAll(
      () -> {
        // Test #1
        long[] numsToSum = { 1, 2, 3, 4 };
        long expectedSum = 10;
        long actualSum = classUnderTest.add(numsToSum);
        assertEquals(expectedSum, actualSum);
      },
      () -> {
        // Test #2
        long[] numsToSum = { 20, 934, 110 };
        long expectedSum = 1064;
        long actualSum = classUnderTest.add(numsToSum);
        assertEquals(expectedSum, actualSum);
      },
      () -> {
        // Test #3
        long[] numsToSum = { 2, 4, 6 };
        long expectedSum = 12;
        long actualSum = classUnderTest.add(numsToSum);
        assertEquals(expectedSum, actualSum);
      }
    );
  }

  @Nested
  @DisplayName("Tests addition for negative numbers")
  class NegativeNumbersTest {

    private App classUnderTest;

    @BeforeEach
    public void setup() throws Exception {
      classUnderTest = new App();
    }

    @AfterEach
    public void tearDown() throws Exception {
      classUnderTest = null;
    }

    @Test
    @DisplayName("When there's 3 negative numbers to add")
    public void testAdd() {
      log.info("@Test: testAdd()");
      assertNotNull(classUnderTest);
      assertAll(
      () -> {
        // Test #1
        long[] numsToSum = { -1, -2, -3, -4 };
        long expectedSum = -10;
        long actualSum = classUnderTest.add(numsToSum);
        assertEquals(expectedSum, actualSum);
      },
      () -> {
        // Test #2
        long[] numsToSum = { -20, -934, -110 };
        long expectedSum = -1064;
        long actualSum = classUnderTest.add(numsToSum);
        assertEquals(expectedSum, actualSum);
      },
      () -> {
        // Test #3
        long[] numsToSum = { -2, -4, -6 };
        long expectedSum = -12;
        long actualSum = classUnderTest.add(numsToSum);
        assertEquals(expectedSum, actualSum);
      }
    );
    }
  }

  @Nested
  @DisplayName("Tests addition of positive and negative numbers")
  class PositiveAndNegativeNumbersTest {

    @Test
    @DisplayName("When there's a mix of positive and negative numbers in 3 tests")
    public void testAdd() {
      log.info("@Test: testAdd()");
      assertNotNull(classUnderTest);
      assertAll(
      () -> {
        // Test #1
        long[] numsToSum = { -1, 2, -3, 4 };
        long expectedSum = 2;
        long actualSum = classUnderTest.add(numsToSum);
        assertEquals(expectedSum, actualSum);
      },
      () -> {
        // Test #2
        long[] numsToSum = { -20, 934, -110 };
        long expectedSum = 804;
        long actualSum = classUnderTest.add(numsToSum);
        assertEquals(expectedSum, actualSum);
      },
      () -> {
        // Test #3
        long[] numsToSum = { -2, -4, 6 };
        long expectedSum = 0;
        long actualSum = classUnderTest.add(numsToSum);
        assertEquals(expectedSum, actualSum);
      }
    );
    }

    @Test
    @DisplayName("Tests add only on Friday (no lambda)")
    public void testAdd_OnlyOnFriday() {
      assertNotNull(classUnderTest);
      LocalDateTime ldt = LocalDateTime.now();
      assumeTrue("Test skipped... it's not Friday!", ldt.getDayOfWeek().getValue() == 5);
      long[] numsToSum = { 1, 2, 3, 4, 5 };
      long expectedSum = 15;
      long actualSum = classUnderTest.add(numsToSum);
      assertEquals(expectedSum, actualSum);
    }

    @Test
    @DisplayName("Tests add only on Thursday (with lambda)")
    public void testAdd_OnlyOnFriday_WithLambda() {
      assertNotNull(classUnderTest);
      LocalDateTime ldt = LocalDateTime.now();
      assumingThat(ldt.getDayOfWeek().getValue() == 4,
        () -> {
          long[] numsToSum = { 1, 2, 3, 4, 5 };
          long expectedSum = 15;
          long actualSum = classUnderTest.add(numsToSum);
          assertEquals(expectedSum, actualSum);
        }
      );
    }

  }

  @Nested
  @DisplayName("Tests single operand")
  class JUnit5AppSingleOperandTest {

    @Test
    @DisplayName("Adds numbers greater than 0")
    public void testAdd_NumbersGt0() {
      assertNotNull(classUnderTest);
      assertAll(
        () -> {
          // Test #1
          long[] numsToSum = { 1 };
          long expectedSum = 1;
          long actualSum = classUnderTest.add(numsToSum);
          assertEquals(expectedSum, actualSum);
        },
        () -> {
          // Test #2
          long[] numsToSum = { 0 };
          long expectedSum = 0;
          long actualSum = classUnderTest.add(numsToSum);
          assertEquals(expectedSum, actualSum);
        }
      );
    }

    @Test
    @DisplayName("Tests numbers less than 0")
    public void testAdd_NumbersLt0() {
      assertNotNull(classUnderTest);
      assertAll(
        () -> {
          // Test #1
          long[] numsToSum = { -1 };
          long expectedSum = -1;
          long actualSum = classUnderTest.add(numsToSum);
          assertEquals(expectedSum, actualSum);
        },
        () -> {
          // Test #2
          long[] numsToSum = { -10 };
          long expectedSum = -10;
          long actualSum = classUnderTest.add(numsToSum);
          assertEquals(expectedSum, actualSum);
        }
      );
    }
  }

  @Nested
  @DisplayName("Tests zero operands")
  class JUnit5AppZeroOperandsTest {

    @Test
    @DisplayName("Tests adding zero operands with empty argument")
    public void testAdd_ZeroOperands_EmptyArgument() {
      assertNotNull(classUnderTest);
      long[] numsToSum = {};
      Throwable expectedException = assertThrows(IllegalArgumentException.class, () -> classUnderTest.add(numsToSum));
      assertEquals("Operands argument cannot be empty", expectedException.getLocalizedMessage());
    }

    @Test
    @DisplayName("Tests adding zero operands with null argument")
    public void testAdd_ZeroOperands_NullArgument() {
      assertNotNull(classUnderTest);
      long[] numsToSum = null;
      Throwable expectedException = assertThrows(IllegalArgumentException.class, () -> classUnderTest.add(numsToSum));
      assertEquals("Operands argument cannot be null", expectedException.getLocalizedMessage());
    }
  }
}