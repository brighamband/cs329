package typeChecker;

public class C {
  int m() {
    if (3 > true) {
      int i = 1 + 2;
    } else {
      boolean b = true;
    }
  }
}
