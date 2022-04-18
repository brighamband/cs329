public class should_ConnectToEnd_when_ReturnInWhile {
  public int name() {
    int i = 0;
    while (i > 2) {
      return i;
    }
    return -1;
  }
}