public class CFGIntegration {
  public int name() {
    int x = 1 + 2;
    if (x > 3) {
      while (x < 10) {
        x = x + 1;
      }
    } else {
      x = x - 1;
    }
    return x;
  }
}