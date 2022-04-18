package typeChecker;

public class C {
    void m(boolean result1) {
        result1 = 1 + true;
        result1 = false < 3;
        result1 = false && 5;
    }
}
