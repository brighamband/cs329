package typeChecker;

public class C {
  int m() {
    if (true) {
      int i = 1 + 2;
    } else {
      boolean b = true;
    }
  }
}
