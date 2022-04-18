package typeChecker;

public class C {
  void m(int result) {
    result = 1 + 2;
    result = 2 * 3;
    result = 5 - 2;
    result = 1 + 2 + 3;
    result = 2 * 3 * 5;
    result = 7 - 1 - 2;
  }
}
