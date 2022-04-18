# Part 2b: Mocking the Control Flow Graph

**Be sure to merge the pull-request from part 2a** into the mainline branch before starting part 2b. Part 2b should be
on a **new feature branch** from the mainline at a point that includes parts 1 and 2a.

For this part of the project, you will be using *Mocking* to test the Reaching Definitions code from part 2a.

This assignment's objective is to learn to write tests that using Mocking to mock the `ControlFlowGraphBuilder` class
inside the `edu.byu.cs329.cfg` package. The tests should be written from the specification using black-box techniques.
Focus on mocking and using the Mockito verify interface.

Please keep in mind that the next part will integrate constant folding, control flow graphs, and reaching definitions to
implement constant propagation.

## Reading

See [mockito.md](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/max-blocks-mockito/mockito.md).

## Java Subset

Use the same subset of Java as before and as defined in the [Part 1 Constant Folding](part1-constant-folding).

## Interfaces

The `ControlFlowGraph` and `ReachingDefinitions` are interfaces for how to interact with the objects. For example,
reaching definitions takes a control flow graph and computes the reaching definitions for each statement in that graph.
Later, when constant propagation is implemented, for any given statement in the control flow graph, it will check to see
how many definitions reach that statement for a given variable, and if there is just one definition, and that definition
is to a literal, then the variable is replaced with the literal.

```java
x=5; // statement s0
        y=x+4; // statement s1
```

In the above example, assuming that ```reachDefs``` is an instance of something that implements
the ```ReachingDefinitions``` interface, then the reaching definitions for statement
1 ```reachDefs.getReachingDefinitions(s0)``` should return the set ```{(x, s0)}```. Notice that the set contains the
name of the variable and the statement in the control flow graph that defines that variable. This pair of name and
statement exactly match the pairs that are in the entry-sets and exit-sets for the analysis. In this
way, ```reachDefs.getReachingDefinitions(s0)``` returns the entry set for statement ```s0``` from the reaching
definitions analysis. Any implementation of the interface will need to compute from the control flow graph for a method
declaration the entry-sets in order to implement the interface. There should be an instance of
the ```ReachingDefinitions``` implementation for each ```MethodDeclaration``` in the input program.

## Assignment Requirements

1. Write a minimal set of tests for `ReachingDefinitionsBuilder` using mocks for the `ControlFlowGraph` class and the
   AST. The tests should use mocks for the `ControlFlowGraph` inputs and check the structure of
   the `ReachingDefinitions` instance in some way. There is no formal specification for guiding black-box test
   generation. Reason over shapes of control-flow graph structures and **only test interesting shapes**. There should be
   less than a handful (e.g. 3 to 6) of tests to cover **interesting shapes**.

## What to turn in?

When you are done with this assignment, create a pull request of your feature branch containing the solution. Upon
submission of your pull request, GitHub will give you a sanity check by running Maven commands that the TA would have
run to grade your assignment. Note that passing the GitHub build *does not* mean that you will get full credit for the
assignment.

Submit to Canvas the URL of the pull request.

## Testing

The easiest way to run tests is with `mvn test`. See [README.md](README.md) for details on how to select specific test
classes and test cases.

### Mocks Mocks and More Mocks

Creating the mocks for the `ControlFlowGraph` instances is tricky: one because it can be tedious, and two because it
requires also creating mocks for several different `ASTNode` types:

* `VariableDeclaration`
* `VariableDeclarationStatement`
* `Assignment`
* `ExpressionStatement`
* `SimpleName`
* `MethodDeclaration`
* `Block`

The above list is not comprehensive as it depends somewhat on the shape of the graphs being mocked, but it is a good
starting point.

The goal is to only define required `when().thenReturn()` behavior for each mock. And it is more common than uncommon to
be returning mocks for that behavior since an `ASTNode` is likely to use mocks in its own mock. Following is some brief
discussion for the less obvious `ASTNodes` to help get started.

The `ControlFlowGraph` itself needs to mock its entire interface. That includes the behavior
for `ControlFlowGraph.getSuccs(Statement)` and `ControlFlowGraph.getPreds(Statement)` for each statement in the graph.
Drawing the graph being mocked is helpful to be sure all the edges are present. Recall that `ControlFlowGraph.getEnd()`
returns the `Block` in the method declaration (see `ControlFlowGraphBuilder#Visitor.visit(MethodDeclaration)`). Using
the block in the method declaration for the end is convenient so that every return statement goes to the same place.
Remember, it is not required to look in the block as the control flow graph defines edges between statements. It is
necessary to look at each statement though to see if it generates a definition (e.g., `VariableDeclarationStatement`
and `ExpressionStatement` in the case of it wrapping an `Assignment`).

#### Variable Declaration Statement

The `VariableDeclarationStatement` has a list of *fragments* where the actual definitions
happen, `VariableDeclarationStatement.fragments()`. It returns a `List<VariableDeclaration>` instance. A fragment is
a `VariableDeclaration`. It needs a `SimpleName` to return for `VariableDeclaration.getName()`.

#### Expression Statement

An `ExpressionStatement` wraps an `Expression` that it returns with `ExpressionStatement.getExpression()`. The
interesting expression in terms of a reaching definitions analysis is `Assignment`. The `Assignment` needs an
expressions for the left-hand side with `Assignment.getLeftHandSide()`. That expression should be a `SimpleName` for
this project.

## Other Things to Consider

* Creating the mocks for the control flow graph is error prone. It is not unusual to find that the reaching definitions
  implementation is fine and rather the test failed because the mock was not correct.
* `doesDefine` in `ReachingDefinitionsBuilderTests` is easily modified to check not just for a name but for the mock
  that is the expected statement for the definition.
* `SimpleName.getIdentifier()` gives the name and is what needs to be mocked.
* Write code to create mocks for things such as `VariableDeclaration`, `VariableDeclarationStatement`, `Assignment`
  , `ExpressionStatement`, etc.
* The code for the reaching definitions analysis should be simple and may prove to be less code than the test code to
  build the mocks and define the tests.

## Rubric

| Item | Point Value |
| ------- | ----------- |
| Minimal number of tests for ```ReachingDefinitionsBuilder``` using black-box testing and mocks for the `ControlFlowGraph` | 80 |
| Consistent, readable, and descriptive grouping and naming of all tests using `@Nested` or `@Tag` along with `@DisplayName`  | 10 |
| Adherence to best practices (e.g., no errors, no warnings, documented code, well grouped commits, appropriate commit messages, etc.) | 10 | |
