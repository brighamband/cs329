package edu.byu.cs329.constantfolding;

import edu.byu.cs329.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Integration test for constant folding (combined 5 cases)")
public class ConstantFoldingTests {
  @Test
  @DisplayName("Integration test for constant folding (includes 5 cases)")
  void integrationTestConstantFolding() {
    String rawName = "foldingInputs/constantfolding/ConstantFoldingIntegrated-raw.java";
    String expectedName = "foldingInputs/constantfolding/ConstantFoldingIntegrated-expected.java";
    TestUtils.assertDidFoldIntegration(this, rawName, expectedName);
  }
}
