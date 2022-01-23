# Objectives

  * Write input and output specifications in terms of *requires* and *ensures* statements
  * Derive test inputs using input partitioning
  * Derive test input using boundary value analysis
  * Derive test input using decision tables
  * Write basic black-box test reports

# Class Standards for Black-box testing

  * The specification should make clear the requires and ensures for correct output
  * The ensures classes must be checked in any test
  * Invalid input that violates any requires clause should be tested and checked that it throws a `RuntimeException`
  * Ensures only includes something about exceptions if there is a throws-clause with a non-`RuntimeException`. * Pick a [naming convention](https://dzone.com/articles/7-popular-unit-test-naming) for the tests so that they are self-documenting and be consistent with that convention.
 
 
# Notes and Advice

  * Test design should be driven by:

    1. **Traceability**: from where did this defect originate from?
    2. **Readability** are the tests self-documentating and understandable by others in a reasonbale amount of time?

  * Write test code so that it can evolve together with the program it is testing.
  * Imagine you have to assert
     
     ```java
      assert(a && b && c);
      ```
     
     In this case, the following is preferable because the failure of an individual assertion is identifiable:
     
     ```java
      assert(a);
      assert(b);
      assert(c);
      ```
      
  * Group assertions in meaningful ways. For example, imagine that the goal is to prove that two queues have the same values with the exception of the last value. A solution can factor the two assertion into a single method as follows:
      
      ```java
      @Test
      public void myAssertion(Queue a, Queue b){
        assertEquals(a.size,b.size);
        a.dequeue();
        b.dequeue();
        assertEquals(a,b);
      }
      ```
      
      The above test *assumes* that dequeue is bug free and **Queue** implements equals.

  * [JUnit 5](https://junit.org/junit5/docs/current/user-guide/) allows test-methods to fail with an exception, if it is explicitly is stated that this is expected to happen. So, if a method should throw a null-pointer exception if it gets the argument null as input, this can be tested with the test case:
  
    ```java
    @Test
    void testExpectedException() {
      Assertions.assertThrows(NullPointerException.class, () -> {
        m(null);
      });
    }
    ```
  
# Problems

1. **(30 points)** Write a specification for a **Queue** class.

  Consider the **Queue** class that implements a queue with a *rotating array*. Testing the implementation depends on an expected behavior that is defined by a specification. Any specification for testing should be *model based* and declaratively state properties relative to a *model* with a *well defined state*. The specification makes clear how the state is referenced and changed by each method. For example, in a queue, the *size()* method returns the size of the queue. The specification does not say *how that size is computed* nor does is say *how the size is represented by any implementation*. It only says that *size()* returns the number of elements in the queue and does not change the state of the queue. As such, model based specification leaves out details to how a class is implemented and rather states declaratively how the class behaves. If the goal is to formally verify that a class implements a specification, then it becomes necessary to map the actual implementation to the model state. For black-box testing, model-based specification is typically the correct level of specification. 

  The state of the model for this specification is an array. Use the below notation in the specification:

     * Elements can be compared for equality as in `x = y`
     * `Q` is the current state of the queue in any method (e.g., it is like the `this` pointer only rather than `this` it is `Q` for readability)
     * `fresh()` creates an empty queue
     * `size(Q)` returns the number of elements in the queue
     * `Q[i]` returns the element at location `i` in `Q` for `0 <= i < size(Q)`
     * `old(Q)` is the previous state of `Q` to use in `ensures` clauses (e.g., provide a means to compare the pre-method state, `old(Q)`,  to the post method state, `Q`)

  **Part A:** Write the model based specification for each method of the **Queue** class assuming the queue has unlimited capacity.

  **Part B:** Write any additional *requires* for each method such that each method is constrained so that it behaves like the model queue. In other words, identify invariants on the internal state of the rotating-queue implementation that must hold for the implementation to behave according to the queue model it is implementing. 

2. **(20 points)** Refine the specification and write tests for the method *ff* in class **Misc**.

    **Part A:** the precondition in the `@requires` clause can be weakened, meaning that it can be less restrictive in certain situations. Write that weaker clause.

    **Part B:** the postcondition in the `@ensures` clause can be strengthened, meaning that it can be more restrictive in certain situations, Write that stronger clause.

    **Part C:** Apply [black-box testing](https://en.wikipedia.org/wiki/Black-box_testing) to create and implement [JUnit 5](https://junit.org/junit5/) tests in the **FTests** class. Clearly document each test in the class. These tests should be based on the specification.

3. **(50 points)** Write the specification and tests for the **WorkSchedule** class.

    Your task is to derive test cases for a number of methods after writing their specifications from the informal prose. Analyze the specifications in order to divide the set of possible inputs into different situations. Then write a set of test cases for each method, which covers the different cases well.

    The assignment is about a class called **WorkSchedule**. The purpose of the class is to manage the schedule for the employees of a company. The time is divided in units of 1 hour and the hours are simply identified by integers (Note that this is an unrealistic simplification). For each hour, the schedule stores the number employees which is needed at that time. It could for instance be that during week-days the need is that 5 employees work at the same time, but less during nights and weekend. For each hour the schedule also stores the names of the employees who have been assigned to work at that hour.
    
    **Your task**: for each method below
    
      * Write the *requires* and *ensures* to define the domain and input space for each argument and return value
      * Divide the input space into partitions based on the *requires* and *ensures*
      * Apply [black-box testing](https://en.wikipedia.org/wiki/Black-box_testing) for **any three* of the methods using the *requires* and *ensures* clauses, input partitions, and boundary value analysis 
      
    The **WorkSchedule** class is in a jar in the project and cannot be modified. As such, but the specifications with the *requires* and *ensures* clauses for each method in the **WorkScheduleTests** class. Use [JUnit 5](https://junit.org/junit5/) for the tests and put the tests in the **WorkScheduleTests** class. Only three (any three) methods need tests. All methods need specification.
      
    Be sure tests are commented with names that describe the tests. Additionally, explain and motivate how the input space is divided.

    **Advice:**

      * The english prose for each method can be understood in many ways. Write the specification and tests as you understand the prose and not what you discover the **WorkSchedule** class to actually do.
      * Define notation for the specifications such as `ws` for the current instance of **WorkSchedule**, `size` as the size of `ws`, `old(ws)` to refer to the old version of the current instance of the **WorkSchedule**, etc.
      * Use **WorkSchedule.workingEmployees** to get scheduled employees for an hour to avoid dealing too much with the internals of **WorkSchedule.Hour** as the meaning of that class is not well defined.  
      * Stay calm and be patient. The english prose makes test hard because it is ambiguous.
     
```java
/**
 * creates a schedule which contains the hours 0,1,2,( ...,size - 1 where for
 * each hour the number of needed employees is set to zero and there are no
 * employees assigned to it
 */
 public WorkSchedule(int size) {}
 
/**
 * gives back an object of the class Hour, which has two fields: requiredNumber
 * of type int is the required number of employees working at hour time.
 * workingEmployees of type String[] is the names of the employees who have so
 * far been assigned to work at hour time.
 */
 public Hour readSchedule(int time) {}

 /**
  * sets the number of required working employees to nemployee for all hours in
  * the interval starttime to endtime.
  */
  public void setRequiredNumber(int nemployee, int starttime, int endtime) {}

 /**
  * schedules employee to work during the hours from starttime to endtime.
  */
  public boolean addWorkingPeriod(String employee, int starttime, int endtime) {}

/**
 * returns a list of all employees working at some point during the interval
 * starttime to endtime.
 */
 public String[] workingEmployees(int starttime, int endtime) {}

/**
 * returns the closest time starting from currenttime for which the required
 * amount of employees has not yet been scheduled.
 */
 public int nextIncomplete(int currenttime) {}
```

# Grading

Upon completion of the homework and uploading of your pull request, GitHub will give you a sanity check by running `mvn compile test` on your code. 
Note that passing the build *does not* mean that you will get full credit for the assignment. 
Also, your tests for `WorkSchedule` *do not* need to pass. GitHub Actions will run your code ignoring the failed tests.

Please reread this writeup to make sure you have completed all the requirements, and refer to the grading rubric on Canvas for details on grading.

# Acknowledgements

Adapted from [TDA567/DIT082, LP2, HT2015](http://www.cse.chalmers.se/edu/year/2016/course/course/TDA567_Testing_debugging_and_verification/schedule.html) by Atze van de Ploeg formerly at the University of Gothenburg, Chalmers, Department of Computer Science and Engineering.
