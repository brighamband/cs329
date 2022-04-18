public class should_ConnectToEnd_when_ReturnInElse {
  public int name() {
    int x = 0;
    if (x < 5) {
      x = 5;
    } else {
      x++;
      return x;
      x++;
    }
    return x;
  }
}