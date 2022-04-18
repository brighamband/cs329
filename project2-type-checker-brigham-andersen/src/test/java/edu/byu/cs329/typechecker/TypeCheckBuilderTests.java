package edu.byu.cs329.typechecker;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.byu.cs329.utils.JavaSourceUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.opentest4j.AssertionFailedError;

@DisplayName("Tests for TypeCheckerBuilder")
public class TypeCheckBuilderTests {
    private boolean getTypeChecker(final String fileName, List<DynamicNode> tests) {
        ASTNode compilationUnit = JavaSourceUtils.getAstNodeFor(this, fileName);
        SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
        ISymbolTable symbolTable = symbolTableBuilder.getSymbolTable(compilationUnit);
        TypeCheckBuilder typeCheckerBuilder = new TypeCheckBuilder();
        return typeCheckerBuilder.getTypeChecker(symbolTable, compilationUnit, tests);
    }

    void addNodeAndChildren(DynamicNode node, List<DynamicNode> list) {
        list.add(node);
        if (node instanceof DynamicContainer) {
            DynamicContainer container = (DynamicContainer) node;
            Iterator<? extends DynamicNode> children = container.getChildren().iterator();
            while (children.hasNext()) {
                addNodeAndChildren(children.next(), list);
            }
        }
    }

    List<DynamicNode> buildListOfDynamicNodes(List<DynamicNode> proof) {
        List<DynamicNode> allNodes = new ArrayList<>();
        addNodeAndChildren(proof.get(0), allNodes);
        return allNodes;
    }

    @TestFactory
    @DisplayName("Should prove type safe when given empty class")
    Stream<DynamicNode> should_proveTypeSafe_when_givenEmptyClass() {
        String fileName = "typeChecker/should_proveTypeSafe_when_givenEmptyClass.java";
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
        tests.add(test);
        return tests.stream();
    }

    @TestFactory
    @DisplayName("Should prove type safe when given empty method")
    Stream<DynamicNode> should_proveTypeSafe_when_givenEmptyMethod() {
        String fileName = "typeChecker/should_proveTypeSafe_when_givenEmptyMethod.java";
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
        tests.add(test);
        return tests.stream();
    }

    // New test factory
    @TestFactory
    @DisplayName("Should prove type safe when multiple methods in a class")
    Stream<DynamicNode> should_proveTypeSafe_when_multiMethodsInAClass() {
        String fileName = "typeChecker/should_proveTypeSafe_when_multiMethodsInAClass.java";
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
        tests.add(test);
        return tests.stream();
    }

    // New test factory
    @TestFactory
    @DisplayName("Should prove type safe when method has params and is non void")
    Stream<DynamicNode> should_proveTypeSafe_when_methodHasParamsAndIsNonVoid() {
        String fileName = "typeChecker/should_proveTypeSafe_when_methodHasParamsAndIsNonVoid.java";
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
        tests.add(test);
        return tests.stream();
    }

    // New test factory
    @TestFactory
    @DisplayName("Should prove type safe when extended prefix bools")
    Stream<DynamicNode> should_proveTypeSafe_when_extendedPrefixBools() {
        String fileName = "typeChecker/should_proveTypeSafe_when_extendedPrefixBools.java";
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
        tests.add(test);
        return tests.stream();
    }

    // New test factory
    @TestFactory
    @DisplayName("Should prove type safe when valid ifs")
    Stream<DynamicNode> should_proveTypeSafe_when_validIfs() {
        String fileName = "typeChecker/should_proveTypeSafe_when_validIfs.java";
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
        tests.add(test);
        return tests.stream();
    }

    // New test factory
    @TestFactory
    @DisplayName("Should not prove type safe when invalid ifs")
    Stream<DynamicNode> should_notProveTypeSafe_when_invalidIfs() {
        String fileName = "typeChecker/should_notProveTypeSafe_when_invalidIfs.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);

         // Toggle as desired
         //
         // Option 1: mvn exec:java shows the details of the typeproof for visual
         // inspection
//          return tests.stream();
         //
         // Option 2: test only isNotTypeSafe and show no details
         DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
         return Arrays.asList((DynamicNode) test).stream();
    }

