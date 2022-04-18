
public class MaxBlocks {

  public void p(int x, int y) {
    x = x + 1;
    y = y + x;
    while(x < 10) {
      y = y + x;
      x = x + 1;
      if (y * 10) {
        y = 2 * y;
      }
    }
  }
}
