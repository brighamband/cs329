package edu.byu.cs329.hw1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisplayName("Testing using JUnit 5")
public class SumVisitorTest {

  private static final Logger log = LoggerFactory.getLogger(SumVisitorTest.class);

  private SumVisitor sumVisitor;

  @BeforeAll
  public static void init() {
    log.info("@BeforeAll: init()");
  }
  
  @AfterAll
  public static void done() {
    log.info("@AfterAll: done()");
  }
  
  @BeforeEach
  public void setup() throws Exception {
    log.info("@BeforeEach: setup()");
    sumVisitor = new SumVisitor();
  }

  @AfterEach
  public void tearDown() throws Exception {
    log.info("@AfterEach: tearDown()");
    sumVisitor = null;
  }

  // Disabled test
  @Test
  @Tag("development")
  // @Disabled
  @DisplayName("A disabled test")
  void testNotRun() {
    log.info("This test will not run if we uncomment that @Disabled tag");
  }

  @Test
  @Tag("development")
  @DisplayName("Tests that sum visitor starts out at 0")
  public void testSumIsZeroIfStarting() {
    assertEquals(0, sumVisitor.getTotal());
  }

  @Test
  @Tag("development")
  @DisplayName("Tests that visit returns true")
  public void testVisitReturnsTrue() {
    NumberNode n = new NumberNode(1);
    assertTrue(sumVisitor.visit(n));
  }

  @Test
  @Tag("development")
  @DisplayName("Tests end visit runs fine")
  public void testEndVisitRunsFine() {
    NumberNode n = new NumberNode(1);
    sumVisitor.endVisit(n);
  }

  @Nested
  @DisplayName("Tests sum updates correctly when visiting positive and negative numbers")
  class SumUpdateTest {

    private SumVisitor sumVisitor;

    @BeforeEach
    public void setup() throws Exception {
      sumVisitor = new SumVisitor();
    }

    @AfterEach
    public void tearDown() throws Exception {
      sumVisitor = null;
    }

    @Test
    @Tag("development")
    @DisplayName("Tests that visit to positive number increases sum")
    public void testPositiveNumVisitIncreasesSum() {
      NumberNode posNumNode = new NumberNode(5);
      sumVisitor.visit(posNumNode);
      assertEquals(5, sumVisitor.getTotal());
    }

    @Test
    @Tag("development")
    @DisplayName("Tests that visit to negative number decreases sum")
    public void testNegativeNumVisitDecreasesSum() {
      NumberNode negNumNode = new NumberNode(-5);
      sumVisitor.visit(negNumNode);
      assertEquals(-5, sumVisitor.getTotal());
    }
  }
}