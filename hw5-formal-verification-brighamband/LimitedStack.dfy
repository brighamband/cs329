class LimitedStack {

  var arr : array<int>;    // contents
  var capacity : int;   // max number of elements in stack.
  var top : int;       // The index of the top of the stack, or -1 if the stack is empty.

  predicate isValid()
    reads `top, `capacity, `arr
  {
    -1 <= top < capacity &&
    capacity > 0 &&
    arr.Length == capacity
  }

  predicate isEmpty()
    reads `top
  {
    top == -1
  }

  predicate isFull()
    reads `top, `capacity
  {
    top == capacity - 1
  }


  constructor(c : int)
    requires c > 0
    ensures isValid()
    ensures isEmpty()
    ensures capacity == c
    ensures top == -1
    ensures fresh(arr)  // Use whenever you 'new'
  {
    capacity := c;
    arr := new int[c];
    top := -1;
  } 

  // Need for push2 to discard the oldest element
  method shift()
    requires isValid() && !isEmpty()
    ensures isValid()
    ensures forall i : int :: 0 <= i < capacity - 1 ==> arr[i] == old(arr[i + 1])   // Ensures everything shifted left
    ensures top == old(top) - 1
    modifies arr, `top
  {
    var i : int := 0;
    while (i < capacity - 1 )
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

  method push(val : int)
    requires isValid() && !isFull()
    ensures isValid()
    ensures top == old(top) + 1
    ensures val == arr[top]
    ensures forall i : int :: 0 <= i < top ==> arr[i] == old(arr[i])  // Everything before top stays the same
    modifies `top, arr
  {
    top := top + 1;
    arr[top] := val;
  }

  method push2(val : int)
    requires isValid() && isFull()
    ensures isValid() && isFull()
    ensures top == old(top)
    ensures val == arr[top]
    ensures forall i : int :: 0 <= i < capacity - 1 ==> arr[i] == old(arr[i + 1])   // Ensures everything shifted left
    modifies arr, `top
  {
    shift();
    push(val);
  }

  method peek() returns (val : int)
    requires isValid() && !isEmpty()
    ensures isValid()
    ensures val == arr[top]
  {
    val := arr[top];
  }

  method pop() returns (val : int)
    requires isValid() && !isEmpty()
    ensures isValid()
    ensures top == old(top) - 1
    ensures val == old(arr[top])
    modifies `top
  {
    val := arr[top];
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

}