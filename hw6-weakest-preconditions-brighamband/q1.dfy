method Abs(x : int) returns (y : int)
	ensures 0 <= y
	ensures 0 <= x ==> y == x
	ensures x < 0 ==> y == -x
{
	if (x < 0) {
    y := -x;
  } else {
    y := x;
  }
}

method main(){
  // Test positive
  var input := 3;
  var res := Abs(input);
  assert res >= 0;
  assert res == input;

  // Test negative
  input := -3;
  res := Abs(input);
  assert res >= 0;
  assert res == -1 * input;
}