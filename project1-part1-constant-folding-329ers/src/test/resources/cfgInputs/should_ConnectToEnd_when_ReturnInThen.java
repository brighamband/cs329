public class should_ConnectToEnd_when_ReturnInThen {
  public int name() {
    int x = 0;
    if (x < 5) {
      x++;
      return x;
      x++;
    } else {
      x = 5;
    }
    return x;
  }
}