     @TestFactory
     @DisplayName("Should prove type safe when given empty block")
     Stream<DynamicNode> should_proveTypeSafe_when_givenEmptyBlock() {
         String fileName = "typeChecker/should_proveTypeSafe_when_givenEmptyBlock.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);
         DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
         tests.add(test);
         return tests.stream();
     }

     @TestFactory
     @DisplayName("Should prove type safe when given variable declarations no inits")
     Stream<DynamicNode> should_proveTypeSafe_when_givenVariableDeclarationsNoInits() {
         String fileName = "typeChecker/should_proveTypeSafe_when_givenVariableDeclarationsNoInits.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);
         DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
         tests.add(test);
         return tests.stream();
     }

     @TestFactory
     @DisplayName("Should prove type safe when given variable declarations with compatible inits")
     Stream<DynamicNode> should_proveTypeSafe_when_givenVariableDeclarationsWithCompatibleInits() {
         String fileName = "typeChecker/should_proveTypeSafe_when_givenVariableDeclarationsWithCompatibleInits.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);
         DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
         tests.add(test);
         return tests.stream();
     }

     @TestFactory
     @DisplayName("Should not prove type safe when given bad inits")
     Stream<DynamicNode> should_notProveTypeSafe_when_givenBadInits() {
         String fileName = "typeChecker/should_notProveTypeSafe_when_givenBadInits.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);

         // Toggle as desired
         //
         // Option 1: mvn exec:java shows the details of the typeproof for visual
         // inspection
         // return tests.stream();
         //
         // Option 2: test only isNotTypeSafe and show no details
         DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
         return Arrays.asList((DynamicNode) test).stream();
     }

     // NEW TEST METHODS

     // ExpressionStatement:void for Assignment tests
     @TestFactory
     @DisplayName("Should prove type safe when expression statement has compatible assignment")
     Stream<DynamicNode> should_proveTypeSafe_when_expressionStatementHasCompatibleAssignment() {
         String fileName = "typeChecker/should_proveTypeSafe_when_expressionStatementHasCompatibleAssignment.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);
         DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
         tests.add(test);
         return tests.stream();
     }

     @TestFactory
     @DisplayName("Should not prove type safe when expression statement has incompatible assignment")
     Stream<DynamicNode> should_notProveTypeSafe_when_expressionStatementHasIncompatibleAssignment() {
         String fileName = "typeChecker/should_notProveTypeSafe_when_expressionStatementHasIncompatibleAssignment.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);

         // Toggle as desired
         //
         // Option 1: mvn exec:java shows the details of the typeproof for visual
         // inspection
         // return tests.stream();
         //
         // Option 2: test only isNotTypeSafe and show no details
         DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
         return Arrays.asList((DynamicNode) test).stream();
     }

     // ReturnStatement:void tests
     @TestFactory
     @DisplayName("Should prove type safe when return statement has compatible expression")
     Stream<DynamicNode>
     should_proveTypeSafe_when_returnStatementHasCompatibleExpression() {
         String fileName = "typeChecker/should_proveTypeSafe_when_returnStatementHasCompatibleExpression.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);
         DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
         tests.add((DynamicNode) test);
         return tests.stream();
     }

     @TestFactory
     @DisplayName("Should not prove type safe when return statement has incompatible expression")
     Stream<DynamicNode> should_notProveTypeSafe_when_returnStatementHasIncompatibleExpression() {
         String fileName = "typeChecker/should_notProveTypeSafe_when_returnStatementHasIncompatibleExpression.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);

         // Toggle as desired

         // Option 1: mvn exec:java shows the details of the typeproof for visual inspection
//          return tests.stream();

         // Option 2: test only isNotTypeSafe and show no details
         DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
         return Arrays.asList((DynamicNode) test).stream();
     }

