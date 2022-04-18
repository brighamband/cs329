# Constant Propagation

This repository provides the framework to implement constant propagation. The implementation requires constant folding,
control flow graph construction, and reading definitions computation. These three components are to be implemented in
this repository in support of the final implementation of constant propagation.

This repository depends on the **project-utils** library. The library is not sufficient to meet all the needs of the
project, and it is expected to be further developed along the way. The instructions to add that library as a *git
submodule* and install it locally with `mvn` are below.  **These steps must be completed before doing anything else**.

Implementing constant propagation is divided into four distinct tasks shown below. Each of these is an assignment in the
course that is submitted with an appropriate pull-request on the repository.

1. [Constant Folding](part1-constant-folding.md) ([part1-constant-folding.md](part1-constant-folding.md))
2. [Control Flow Graphs and Reaching Definitions](part2a-cfg-rd.md) ([part2a-cfg-rd.md](part2a-cfg-rd.md))
    - [Mocking Control Flow Graphs](part2b-mocking.md) ([part2b-mocking.md](part2b-mocking.md))
3. [Constant Propagation](part3-constant-propagation.md) ([part3-constant-propagation.md](part3-constant-propagation.md))

## Adding the project-utils Library

The *project-utils* package is an incomplete set of utility code for creating *JDT DOM objects* from input Java files,
getting information in and out of a JDT DOM, changing a JDT DOM, and standardizing errors and exceptions. It is a
required dependency for the this repository (see line 34 in `pom.xml`).

The library must be added as a git submodule and installed in the local Maven cache for *constant-propagation* to build.
Access to *project-utils* must also be granted to GitHub for the CI/CD pipeline to work on pushes to the **main** branch
and pull-request.

### Adding the Git Submodule

