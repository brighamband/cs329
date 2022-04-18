package typeChecker;

public class C {
    void m(int a) {
        X x = null;
        if (true) {
            a = 1;
        } else {
            a = 2;
        }
    }
}