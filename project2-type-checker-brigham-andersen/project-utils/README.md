# CS 329 Projects Utility Source Files

## Purpose

This repository contains boilerplate code which you may use to work on the projects.
They include logic for tasks you may do over and over again, such as reading in and compiling a Java source file,
throw a runtime exception when a precondition is not met, and retrieving a specific property of an ASTNode.

You will need to run `mvn install` on this repository for the starter code given to you in the project repositories to work.
The `mvn install` command builds and names a jar file for the project according to the `pom.xml` file and installs that jar in the local Maven cache. Run it for this project so that it is available to use in your projects.

## Understanding the Boilerplate Code

To save yourself time in the long run, understand the tools given to you in this project.
Below is a rundown of the files provided.

### ExceptionUtils.java

This contains code that can throw an exception when an unexpected behavior occurred.

### JavaSourceUtils.java

Many tests you write for your projects would require tests in the format of a Java source file.
This class contains the boilerplate code to read in and compile a Java source file to an ASTNode.

### AstNodePropertiesUtils.java

You will come to find that you would need to get a common property from an ASTNode (such as a name).
Reference the starter code to see how these methods are used.

### TreeModificationUtils.java

The first project will require you to make modifications to a current ASTNode.
Methods in this class allow you to modify the tree as represented by the ASTNode class.