[This blog post](https://github.blog/2016-02-01-working-with-submodules/) gives a good overview of Git
Submodules. [The official e-book for Git](https://git-scm.com/book/en/v2/Git-Tools-Submodules) also contains more
instructions and details on how to use Git Submodules.

The below steps walk through adding *project-utils* to the *constant-propagation* repository as a Git Submodule. These
steps should be completed by one person in the group on an appropriate feature branch and then merged into the **main**
branch for the rest of the group pull down.

1. Create and switch to an appropriate feature branch.
2. Accept the *project-utils* classroom invite to create a private repository for the groups version of *project-utils*.
3. Copy the URL for the newly created repository
4. In the *constant-propagation* repository, run the following command, replacing the *repository-url* with the URL from
   step 2:

   `git submodule add <repository-url> project-utils`

5. Initialize the submodule by running `git submodule update --init`.
6. Run `git status`. You will notice that there are files now ready to be committed. These files store the metadata of
   the submodules, including what commit of each submodule is pulled.
7. Commit the changes and push your feature branch to GitHub. If you are working with a partner, they can pull the
   branch and run the following to initialize the repository: `git submodule update --init`

### Maven install command

The *project-utils* library must be added to the local Maven repository for it to be visible to `mvn` to satisfy the
build dependency. In the newly created, and initialized `project-utils` subfolder, run the `mvn install` command.

The `mvn install` command builds and names a jar file for the project according to the `pom.xml` file in `project-utils`
and installs that jar in the local Maven cache.

**Anytime *project-utils* is updated, it must be installed with `mvn install` in order for the code in *
constant-propagation* to see and use the changes.**

### Updating a submodule

Git treats a submodule as its own Git repository, so the code in the `projec-utils` subfolder can be modified directly
and changes can be tracked with Git in mostly the same way as usual with a few caveates. The process is generalized in
the following:

* Make, test, commit, and push changes in the `project-utils` submodule as usual with `git` and JUnit. The `git`
  commands are relative to the folder in which they are run, so anything in the `project-utils` folder is relative to
  that local `git` repository cloned when the module was initialized. **Revisions must be push to the
  remote `project-utils` repository before the next part.**

* In the *constant-propagation* repository, the folder containing `project-utils`,
  run  `git submodule update --remote --merge`. This command updates submodule metadata to use the newly committed
  version of `project-utils`. Commit these changes to ensure the project uses the correct version of *project-utils*.

* Push the changes on *constant-propagation* to the remote repository to share the project group. Remind everyone in the
  group to run `git submodule update` in the *constant-propagation* repository to get the pushed changes on the
  submodule.

Don't forget the `mvn install` in `project-utils` in order for the code in *constant-propagation* to see and use the
changes.**

### Adding the Personal Access Token for CI/CD

The GitHub CI/CD for *constant-propagation* must have access to the named *project-utils* repository in the submodule.
The access is enabled with a *personal access token* (PAT). Creating and adding the PAT is a one time process.

1. Log in to GitHub and go to https://github.com/settings/tokens
2. Click on *"Generate new token"*. It may require the GitHub password again.
3. Enter a note for this token describing its purpose: *"CS 329 Project GitHub Access"*.
4. Change the expiration date to 90 days.
5. Under scopes, select the *"repo"* scope to allow this token to have full control of private repositories.
6. Scroll down, then click on *"Generate token"*.
7. Copy the PAT. Do not close the page until you have recorded the PAT (see next steps) because once the page is closed,
   there is no way to get a copy of the PAT again.

After generating the PAT, go to the *constant-propagation* repository on GitHub to add the token to that repository as
follows:

1. Open the GitHub repo for *constant-propagation* then click on the *Settings* tab.
2. On the left menu, select *"Secrets"*, then click on *"New repository secret"*.
3. Insert `ACCESS_TOKEN` as the secret's name. In the [GitHub workflow file](.github/workflows/maven.yml), this name is
   referenced to give GitHub Actions access to the *project-utils* private repository.
4. Paste the PAT in the *"value"* section, then click on *"Add secret"*. After doing so, GitHub stores the PAT securely
   and it is no longer human readable.

## POM Notes on testing

The `mvn test` uses the Surefire plugin to generate console reports and additional reports
in `./target/surefire-reports`. The console report extension is configured to use the `@DisplayName` for the tests and
generally works well except in the case of tests in `@Nested`, tests in `@ParameterizedTest`, or `@DynamicTest`. For
these, the console report extension is less than ideal as it does not use the `@DisplayName` all the time and
groups `@ParameterizedTest` and `@DynamicTest` into a single line report.

**Important:** `mvn test -D test=<classname>` narrows the tests to be only those in `<classname>`, so for example,
running just `ParenthesizedExpressionFoldingTests` is

```
 $ mvn test -D test=ParenthesizedExpressionFoldingTests
```

It is also possible to run a *specific* test in a test class with `mvn test -D test=<classname>#<methodname>`, so for
example, running just `should_OnlyFoldParenthesizedLiterals_when_GivenMultipleTypes`
in `ParenthesizedExpressionFoldingTests` is

```
$ mvn test -D test=ParenthesizedExpressionFoldingTests#should_OnlyFoldParenthesizedLiterals_when_GivenMultipleTypes
```

Focussing the output to just be the specific test(s) of interest greatly reduces complexity and noise in the output and
improves efficiency.

The `./target/surefire-reports/TEST-<fully qualified class name>.xml` file is the detailed report of all the tests in
the class that uses the correct `@DisplayName`. The file is very useful for isolating failed parameterized or dynamic
tests. The regular text files in the directory only show what Maven shows. That said, many IDEs present a tree view of
the tests with additional information for `@Nested`, `@ParameterizedTest`, `@DynamicTest`, `@RepeatTest`, etc. This tree
view can be generated with the JUnit `ConsoleLauncher`.

The POM in the project is setup to run
the [JUnit Platform Console Standalone](https://mvnrepository.com/artifact/org.junit.platform/junit-platform-console-standalone)
on the `mvn exec:java` goal in the build phase. The POM sets the arguments to scan for tests, `--scan-classpath`,
with `./target/test-classes` being added to the class path. You can also set the `--include-package` to contain the
tests that you want to run with `mvn exec:java`. This repository is initialized so only the tests in
the `edu.byu.cs329.constantfolding` package runs. As you work on part 2 and part 3 of this project, you would want to
modify that part of the POM file.

The equivalent command line of the default defined in the POM is:

`mvn exec:java -Dexec.mainClass=org.junit.platform.console.ConsoleLauncher -Dexec.args="--class-path=./target/test-classes --select-package=edu.byu.cs329.constantfolding"`

The above is what is run with just the command `mvn exec:java`.

**Important**: specifying `-Dexec.args` narrows the tests run to a specific class or method similar to what is possible
with `-D test` for `mvn test`. For example, to run only the `ParenthesizedExpressionFoldingTests` it goes as

```
mvn exec:java -D exec.args="--class-path=./target/test-classes --select-class=edu.byu.cs329.constantfolding.ParenthesizedExpressionFoldingTests"
```

Running a specific test method goes as, for example, `should_OnlyFoldParenthesizedLiterals_when_GivenMultipleTypes`,
goes as

```
mvn exec:java -D exec.args="--class-path=./target/test-classes --select-method=edu.byu.cs329.constantfolding.ParenthesizedExpressionFoldingTests#should_OnlyFoldParenthesizedLiterals_when_GivenMultipleTypes"
```

These arguments can be set in the POM if
desired. [Section 4.3.1](https://junit.org/junit5/docs/current/user-guide/#running-tests-console-launcher) of the JUnit
5 users lists all the options including the short form of the options: `-c` versus `--select-class`.

## Things to watch out for

For this project, you may see issues with logging or running your tests. Please note the following:

* Watch carefully what imports are added by the IDE when you use a test annotation like `@Test`. Sometimes, the IDE will
  select JUnit 4's `@Test` annotation or assertions, potentially causing some issues in your tests. Make sure the import
  for JUnit annotations and assertions are from the `org.junit.jupiter.api` package.
* Be sure the logger imports are from the `org.slf4j` package, or your logging may not work as expected.
