public class esercizio1_2 {

    public static void main(String[] args) {

        System.out.println(scan(args[0]) ? "Okay" : "No");
    }
    public static boolean scan(String s) {//restituisce vero se riconosce un identificatore
        int state = 0;/** state rapresenta lo stato del a automa quindi q1,q2,q3...ecc*/
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') )
                        state = 2;
                    else if (ch == '_')
                        state = 1;
                    else  if (ch >= '0' && ch <= '9')
                        state = -1;/*si mette -1 per fare conclude il ciclo essendo che abbiamo impostato state a >=0*/
                    break;
                case 1:
                    if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') ||(ch >= '0' && ch <= '9'))
                        state = 2;
                    else if (ch == '_')
                        state = 1;

                    break;

            }
        }
        return state==2;
    }


}
