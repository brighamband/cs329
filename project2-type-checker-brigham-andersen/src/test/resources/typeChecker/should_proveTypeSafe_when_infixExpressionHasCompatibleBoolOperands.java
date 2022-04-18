package typeChecker;

public class C {
  void m(boolean result) {
    result = true && false;
    result = false || false;
    result = true < false;
    result = false == true;
    result = false || false && true;
    result = false < false > true;
  }
}
