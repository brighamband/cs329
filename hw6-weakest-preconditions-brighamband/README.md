# Objectives

   * Prove partial correctness in programs with weakest pre-condition calculus
   * Prove programs in Dafny using weakest pre-condition calculus as a guide

# Reading

Here are Dr. Mercer's notes on Weakest Precondition Calculus:
   * [weakest-precondition.md](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/weakest-preconditions.md): all about weakest pre-condition calculus

# Acknowledgement

This homework is largely the same as [this lab exercise](http://www.cse.chalmers.se/edu/year/2016/course/course/TDA567_Testing_debugging_and_verification/Lab3.html) from Chalmers.

# Guidelines

Please follow these general guidelines for the paper-pencil proofs:  

   1. State an expression for the weakest precondition of the program
   2. Proceed to calculate the weakest precondition of the program by creating the tree
   3. State the goal of the proof: `P ==> wp(S,Q)`
   4. Proceed to prove the proof goal

The proofs can be rather lengthy. You can copy and paste to avoid excessive typing. When you are dealing with large formulas in the pre- or post-condition you can introduce names to abbreviate them. Just keep in mind that when you are applying an update to a formula you have to unfold the abbreviation first before you apply the update (i.e., you need to substitute through the entire formula).

Whenever you are asked to implement something using Dafny you should:

   1. Write a file with a *dfy* extension corresponding to the program
   2. Make sure Dafny can prove your program correct according to specification


# Problems

## 1. Branching Code Proof (25 points)

Below is the familiar Dafny program for computing an absolute value of a number

```java
method Abs(x : int) returns (y : int) 
  ensures 0 <= y;
  ensures x >= 0 ==> y == x;
  ensures x < 0  ==> y == -x;
{
  if (x < 0)
   {y := -x;}
  else
   {y := x;}
}
```

Use the weakest precondition calculus to prove that the program is correct with respect to the specification. Show the entire proof.

## 2. Complete the Specification (25 points)

Below is a small Dafny program

```java
method Q2(x : int, y : int) returns (big : int, small : int) 
  ensures big > small;
{
  if (x > y)
   {big, small := x, y;}
  else
   {big, small := y, x;}
}
```
**Part A**: Dafny will not be able to prove the above program correct because its specification is incomplete. Use weakest precondition calculus to find a suitable precondition. Show each step of the derivation. Fom there derive the missing part of the specification.  **Changing the postcondition or method body is not allowed. The precondition must come from the proof using the weakest precondition calculus.**

**Part B**: 
Show that your answer from **Part A** enables Dafny to prove the program correct.

# Submission

Submit to canvas the URL for the pull request with the Dafny models and paper-pencil proofs. The pull request should include a file **EXPLANATIONS.md** explaining your solution to each problem and how to load each into Dafny to see if it is able to prove the models correct. It should also make clear where to find the proofs. Please be organized and make it easy to find and grade each problem.