     // PrefixExpression:boolean tests
     @TestFactory
     @DisplayName("Should prove type safe when prefix expression has not bool")
     Stream<DynamicNode> should_proveTypeSafe_when_prefixExpressionHasNotBool() {
     String fileName = "typeChecker/should_proveTypeSafe_when_prefixExpressionHasNotBool.java";
     List<DynamicNode> tests = new ArrayList<>();
     boolean isTypeSafe = getTypeChecker(fileName, tests);
     DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
     tests.add((DynamicNode) test);
     return tests.stream();
     }


     @TestFactory
     @DisplayName("Should not prove type safe when prefix expression has not int")
     Stream<DynamicNode> should_notProveTypeSafe_when_prefixExpressionHasNotInt() {
         String fileName =
         "typeChecker/should_notProveTypeSafe_when_prefixExpressionHasNotInt.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);

         // Toggle as desired
         //
         // Option 1: mvn exec:java shows the details of the typeproof for visual
         // inspection
         // return tests.stream();
         //
         // Option 2: test only isNotTypeSafe and show no details
         DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
         return Arrays.asList((DynamicNode) test).stream();
     }

     // InfixExpression:int tests
     @TestFactory
     @DisplayName("Should prove type safe when infix expression has two int operands")
     Stream<DynamicNode> should_proveTypeSafe_when_infixExpressionHasCompatibleIntOperands() {
         String fileName = "typeChecker/should_proveTypeSafe_when_infixExpressionHasCompatibleIntOperands.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);
         DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
         tests.add((DynamicNode) test);
         return tests.stream();
     }

