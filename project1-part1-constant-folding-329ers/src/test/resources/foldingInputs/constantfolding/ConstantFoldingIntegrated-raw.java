package constantfolding;

public class Name {
  public void name() {
    boolean bool1 = (!true);
    boolean bool2 = (!false);
    boolean bool3 = !(true);
    boolean bool4 = !(false);
    boolean bool5 = !(!true);
    boolean bool6 = !(!false);
    boolean bool7 = !!(true);
    boolean bool8 = (!!false);

    int res1 = (1 + 2);
    int res2 = (1 + 2) + 3;

    int res3 = ((1 + 2) + 3);
    int res4 = (1 + 2) - 3;
    if (false) {
      if (2 < 1) {
        int i = 0;
        i++;
      }
    } else {
      bool1 = true;
    }

    int a = 0;
    if (true) {
      a = 5 + 12;
    }

    if (!(3 < 4)) {
      System.out.println("This will never print");
    }

    if ((5 < 4)) {
      a = 5;
    }
  }
}
