package edu.byu.cs329.constantpropagation;

import edu.byu.cs329.TestUtils;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Tests for ConstantPropagation")
public class ConstantPropagationTests {

  /**
   * Black-box and white-box tests
   */

  @Nested
  @DisplayName("Tests the entire constant propagation algorithm")
  class PropagateTests {

    @Test
    @DisplayName("Tests propagate numbers")
    void shouldPropagateNum() {
      String rawName = "constPropInputs/shouldPropagateNum-raw.java";
      String expectedName = "constPropInputs/shouldPropagateNum-expected.java";
      TestUtils.assertDidPropagate(this, rawName, expectedName);
    }

    @Test
    @DisplayName("Should propagating booleans")
    void shouldPropagateBool() {
      String rawName = "constPropInputs/shouldPropagateBool-raw.java";
      String expectedName = "constPropInputs/shouldPropagateBool-expected.java";
      TestUtils.assertDidPropagate(this, rawName, expectedName);
    }

    @Test
    @DisplayName("Should propagate strings")
    void shouldPropagateString() {
      String rawName = "constPropInputs/shouldPropagateString-raw.java";
      String expectedName = "constPropInputs/shouldPropagateString-expected.java";
      TestUtils.assertDidPropagate(this, rawName, expectedName);
    }

    @Test
    @DisplayName("Should propagate with fold")
    void shouldPropagateWithFold() {
      String rawName = "constPropInputs/shouldPropagateWithFold-raw.java";
      String expectedName = "constPropInputs/shouldPropagateNum-expected.java";
      TestUtils.assertDidPropagate(this, rawName, expectedName);
    }

    @Test
    @DisplayName("Should propagate a variable declaration")
    void shouldPropagateVarDeclaration() {
      String rawName = "constPropInputs/shouldPropagateVarDeclaration-raw.java";
      String expectedName = "constPropInputs/shouldPropagateVarDeclaration-expected.java";
      TestUtils.assertDidPropagate(this, rawName, expectedName);
    }

    @Test
    @DisplayName("Should propagate an expression statement")
    void shouldPropagateExpressionStatement() {
      String rawName = "constPropInputs/shouldPropagateExpressionStatement-raw.java";
      String expectedName = "constPropInputs/shouldPropagateExpressionStatement-expected.java";
      TestUtils.assertDidPropagate(this, rawName, expectedName);
    }

    @Test
    @DisplayName("Should propagate single infix expression")
    void shouldPropagateSingleInfix() {
      String rawName = "constPropInputs/shouldPropagateSingleInfix-raw.java";
      String expectedName = "constPropInputs/shouldPropagateSingleInfix-expected.java";
      TestUtils.assertDidPropagate(this, rawName, expectedName);
    }

    @Test
    @DisplayName("Should not initially propagate opposing literal definitions (will wait for future iterations)")
    void shouldNotInitiallyPropagateOpposingLiterals() {
      String rawName = "constPropInputs/shouldNotInitiallyPropagateOpposingLiterals-raw.java";
      String expectedName = "constPropInputs/shouldNotInitiallyPropagateOpposingLiterals-expected.java";
      TestUtils.assertDidPropagate(this, rawName, expectedName);
    }

    @Test
    @DisplayName("Integration test for constant propagation")
    void integrationTestConstProp() {
      String rawName = "constPropInputs/integrationTestConstProp-raw.java";
      String expectedName = "constPropInputs/integrationTestConstProp-expected.java";
      TestUtils.assertDidPropagate(this, rawName, expectedName);
    }

    @Test
    @DisplayName("Simple integration test for constant propagation")
    void simpleIntegrationTestConstProp() {
      String rawName = "constPropInputs/simpleIntegrationTestConstProp-raw.java";
      String expectedName = "constPropInputs/simpleIntegrationTestConstProp-expected.java";
      TestUtils.assertDidPropagate(this, rawName, expectedName);
    }
  }

  @Nested
  @DisplayName("Tests main function")
  class MainTests {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;


    @Before
    public void setUpStreams() {
      System.setOut(new PrintStream(outContent));
      System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
      System.setOut(originalOut);
      System.setErr(originalErr);
    }

    @Test
    @DisplayName("Tests passing in null args")
    void testNullArgs() {
      String[] args = null;
      assertThrows(NullPointerException.class, () -> ConstantPropagation.main(args));
    }

    @Test
    @DisplayName("Tests pass in not enough args")
    void testNotEnoughArgs() {
      String[] args = new String[] {"test"};
      ConstantPropagation.main(args);
      assertEquals("usage: java DomViewer <java file to parse> <html file to write>",
              outContent.toString());
      // Would have tested the line above it System.exit() didn't kill the program
    }


    @Test
    @DisplayName("Tests bad output destination (if invalid destination specified for args[1]")
    void testBadOutputDestination() {
      String input = "constPropInputs/shouldPropagateNum-raw.java";
      String output = "BAD LOCATION";
      String[] args = new String[] {input, output};

      assertDoesNotThrow(() -> ConstantPropagation.main(args));
    }

    @Test
    @DisplayName("Tests a valid file (should succeed)")
    void testValidFile() {
      String rawName = "constPropInputs/integrationTestConstProp-raw.java";
      String expectedName = "constPropInputs/integrationTestConstProp-expected.java";
      String[] args = new String[] {rawName, expectedName};
      
      assertDoesNotThrow(() -> ConstantPropagation.main(args));
    }
  }
}
