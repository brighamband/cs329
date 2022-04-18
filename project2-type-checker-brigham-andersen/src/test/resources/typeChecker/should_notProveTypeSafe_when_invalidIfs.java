package typeChecker;

public class C {
    void m() {
        if (false + 2) {
            int a = false;
        } else {
            boolean b = 3;
        }
        a = 2;
    }
}