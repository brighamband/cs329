# HW 5 - Formal Verification with Dafny

## Objectives

Learn Dafny and use it to write formal specifications strong enough to proof assertions about program behavior.

## Reading

* [Dafny Documentation](https://dafny-lang.github.io/dafny/)
* [Dafny Documentation: Tutorial](https://dafny-lang.github.io/dafny/OnlineTutorial/guide.html)
* [dafny-intro.md](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/dafny/dafny-intro.md)

## Acknowledgement

This homework is largely the same as [this lab exercise](http://www.cse.chalmers.se/edu/year/2016/course/course/TDA567_Testing_debugging_and_verification/Lab2.html) from Chalmers.

## Setup

This repository is configured to build and open a Docker container that contains the dependencies needed to run Dafny.
Below are the steps to mount this repository onto the Docker development container:

1. Ensure that Docker is running (If you run into issues in later steps, try restarting Docker)
2. Open VS Code and run the `Remote-Containers: Open Folder in Container...` command
3. Select this repository as the folder to open. VS Code will automatically build the container. This may take some time depending on your computer's download and processing speeds.
4. After building the container, you should see in the bottom left corner of VS Code that you are developing in `Dev Container: Dafny`.
5. Open the `LimitedStack.dfy` file. In the terminal, you should see Dafny being installed automatically.

Note: If you are running on a Mac that **does not** use an Intel-based chip, you may have issues running this container.
One workaround could be setting "buildKit" to false in the "Docker Engine" Preferences page. 
If the workaround does not work, please try to [install Dafny as instructed here](https://github.com/dafny-lang/dafny/wiki/INSTALL#visual-studio-code)

## Guidelines

For each problem there is a *.dfy* file that must be completed and then run with Dafny. There must be no errors in either the method specifications or the main method assertions. The starter code and desired implementations are discussed below.

## Problems

### 1. Limited Stack (30 points)  

Consider the class **LimitedStack**. A **LimitedStack** object represents a stack data-structure that is a queue following the last-in-first-out principle (LIFO). Additionally, it can only hold a maximum number of elements, given by the field *capacity*. The stack itself is represented by an array of *capacity* length. The current top of the stack is located at index *top*. If the stack is empty, *top* is *-1*. The stack should implement the following methods:

   * `constructor(c : int)` : you just need to add the contract
   * `method peek() returns (elem : int)`
   * `method push(elem : int)` : pushes an element on a **non-full** stack
   * `method pop() returns (elem : int)`
   * `method push2(elem : int)`: discards the oldest element if the stack is full and then pushes the new element (uses the provided **shift** method)

Your class should also implement the following predicates:

   * `predicate isValid()`
   * `predicate isEmpty()`
   * `predicate isFull()`

The starting point for the **LimitedStack** is below. 

``` dafny
class LimitedStack {

   var arr : array<int>;    // contents
   var capacity : int;   // max number of elements in stack.
   var top : int;       // The index of the top of the stack, or -1 if the stack is empty.


   constructor(c : int)
      . . .
   {
      capacity := c;
      arr := new int[c];
      top := -1;
   } 
   . . . 
}
```

Formally specify and implement the methods for the **LimitedStack** class in [Dafny](https://github.com/Microsoft/dafny). There are two versions of *push* that need to be specified: `push` and `push2`. `push` only works for a non-full stack and behaves as expected. `push2` works for a non-full and full stack. If the stack is full in `push2` then the oldest element is discarded using the `shift` method provided below with its specification. The following code should prove correct in a complete specification. Additional tests may be required along the way.

``` dafny
// Need for push2 to discard the oldest element
method shift()
   requires isValid() && !isEmpty()
   ensures isValid()
   ensures forall i : int :: 0 <= i < capacity - 1 ==> arr[i] == old(arr[i + 1])
   ensures top == old(top) - 1
   modifies arr, `top
{
   var i : int := 0;
   while (i < capacity - 1)
      decreases capacity - 1 - i
      invariant 0 <= i < capacity
      invariant top == old(top)
      invariant forall j : int :: 0 <= j < i ==> arr[j] == old(arr[j + 1])
      invariant forall j : int :: i <= j < capacity ==> arr[j] == old(arr[j])
   {
      arr[i] := arr[i + 1];
      i := i + 1;
   }
   top := top - 1;
}

// Feel free to add extra assertions at the end, but do NOT remove any.
method main(){
    var s := new LimitedStack(3);

    assert s.isEmpty() && !s.isFull(); 

    s.push(27);
    assert !s.isEmpty();

    var e := s.pop();
    assert e == 27;

    s.push(5);
    s.push(32);
    s.push(9);
    assert s.isFull();

    var e2 := s.pop();
    assert e2 == 9 && !s.isFull(); 
    assert s.arr[0] == 5;

    s.push(e2);
    s.push2(99);

    var e3 := s.peek();
    assert e3 == 99;
    assert s.arr[0] == 32;
                     
}
```

The class implementation, with the Dafny annotations needed to prove it correct, should be located in a `LimitedStack.dfy` file. Include comments as appropriate to understand the file.

### 2. Tokeneer (70 points)

The [Tokeneer](http://www.adacore.com/sparkpro/tokeneer) project is a famous large industrial case-study and benchmark set for software verification systems. It concerns the management of a high-security building with doors opened by fingerprint recognition.

The real system has a very large specification that is beyond the scope of this class. The goal here is to model at a high-level a super mini-version of the Tokeneer system in Dafny and verify its correctness. 

Generally speaking, a user enrolls in the system at the enrollment station and receives a token. The token stores the user's fingerprint data and the user's security clearance level. Model fingerprint data as a single integer. Also, for the model, there are only three security clearance levels: Low, Medium, and High. The enrollment station tracks all users registered in the system and prevents users from being issued more than one token at a time.

Opening the door has the user present a token to the ID station and place a finger on the fingerprint reader. The ID station checks that the user's fingerprint agrees with what is stored on the user's token and that the user has the adequate security clearance to enter the door. If a security breach is discovered (i.e., a token does not match the scanned fingerprint), then the token is invalidated immediately and the alarm sounds.

The above description in English is not exact or precise. Formally specify and implement the system in Dafny. The solution should be in a `Tokeneer.dfy` file or in a directory with several different files. Include a text file to explain the solution with its structure.

#### System components

Design the classes representing the different elements of the system:

  * Tokens delivered to users. Tokens must store their user's identifier and a security clearance. Tokens may be invalidated.
  * ID stations corresponding to a certain door with a given security level. The state of the door and the alarm can be simply represented by booleans.
  * The enrollment station that must keep track of the users who currently have a token.

The system model must include the ability to 

   * Open the door
   * Close the door
   * Issue tokens to users
   * Invalidate tokens

For the contracts, start with English descriptions and then turn those in to formal statements.
Remember that sometimes the post-conditions may depend on the input (e.g. "if the token is still valid ..., otherwise ..."). 

Be sure to add a test method to exploring various cases. Use assertions liberally to check that Dafny is able to verify the expected properties of the model.

## Submission

Create a pull-request in your repository and submit the URL to [canvas.byu.edu](http://canvas.byu.edu). Be sure the files make clear the problem that they pertain too (use directories and filenames as appropriate).
