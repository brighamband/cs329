method Q2(x : int, y : int) returns (big : int, small : int)
  requires x != y // Found this is needed after doing the WPC
  ensures big > small
{
  if (x > y) {
    big, small := x, y;
  } else {
    big, small := y, x;
  }
}

method main(){
  // Test x is smaller than y
  var x := 3;
  var y := 5;
  var big, small := Q2(x, y);
  assert big > small;

  // Test y is smaller than x
  x := 7;
  y := 5;
  big, small := Q2(x, y);
  assert big > small;
}