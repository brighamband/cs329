# Objectives

  * Write tests to accomplish MC/DC coverage
  * Implement a stateless PBT framework
  * Implement a stateful PBT framework
  * Generate by hand static type proof certificates for simple programs

# Reading

See the following in the [lecture notes](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/):

  * [whitebox.md](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/whitebox/white-box.md)
  * [Property based testing](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/property-based-testing/)
  * [type-checking](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/type-checking.md)

# Problems

## MC/DC Coverage

1) **(15 points)** Write a minimal number of tests to achieve MC/DC coverage for `edu.byu.cs329.mcdc.McDc.problem1`. Justify the final set of tests in the comments. Make clear in the comments how the test input gives MC/DC coverage including a table for any decisions with more than one condition.

2) **(20 points)** Write a minimal number of tests to achieve MC/DC coverage for `edu.byu.cs329.mcdc.McDc.problem2`. Justify the final set of tests in the comments. Make clear in the comments how the test input gives MC/DC coverage including a table for any decisions with more than one condition (e.g., the if-statement in the loop). What makes this problem hard is finding the inputs to exercise the needed condition values for the if-statement in the loop. Find the needed condition values, then find the input to give those values.

## Stateless PBT

3) **(20 points)** For one of the following classes, implement functions that generate random input for test cases and oracles to check outputs. Run the random tests as **parameterized tests in JUnit 5**. **Report and fix** any defects you find. Consider any of the following for creating tests:

  * Assert any ensures from the specification on the output (post-conditions holds)
  * Check for runtime exceptions (it did something unexpected)
  * Assert a point-wise equivalence to some *gold-standard* implementation (compare with a perfect oracle)
  * Check for algebraic properties (e.g. inverse, etc.)

Choose one class to test:

  * [Binary search](https://en.wikipedia.org/wiki/Binary_search_algorithm): *O(ln n)* search for sorted array. The source is located in `edu.byu.cs329.pbt.stateless.BinarySearch`. 
  * [Merge sort](https://en.wikipedia.org/wiki/Merge_sort): *O(n ln n)* algorithm to sort an input array. The source is located in `edu.byu.cs329.pbt.stateless.MergeSort`.

## Stateful PBT

4) **(20 points)** This problem is to test an implementation of a map which uses [separate chaining](https://en.wikipedia.org/wiki/Hash_table) with linked lists. As discussed in class, a point-wise comparison with an oracle is one possible way to test. Another approach is to compare two objects created with different but equivalent statements. In pseudo code it goes as follows assuming that `command1` and `command2` are different but equivalent statements:

```java
prefix = randomActions();
suffix = randomActions();

TestObject T1 = new TestObject();
TestObject T2 = new TestObject();

output1 = perform(T1, prefix) + perform(T1, command1) + perform(T1, suffix);
output2 = perform(T2, prefix) + perform(T2, command2) + perform(T2, suffix);

equals(T1, T2) && equals(output1, output2);
```

An example of equivalent, but different, statements is

```
put(Ka,Va), put(Kb,Vb) <==> put(Kb,Vb), put(Ka, Va) iff Ka != Kb
```

These statements are equivalent in that `(Ka,Va)` and `(Kb,Vb)` are in the hash table no matter which set of statements are run.

Remember that it is also possible to have an error in the implementation of the property rather than the class under test!

Write an stateful PBT for the `edu.byu.cs329.pbt.stateful.LLHashMap` implementation of a map.  Be sure to include each approach: point-wise comparison and equivalent statements. **Report and fix** any defects found. You are not allowed to change the interface. Only fix defects. 

## Type Checking

5) **(25 points)** Create a type proof for `edu.byu.cs329.pbt.stateful.LlHashTable.get`. The proof should either be in the form of a tree with indentation, as shown in class slides. Assume the existence of the environment that does the right thing. 

## Optional - Extra Credit

Practice writing type proofs for  `edu.byu.cs329.pbt.stateful.LlHashTable.equals` and `edu.byu.cs329.pbt.stateful.LlHashTable.remove`. Each one is worth an additional 10 points if completely correct. You will write a type proof for the final exam, so you might consider getting in extra practice (and credit) right now.
