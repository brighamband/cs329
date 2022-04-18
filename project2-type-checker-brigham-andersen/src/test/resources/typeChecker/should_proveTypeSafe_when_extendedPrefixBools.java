package typeChecker;

public class C {
    void m(boolean b) {
        b = !false && true && false;
    }
}