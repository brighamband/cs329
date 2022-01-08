# Help Video

https://byu.zoom.us/rec/share/_XYvBtZ0QMsNhsY_mlAoPBjIrKBhqyytUntLEQV1aulQvHX6FKm-Fst_cJthHX_S.7e0O7ar-9A3R05zP

Passcode: N8#GAtx1

# Intro

The goal of this homework is to install the core tooling that is used throughout the semester and to learn its basic functionality: build (Maven), revision control (Git Feature Branch Workflow), code documentation (JavaDoc), lint-ing (CheckStyle), and logging (SLF4J). Many of the steps require research to complete and lack specificity. Use the TAs, Slack, and peers early rather than spend hours trying to figure something out. Tooling is a pain, but once it works, it is amazing.

At the end of this homework, a student should be able to:

   * Manage code development with Git and the feature branch workflow  
   * Configure and use Maven to manage the build lifecycle of a project
   * Write JavaDocs and run JavaDoc via Maven to generate documentation
   * Run CheckStyle via Maven to lint source code 
   * Add via Maven and then use SLF4J logging with the logback binding to the console

# Pre-requisites

This homework requires Git and Maven be available in a command line interface (CLI): `git` and `mvn`. Git is commonly available by default in Linux and Mac OSX. Windows provides several options. Maven is most easily installed with a package manager (e.g., [Homebrew Cask](https://brew.sh/) for Mac OSX). There is no single preferred shell for the CLI as long as `git` and `mvn` are runnable from the prompt.

The course does not rely on any single platform; the tools are available to Windows, OS X, and Linux. Windows users might consider using [Windows Subsystem for Linux](https://en.wikipedia.org/wiki/Windows_Subsystem_for_Linux) ([Installation guide](https://docs.microsoft.com/en-us/windows/wsl/install-win10), [Using VS Code with WSL](https://code.visualstudio.com/docs/remote/wsl)). OS X and Linux are fairly straightforward with available package managers.

This course is Java based and requires a Java 11 JDK. As with the other tools, the JDK is most easily installed with a package manager. The `javac` and `java` tools should be available from the CLI for `mvn`. 

The course is IDE agnostic so that students are free to use any preferred IDE or editor. Maven will manage the build lifecycle for the homework and projects in the CLI as mentioned previously. Students are welcome to use the GUI features in a preferred IDE, but all the grading and support is via CLI with `git` and `mvn`. Students are expected to be proficient with these tools from the CLI. General IDE issues are considered outside the scope of TA and instructor support. Students have been successful using [VS Code](https://code.visualstudio.com/), [IntelliJ](https://www.jetbrains.com/idea/), and [Eclipse](https://www.eclipse.org/downloads/packages/).

# Homework 

## Part I: Docker for Environment

**Requirement:** create a Docker container for Java development with Maven.

Containerization is critical for testing as it bridges the gab between deployment and test environments. It also provides a mechanism whereby the test and development environments are easily deployed to all developers. In the extreme, it is also the mechanism to deploy to the customer or production environments. It also worth noting that it is how most CI/CD tools deploy and test in the cloud.

Docker is fast becoming the de-factor way to specify *images* and build *containers* from images. An image is a specification for a container. A container is an instance of an image. A container ends up being a process, but that process effectively is some form of Linux distribution, depending on the image, for Docker. Good practice includes with the code the image specifications for development, test, and deploy. The image specifications are stored in the code repository and as important as the code itself.

### Docker Desktop

[Docker Desktop](https://www.docker.com/products/docker-desktop) is all that is needed to get started with Docker. There is a cask in brew for it: `$ brew cask install docker`. A few useful commands: `docker run` and `docker container ls`. 

The windows desktop version will show the containers and give some basic ability to understand the state of the container. OSX only provides the `docker` command after the app is launched.

### Java Development

There are two readily available (and easy to use) paths in Docker for Java development and test. Each is details below.

#### Maven Images for Test

It is easiest to use maven images that are publicly available. A good overview is in [Crafting the Perfect Java Docker Build Flow](https://codefresh.io/docker-tutorial/java_docker_pipeline/).

```
$ docker run --interactive --tty --rm --name hw2 --volume $(pwd):/usr/src/app --workdir /usr/src/app maven:3.8-jdk-11 mvn install
```

The `--interactive` and `--tty` make the command at the end be interactive using a console interface. The `--rm` deletes the container once the command exits. The `--volume` flag mounts the current working directory to the `/usr/src/app` mount point in the container. The host filesystem is now synced with the container at the mount point and below so it is possible to edit files and then re-run tests in the container. The `--workdir` specifies where to run the command. The image is `maven:3.6-jdk-8`. Docker automatically gets and builds a container from the image. The rest is the command to run.

Super helpful is the ability to get a shell in the container:

```
$ docker run --interactive --tty --rm --name hw2 --volume $(pwd):/usr/src/app --workdir /usr/src/app maven:3.8-jdk-11 bash
```

The shell enables the ability to run maven from the command line inside the running container. For example, it is possible to edit the files and then run `mvn test` from the shell interactively. In this interactive mode using the shell, the maven is build and reuse its cache in the container.

As the container is deleted each run, all the project dependencies are deleted as well, so running the maven command again will download all the dependencies again. This re-download to populate the maven cache can be avoided by mounting the local cache in the filesystem to the container: `--volume $(home)/.m2:/root/.m2`

For the windows folks, the environment variables and commands have to be treated differently, or use WSL. The command is as below in Powershell:

```
$ docker run --interactive --tty --rm --name hw2 --volume "${pwd}:/usr/src/app" --workdir "/usr/src/app" maven:3.8-jdk-11 mvn install
```

The paths have to be quoted as do the lists (e.g., the : joined things).

#### Visual Studio Code Connected to Docker Container

Super cool is the ability to have Visual Studio Code open a directory on the host machine in a running docker container. [Developing inside a Container](https://code.visualstudio.com/docs/remote/containers) is a good starting point. For this class, the Java container is a fine starting point. The specification for that container is added to the local directory, so the image can be changed and rebuilt. Code automatically detects changes and prompts to rebuild.

Visual Studio Code for Java development is easy to get up and going. The *Java Extension Pack*, *Maven for Java*, and *Java Test Runner* extensions cover about everything needed for code to work. Open the directory with the `pom.xml` in code for all the extensions to wake up and go. Right click on the Maven entry in Explorer gives the `mvn` targets. Hovering over tests will give `Run Test|Debug Test`. As a note, when running code in a container, the *Java Test Runner* defaults to lightweight mode. Click the beaker bottle in code to get to *Test* and there should be a button to switch to standard mode. The `Run Test|Debug Test` are not available in lightweight mode.

Install the *Remote Development* extension. In the command prompt look for *Remote-Containers*. Choose *Java* for the container. It takes some time to build the image so be patient. After that, use the terminal in VS Code to run commands or use the `mvn` targets. VS Code prompts to re-open the directory in the container whenever the directory is opened. The container preserves the `mvn` cache.

### Conclusion
Make sure to have a Docker container installed and available to use for the rest of this assignment.

## Part II: Feature Branch Workflow in Git

**Requirement:** create a feature branch for your homework submission in your local copy of the 'hw0-tooling' repository to later use in a pull request as your homework submission.

Study the [feature branch workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/feature-branch-workflow). The repository created by the invitation request from GitHub Classroom is the central repository. The main branch on the central repository is the latest state of the homework (note: the repo only contains this readme file).

Complete this part of the homework by creating a feature branch for the homework in your local repository. At the end of this homework, the local feature branch will be pushed to the central repository and then used to create a pull request. Be sure the branch names reflects its intended purpose.

Along the way, please be mindful of the commit contents and comments. Commits should be self-contained and reflect a set of changes that logically belong together. Commit messages are expected to adhere to these [commit message guidelines](https://gist.github.com/robertpainsi/b632364184e70900af4ab688decf6f53). Make sure to actually read the guidlines or you will probably lose some points on this hw0!

## Part III: Build Lifecycle Management with Maven

**Requirement:** create a `pom.xml` file for Maven (details below) such that `mvn compile` followed by [mvn exec:java](http://www.vineetmanohar.com/2009/11/3-ways-to-run-java-main-from-maven/) with the appropriate main method class specified runs the Java code as expected using Java 11.

You have been provided with code that uses the Visitor Pattern (we will discuss this topic later in class) to traverse Node subclasses and perform computation. For this assignment, the details of the code don't matter.

There are many ways to create the `pom.xml` file. [Your First Maven Project](http://tutorials.jenkov.com/maven/your-first-maven-project.html) tutorial is a good starting point. Go read it first and then come back to this assignment. [Maven Archetypes](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html) for Java make it even easier to get started. Any of the following archetypes work for the homework: [maven-archetype-quickstart-java11](https://github.com/EmilianoFraga/maven-archetype-quickstart-java11), [maven-archetype-simple](https://maven.apache.org/archetypes/maven-archetype-simple/), or [maven-archetype-quickstart](https://maven.apache.org/archetypes/maven-archetype-quickstart/) -- just make sure you replace the code file names with *your* code names. Even more archetypes can by explored with `mvn archetype:generate -Dfilter=java`. Try out a few to see what different options are given and how those affect the final `pom.xml` file.

This part of the homework is complete when there exists a `pom.xml` in the same directory as this `README.md` file that is able to build, package, and run the code. Building the code is accomplished with `mvn compile` or `mvn package`. The [mvn exec:java](http://www.vineetmanohar.com/2009/11/3-ways-to-run-java-main-from-maven/) command expects the main class method to be specified as an argument (e.g., `-Dexec.mainClass="edu.byu.cs329.hw0.Main"`). The `pom.xml` file is not required to do anything else. More actions in the build process will be added later. The provided code uses the group `edu.byu.cs329` in the `pom.xml`. Come up with a sensible name for the artifact IDs.

As a note, some Maven archetypes configure plugins in the [Plugin Management](https://maven.apache.org/pom.html#Plugin_Management) section of the `pom.xml` file. Stack Overflow has a nice discussion about [plugin management in Maven](https://stackoverflow.com/questions/10483180/what-is-pluginmanagement-in-mavens-pom-xml) that is worth reading before leaving this section of the homework. In short, the `Plugin Management` section configures plugins for builds that inherit the current `pom.xml` file (e.g., projects in sub-directories). These plugins must be referenced in some element of the current `pom.xml` to be active in the current build.

Some of the plugins in the `pom.xml` file need to be in the `reporting` tag as well as the `build` tag, especially when using the `mvn site` command.

Maven is an intimidating tool and does take some time to learn (which is some of the goal of this homework), so try some experiments and play around with it. As always, be patient and do not be afraid to ask for help!

## Part IV: Code Documentation with JavaDoc

**Requirement:** [JavaDoc](https://www.oracle.com/technetwork/java/javase/documentation/index-jsp-135444.html) the `ListNode` class in the source code, add the [Apache Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/usage.html) to the `pom.xml` file, and use Maven to generate the actual docs.

In this class, it is expected that all code is documented via [JavaDoc](https://www.oracle.com/technetwork/java/javase/documentation/index-jsp-135444.html) following the Java Software Oracle [style guide](https://www.oracle.com/technetwork/java/javase/documentation/index-137868.html). There is a good example at the end of the [style guide](https://www.oracle.com/technetwork/java/javase/documentation/index-137868.html) that represents what is expected for the course: 

  * All public classes, methods, and fields should have JavaDocs
  * Methods should minimally use the `@param` and `@return` in addition to the overview
  * Classes should minimally indicate `@author` in addition to the overview
  * The first line for any method should be a summary statement similar to a commit message

JavaDoc the `ListNode` class file in the project.

The actual docs are generated using the [Apache Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/usage.html). Configure the `pom.xml` file to generate the docs in reporting as a part of the `mvn site` build life cycle. Be sure the docs are correctly generated with no warnings or errors except for the `[WARNING] No project URL defined - decoration links will not be relativized!` which is acceptable.

As a note, the JavaDoc plugin will need to be configured for Java 11, and the [Maven Site Plugin](https://maven.apache.org/plugins/maven-site-plugin/) may be required in the `build` section of the `pom.xml` file for the `mvn site` target to complete.

## Part V: Lint-ing with CheckStyle

**Requirement:** integrate [CheckStyle](https://checkstyle.sourceforge.io/) with the [Google rule set](https://checkstyle.sourceforge.io/google_style.html) into the Mavin build life cycle using the [Maven CheckStyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/) so that the build fails in the validate phase if [CheckStyle](https://checkstyle.sourceforge.io/) does not pass.

Static code analysis is known to reduce code defects. This course uses [CheckStyle](https://checkstyle.sourceforge.io/) with the [Google rule set](https://checkstyle.sourceforge.io/google_style.html) via the [Maven CheckStyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/). [Configure](https://maven.apache.org/plugins/maven-checkstyle-plugin/usage.html) the plugin to fail the build in the [validate phase](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html) when CheckStyle does not pass.

Hints: the Checkstyle plugin must be inside the `<build>` tag of the `pom.xml` file to be run during the Maven build. Also, the `<execution>` tag must be configured to run the `check` `goal` and the `validate` `phase`. See this tutorial, section [5. Checkstyle Maven Plugin](https://www.vogella.com/tutorials/Checkstyle/article.html).

Select the [Google rule set](https://checkstyle.sourceforge.io/google_style.html) by setting the `configlocation` tag in the `configuration` section to **google_checks.xml**.

You should now be able to run Checkstyle with `mvn validate`. Fix all of the warning for `Main.java`. Ignore the missing JavaDoc warnings.

## Part VI: Logging with SLF4J and log

**Requirement:** add logging to the Java code using the [SLF4J](https://www.slf4j.org/) API via the [Log4j2](https://logging.apache.org/log4j/2.x/) binding with a configuration file for [Log4j2](https://logging.apache.org/log4j/2.x/) that outputs to console all logging on *ERROR*, *WARN*, *INFO*, *DEBUG*, and *TRACE*.

Logging is an important part of debugging and fault isolation for removing code defects. All console output related to program state should go through the logger in this course at an appropriate log level:

   * *ERROR*: designates error events that might still allow the application to continue
   * *WARN*: designates potentially harmful situations
   * *INFO*: designates high-level course grained informational messages that highlight progress
   * *DEBUG*: designates fine-grained informational events that are most useful to debugging
   * *TRACE*: designates even finer-grained informational events that the *DEBUG* level

The logger enables the programmer to output fine-grained to coarse-grained program events at appropriate log levels. A simple configuration file turns on/off log levels depending on the intended task or use of the logging.  As such, it is no longer necessary to track down and remove errant console output from operational code relating to debugging efforts---just turn off the logger.

Complete this part of the homework by adding the needed dependencies to the `pom.xml` file to use the [Log4j2](https://logging.apache.org/log4j/2.x/) implementation of [SLF4J](https://www.slf4j.org/). Configure [Log4j2](https://logging.apache.org/log4j/2.x/) to output to console from log-level *TRACE* up to log-level *ERROR* (that should cover all level defined above). Add logging to the Java code to output at all the levels.

To configure Log4j2, you need a configuration file. Create a folder `resources` in `src/main/`. Then create the `log4j2.xml` config file in the `src/main/resources` folder. Apache has [detailed documentation](https://logging.apache.org/log4j/2.x/manual/configuration.html) on how to customize the configuration file. As it is somewhat overwhelming, consider reading this article from [Stack Overflow](https://stackoverflow.com/questions/21206993/very-simple-log4j2-xml-configuration-file-using-console-and-file-appender). Note that all of the example XML files have the logging level set to *DEBUG*. This will exclude any *TRACE* logging, which must be included for full points.

Finally, add the necessary code to the `Main` class to include logging with Log4j2. You must have at least one log entry for each logging level.

## Part VII: .gitignore

**Requirement:** add a `.gitignore` file to excludes any build artifacts and IDE project artifacts from revision control.

Version control is most effective when it only tracks and reports files that are pertinent to the build and deployment of the project. It can be confusing when version control constantly reports information on non-essential files. It is expected that students include an appropriate `.gitignore` file in all project repositories to only track important files and artifacts. 

Complete this part of the homework by creating and adding a `.gitignore` file to the project (if it has yet to be added) that ignores build artifacts, IDE project files, and any other files not essential to building the project.

Hint: the `target` directory only contains files that can be regenerated by `mvn` commands.

## Part VII: Create a Pull Request

**Requirement:** push your local feature branch to the central repository and then create a [pull request](https://help.github.com/en/articles/creating-a-pull-request). Pull requests are expected to roughly follow this [style guide](https://www.braintreepayments.com/blog/effective-pull-requests-a-guide/). Be sure to directly @-reference the `cs329ta` account for notification. 

Upon uploading of your pull request, GitHub will give you a sanity check by running `mvn verify site` on your code. 
Note that passing the build *does not* mean that you will get full credit for the assignment. 
Please reread this writeup to make sure you have completed all the requirements, and refer to the grading rubric on Canvas for details on grading.

Finally, copy the URL of your pull-request and submit it for HW0 in Canvas (this is how the TAs know to grade your assignment).