     // InfixExpression:boolean tests
     @TestFactory
     @DisplayName("Should prove type safe when infix expression has compatible bool operands")
     Stream<DynamicNode> should_proveTypeSafe_when_infixExpressionHasCompatibleBoolOperands() {
         String fileName = "typeChecker/should_proveTypeSafe_when_infixExpressionHasCompatibleBoolOperands.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);
         DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () ->
         assertTrue(isTypeSafe));
         tests.add((DynamicNode) test);
         return tests.stream();
     }

     // Negative test for InfixExpression (int and boolean)
     @TestFactory
     @DisplayName("Should not prove type safe when infix expression has incompatible operands")
     Stream<DynamicNode> should_notProveTypeSafe_when_infixExpressionHasIncompatibleOperands() {
         String fileName = "typeChecker/should_notProveTypeSafe_when_infixExpressionHasIncompatibleOperands.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);

         // Toggle as desired
         //
         // Option 1: mvn exec:java shows the details of the typeproof for visual
         // inspection
         // return tests.stream();
         //
         // Option 2: test only isNotTypeSafe and show no details
         DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () ->
         assertFalse(isTypeSafe));
         return Arrays.asList((DynamicNode) test).stream();
     }

     // IfStatement:void tests
     @TestFactory
     @DisplayName("Should prove type safe when if statement has compatible expression and statements")
     Stream<DynamicNode> should_proveTypeSafe_when_ifStatementHasCompatibleExpressionAndStatements() {
         String fileName = "typeChecker/should_proveTypeSafe_when_ifStatementHasCompatibleExpressionAndStatements.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);
         DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
         tests.add((DynamicNode) test);
         return tests.stream();
     }


     @TestFactory
     @DisplayName("Should not prove type safe when if statement has incompatible expression or statements")
     Stream<DynamicNode> should_notProveTypeSafe_when_ifStatementHasIncompatibleExpressionOrStatements() {
         String fileName = "typeChecker/should_notProveTypeSafe_when_ifStatementHasIncompatibleExpressionOrStatements.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);

         // Toggle as desired
         //
         // Option 1: mvn exec:java shows the details of the typeproof for visual
         // inspection
         // return tests.stream();
         //
         // Option 2: test only isNotTypeSafe and show no details
         DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
         return Arrays.asList((DynamicNode) test).stream();
     }

     // WhileStatement:void tests
     @TestFactory
     @DisplayName("Should prove type safe when while statement has compatible expression and statements")
     Stream<DynamicNode> should_proveTypeSafe_when_whileStatementHasCompatibleExpressionAndStatements() {
         String fileName = "typeChecker/should_proveTypeSafe_when_whileStatementHasCompatibleExpressionAndStatements.java";
         List<DynamicNode> tests = new ArrayList<>();
         boolean isTypeSafe = getTypeChecker(fileName, tests);
         DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
         tests.add((DynamicNode) test);
         return tests.stream();
     }


     @TestFactory
     @DisplayName("Should not prove type safe when while statement has incompatible expression or statements")
     Stream<DynamicNode> should_notProveTypeSafe_when_whileStatementHasIncompatibleExpressionOrStatements() {
     String fileName =
             "typeChecker/should_notProveTypeSafe_when_whileStatementHasIncompatibleExpressionOrStatements.java";
     List<DynamicNode> tests = new ArrayList<>();
     boolean isTypeSafe = getTypeChecker(fileName, tests);

     // Toggle as desired
     //
     // Option 1: mvn exec:java shows the details of the typeproof for visual
     // inspection
     // return tests.stream();
     //
     // Option 2: test only isNotTypeSafe and show no details
     DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
     return Arrays.asList((DynamicNode) test).stream();
     }

     /**
     * Things to test
     *
     * 1. Tests if generates the right number of tests
     * 2. Test display names
     * 3. Test assert does not throw || Have examples of non type safe code that
     fails that do throw something
     */
    @Nested
    @DisplayName("Mutation tests for type checker")
    class MutationTests {
        @Test
        @DisplayName("Test empty class mutation")
        void testEmptyClassMutation() {
            Stream<DynamicNode> testResults = should_proveTypeSafe_when_givenEmptyClass();
            List<DynamicNode> nodeList = buildListOfDynamicNodes(testResults.collect(Collectors.toList()));

            // Test num nodes
            assertEquals(4, nodeList.size());

            List<Boolean> expectedContainers = Arrays.asList(true, true, false, false);
            List<String> expectedNames = Arrays.asList("CompilationUnit :void", "class C:void", "true", "void = void");

            confirmDynamicNodes(nodeList, expectedContainers, expectedNames, null);
        }

        @Test
        @DisplayName("Test empty method mutation")
        void testEmptyMethodMutation() {
            Stream<DynamicNode> testResults = should_proveTypeSafe_when_givenEmptyMethod();
            List<DynamicNode> nodeList = buildListOfDynamicNodes(testResults.collect(Collectors.toList()));

            // Test num nodes
            assertEquals(8, nodeList.size());

            List<Boolean> expectedContainers = Arrays.asList(true, true, true, true, false, false, false, false);
            List<String> expectedNames = Arrays.asList("CompilationUnit :void", "class C:void", "method C.m:void",
                    "B0:void", "true", "void = void", "void = void", "void = void");

            confirmDynamicNodes(nodeList, expectedContainers, expectedNames, null);
        }

        @Test
        @DisplayName("Test multiple methods in a class mutation")
        void testMultiMethodsInAClassMutation() {
            Stream<DynamicNode> testResults = should_proveTypeSafe_when_multiMethodsInAClass();
            List<DynamicNode> nodeList = buildListOfDynamicNodes(testResults.collect(Collectors.toList()));

            // Test num nodes
            assertEquals(21, nodeList.size());

            List<Boolean> expectedContainers = Arrays.asList(true, true, true, true, true, true, false, false, false,
                    true, true, true, true, false, true, false, false, false, false, false, false);
            List<String> expectedNames = Arrays.asList("CompilationUnit :void", "class C:void", "method C.f1:void",
                    "B0:void", "S0:void", "a:int", "E(a) = int", "void = void", "void = void", "method C.f2:void",
                    "B0:void", "S0:void", "b:int", "E(b) = int", "2:int", "E(2) = int", "int := int", "void = void",
                    "void = void", "void,void = void", "void = void");

            confirmDynamicNodes(nodeList, expectedContainers, expectedNames, null);
        }

        @Test
        @DisplayName("Test method parameters and non void mutation")
        void testMethodParamsAndNonVoidMutation() {
            Stream<DynamicNode> testResults = should_proveTypeSafe_when_methodHasParamsAndIsNonVoid();
            List<DynamicNode> nodeList = buildListOfDynamicNodes(testResults.collect(Collectors.toList()));

            // Test num nodes
            assertEquals(16, nodeList.size());

            List<Boolean> expectedContainers = Arrays.asList(true, true, true, true, true, true, true, false, true,
                    false, false, false, false, false, false, false);
            List<String> expectedNames = Arrays.asList("CompilationUnit :void", "class C:void", "method C.m:void",
                    "B0:void", "S1:void", "S0:int", "i:int", "E(i) = int", "j:int", "E(j) = int", "int := int",
                    "int := int", "void = void", "void = void", "void = void", "void = void");

            confirmDynamicNodes(nodeList, expectedContainers, expectedNames, null);
        }

        @Test
        @DisplayName("Test extended prefix bools mutation")
        void testExtendedPrefixBoolsMutation() {
            Stream<DynamicNode> testResults = should_proveTypeSafe_when_extendedPrefixBools();
            List<DynamicNode> nodeList = buildListOfDynamicNodes(testResults.collect(Collectors.toList()));

            // Test num nodes
            assertEquals(23, nodeList.size());

            List<Boolean> expectedContainers = Arrays.asList(true, true, true, true, true, true, false, true, true,
                    true, false, false, true, false, false, true, false, false, false, false, false, false, false);
            List<String> expectedNames = Arrays.asList("CompilationUnit :void", "class C:void", "method C.m:void",
                    "B0:void", "S2:void", "b:boolean", "E(b) = boolean", "S1:boolean", "S0:boolean", "false:boolean",
                    "E(false) = boolean", "boolean := boolean", "true:boolean", "E(true) = boolean",
                    "boolean := boolean", "false:boolean", "E(false) = boolean", "boolean := boolean",
                    "boolean := boolean", "void = void", "void = void", "void = void", "void = void");
            confirmDynamicNodes(nodeList, expectedContainers, expectedNames, null);
        }

         @Test
         @DisplayName("Test valid ifs mutation")
         void testValidIfsMutation() {
             Stream<DynamicNode> testResults = should_proveTypeSafe_when_validIfs();
             List<DynamicNode> nodeList = buildListOfDynamicNodes(testResults.collect(Collectors.toList()));

             // Test num nodes
             assertEquals(34, nodeList.size());

             List<Boolean> expectedContainers = Arrays.asList(true, true, true, true, true, true, false, true, false,
                     false, true, true, false, false, true, true, true, false, true, false, false, false, true, true,
                     true, false, true, false, false, false, false, false, false, false);
             List<String> expectedNames = Arrays.asList("CompilationUnit :void", "class C:void", "method C.m:void",
                     "B3:void", "S0:void", "x:X", "E(x) = X", "null:nullType", "E(null) = nullType", "X := nullType",
                     "B2:void", "true:boolean", "E(true) = boolean", "boolean := boolean", "B0:void", "S1:void",
                     "a:int", "E(a) = int", "1:int", "E(1) = int", "int := int", "void = void", "B1:void", "S2:void",
                     "a:int", "E(a) = int", "2:int", "E(2) = int", "int := int", "void = void", "void,void = void",
                     "void = void", "void = void", "void = void");

             confirmDynamicNodes(nodeList, expectedContainers, expectedNames, null);
         }

         @Test
         @DisplayName("Test invalid if mutation")
         void testInvalidIfMutation() {
             String fileName = "typeChecker/should_notProveTypeSafe_when_invalidIfs.java";
             List<DynamicNode> tests = new ArrayList<>();
             boolean isTypeSafe = getTypeChecker(fileName, tests);
             assertFalse(isTypeSafe);
             Stream<DynamicNode> testResults = tests.stream();

             List<DynamicNode> nodeList = buildListOfDynamicNodes(testResults.collect(Collectors.toList()));

             // Test num nodes
             assertEquals(38, nodeList.size());

             List<Boolean> expectedContainers = Arrays.asList(true, true, true, true, true, true, true, false,
                     true, false, false, false, true, true, true, false, true, false, false, false, true, true,
                     true, false, true, false, false, false, true, true, false, true, false, false, false, false, false,
                     false);
             List<String> expectedNames = Arrays.asList("CompilationUnit :ERROR", "class C:ERROR", "method C.m:ERROR",
                     "B3:ERROR", "B2:ERROR", "S0:ERROR", "false:boolean", "E(false) = boolean", "2:int", "E(2) = int",
                     "boolean := int", "boolean := ERROR", "B0:ERROR", "S1:ERROR", "a:int", "E(a) = int",
                     "false:boolean", "E(false) = boolean", "int := boolean", "ERROR = void", "B1:ERROR", "S2:ERROR",
                     "b:boolean", "E(b) = boolean", "3:int", "E(3) = int", "boolean := int", "ERROR = void", "S3:ERROR",
                     "a:ERROR", "E(a) = ERROR", "2:int", "E(2) = int", "ERROR := int", "ERROR,ERROR = void",
                     "ERROR = void", "ERROR = void", "ERROR = void");
             List<Boolean> expectedThrows = Arrays.asList(false, false, false, false, false, false, false, false, false,
                     false, true, true, false, false, false, false, false, false, true, true, false, false, false,
                     false, false, false, true, true, false, false, true, false, false, true, true, true, true, true);

             confirmDynamicNodes(nodeList, expectedContainers, expectedNames, expectedThrows);
         }

         @Test
         @DisplayName("Test valid while mutation")
         void testValidWhileMutation() {
             Stream<DynamicNode> testResults = should_proveTypeSafe_when_whileStatementHasCompatibleExpressionAndStatements();
             List<DynamicNode> nodeList = buildListOfDynamicNodes(testResults.collect(Collectors.toList()));

             // Test num nodes
             assertEquals(24, nodeList.size());

             List<Boolean> expectedContainers = Arrays.asList(true, true, true, true, true, true, false, false, true,
                     true, true, false, true, true, false, true, false, false, false, false, false, false,
                     false, false);
             List<String> expectedNames = Arrays.asList("CompilationUnit :void", "class C:void", "method C.m:void",
                     "B2:void", "B1:void", "true:boolean", "E(true) = boolean", "boolean := boolean", "B0:void",
                     "S1:void", "i:int", "E(i) = int", "S0:int", "1:int", "E(1) = int", "2:int", "E(2) = int",
                     "int := int", "int := int", "void = void", "void = void", "void = void", "void = void",
                     "void = void");

             confirmDynamicNodes(nodeList, expectedContainers, expectedNames, null);
         }

        private void confirmDynamicContainer(DynamicNode n, String expectedName) {
            assertEquals(expectedName, n.getDisplayName()); // Test display name
            assertTrue(n instanceof DynamicContainer); // Test node type
        }

         private void confirmDynamicTest(DynamicNode n, String expectedName, boolean expectToThrow) {
             assertEquals(expectedName, n.getDisplayName()); // Test display name
             assertTrue(n instanceof DynamicTest); // Test node type
             DynamicTest t = (DynamicTest) n;
             if (expectToThrow) {
                Assertions.assertThrows(AssertionFailedError.class, t.getExecutable()); // Test does throw
             } else {
                assertDoesNotThrow(t.getExecutable()); // Test does not throw
             }
         }

        private void confirmDynamicNodes(List<DynamicNode> nodeList, List<Boolean> expectedContainers,
                                         List<String> expectedNames, List<Boolean> expectedThrows) {
            for (int i = 0; i < nodeList.size(); i++) {
                // Setup
                DynamicNode n = nodeList.get(i);
                boolean containerExpected = expectedContainers.get(i);
                String expectedName = expectedNames.get(i);
                // Setup for passing tests
                boolean expectToThrow = false;
                if (expectedThrows != null)
                    expectToThrow = expectedThrows.get(i);

                // Actual logic

                if (containerExpected) {    // If node is container
                    confirmDynamicContainer(n, expectedName);
                } else {    // If node is test
                    confirmDynamicTest(n, expectedName, expectToThrow);
                }
            }
        }
    }
}
