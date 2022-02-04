# Part 3: Constant Propagation

**Be sure to merge the pull-request from part 2** into the mainline branch before starting part 3. Part 3 should be on a **new feature branch** from the mainline at a point that includes part 2 (which includes part 1).

This part of the project brings together constant folding and reaching definitions on a control flow graph to implement constant propagation. For this part, you will be modifying and adding code to the `edu.byu.cs329.constantpropagation` package.

The first objective of this assignment is to implement a `ConstantPropagation` class that for each `MethodDeclaration` does constant propagation to simplify the code. The implementation should be constructed with an `ASTVisitor` to find and replace a variable use with a constant based on the reaching definitions analysis. The visitor only looks at a statement in isolation, and the control flow graph is used to be sure each statement is considered.

The second objective of this assignment is to create a test framework to test the implementation. That framework needs to accomplish the following:

  * Black-box functional test constant propagation
  * White-box decision coverage test constant propagation: the visitor and any code needed to implement this assignment --- use mocks where appropriate to get the needed coverage.
  * Any additional integration deemed necessary for the system

As before, the test framework should be self-documenting and make clear how the tests are organized and what part of the testing belongs too: white-box, black-box, integration.

## Reading

See [DOM-Visitor](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/DOM-Visitor/) for constant folding and [cfg-rd-lecture.md](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/cfg-rd-lecture.md).

Be sure to carefully read about [constant propagation](https://en.wikipedia.org/wiki/Constant_folding)

## Java Subset

Use the same subset of Java as defined in the [constant folding](part1-constant-folding.md).

## What is Constant Propagation?

Constant propagation replaces variables references with literals anytime only one definition reaches that reference and that definition assigns the variable to a literal. There is a nice example in the [reading](https://en.wikipedia.org/wiki/Constant_folding).

```java
int a = 30;
int b = 9 + (a + 5);
int c;

c = b + 4;
if (10 < c) {
  c = c + 10;
}
return c + (60 + a);
```

Constant folding is not able to reduce anywhere. Reaching definitions shows that only a single definition of ```a``` reaches the initializer for ```b```, and since ```a``` is assigned a literal in that definition the literal is able to replace ```a``` in the initializer. Similarly, the reference to ```a``` in the return can be replaced as well.

```java
int a = 30;
int b = 9 + (30 + 5);
int c;

c = b + 4;
if (10 < c) {
  c = c + 10;
}
return c + (60 + 30);
```

Constant folding reduces the initializer for ```b``` to a literal (the literal is promoted out of the parenthesis-expression as one of the special cases).

```java
int a = 30;
int b = 44;
int c;

c = b + 4;
if (10 < c) {
  c = c + 10;
}
return c + 90;
```

Another analysis on this new version of the code shows that only a single definition of ```b``` reaches the assignment to ```c``` before the if-statement, so that ```b``` can be replaced with the literal. No other replacements are possible.

```java
int a = 30;
int b = 44;
int c;

c = 48;
if (10 < c) {
  c = c + 10;
}
return c + 90;
```

Reaching definitions on the new code replaces the reference to ```c``` in the if-statement, and it replaces the reference to ```c``` in the body of the if-statement. It does not replace the reference to ```c``` in the return statement because two different definitions of ```c``` reach that line.

```java
int a = 30;
int b = 44;
int c;

c = 48;
if (10 < 48) {
  c = 48 + 10;
}
return c + 90;
```

Constant folding reduces the code more.

```java
int a = 30;
int b = 44;
int c;

c = 48;
c = 58;

return c + 90;
```

Another round of reaching definitions and constant folding give the final code.

```java
int a = 30;
int b = 44;
int c;

c = 48;
c = 58;

return 148;
```

The assignment does not require any further reduction, but feel free to go further if desired.

### Algorithm

For each method, repeat until no changes

  1. Constant folding
  2. Construct the control flow graph
  3. Perform reaching definitions
  4. Replace any use of a variable that has a single reaching definition that is a literal with the literal (or if there are multiple definitions that reach the use but both are the same literal)

## Project Requirements

  1. Update the project POM file as described in the *Maven Test Configuration* section below.
  2. A minimal set of black-box tests for the functionality of ```ConstantPropagation```
  3. An implementation of `ConstantPropagation`
  4. Any additional tests for white-box decision coverage (e.g., branch coverage) as reported by Jacoco for the ```ConstantPropagation``` class and any new classes created in this assignment to implement the `ConstantPropagation` class. Assertion statements are exempt from the coverage report, so if a decision is not covered, and it is not due to an assertion, then that lack of coverage must be justified in some way. You are required to modify the POM file as described in the Jacoco section of this writeup to ensure this is enforced by `mvn verify`.

It is strongly encouraged to use test driven development that writes a test, writes code to pass the test, and then repeats until the implementation is complete.

## What to turn in?

When you are done with this assignment, create a pull request of your feature branch containing the solution. Upon submission of your pull request, GitHub will give you a sanity check by running Maven commands that the TA would have run to grade your assignment. Note that passing the GitHub build *does not* mean that you will get full credit for the assignment.

Submit to Canvas the URL of the pull request.

## Maven Test Configuration

The easiest way to run tests is with `mvn test`. See [README.md](README.md) for details on how to select specific test classes and test cases.

### Running tests with `mvn exec:java`

The `POM Notes on testing` section of [the README.md](README.md) explains how the POM file configures the `mvn exec:java` command and about JUnit's console launcher. For the previous part of this project, you had updated the POM file to run tests related to that part.You will replace those lines in the POM file :

```xml
    <argument>--include-package=edu.byu.cs329.cfg</argument>
    <argument>--include-package=edu.byu.cs329.rd</argument>
```

with this:

```xml
    <argument>--include-package=edu.byu.cs329.constantpropagation</argument>
```

After making the changes and rebuilding the project with a command like `mvn clean test`, run `mvn exec:java`. The only tests that should run from that would be the ones in the `edu.byu.cs329.constantpropagation` package.

### Jacoco

Jacoco is already configured in the `pom.xml` file for this project. The configuration makes the `mvn test` command automatically generate a `./target/jacoco.exec` binary file with white-box coverage data. A human readable version of the data is created with the `mvn jacoco:report` command. This command creates the `./target/site/jacoco/index.html` report that is easily navigated to see decision (e.g., branch) coverage for `ConstantPropagation` and any new classes that are a part of this project. Coverage is based only on the defined and run tests.

To get credit for this assignment, remove the following line of code in the POM file configuring the Jacoco plugin to allow Maven fail the `verify` command if 100% decision coverage was not met:

```xml
<skip>true</skip>
```

It is permissible to modify the `ConstantPropagation` class to make it easier to achieve branch coverage. It may be necessary to use mocks to cover some of the more difficult decisions.

Some further work must take place to read the report if working in a container as the container file-system may not be visible to the browser on the host operating system. It is possible to have Jacoco generate different reports such as a CSV report that can be inspected in the text editor. See the Jacoco documentation and the GitHub workflow file for details.

## Rubric

| Item | Point Value |
| ------- | ----------- |
| Minimal black-box tests for ```ConstantPropagation``` with reasonable oracles | 75 |
| ```ConstantPropagation``` Implementation | 75 |
| Additional tests for decision coverage | 30 |
| Adherence to best practices (e.g., no errors, no warnings, documented code, well grouped commits, appropriate commit messages, etc.) | 20 |
