public class Esercizio1_8 {
    public static boolean scan(String s)
    {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if ( ch == '+' || ch == '-')
                        state = 1;
                    else if (ch =='.')
                        state = 2;
                    else if ('0'<= ch && ch <= '9')
                        state = 3;
                    else
                        state = -1;

                    break;

                case 1:
                    if ( ch == '.')
                        state = 2;
                    else if ('0'<= ch && ch <= '9')
                        state = 3;
                    else
                        state = -1;

                    break;
                case 2:
                    if ('0'<= ch && ch <= '9')
                        state = 5;
                    else
                        state = -1;

                    break;
                case 3:
                    if ( ch == '.')
                        state = 5;
                    else if (ch =='e')
                        state = 4;
                    else if ('0'<= ch && ch <= '9')
                        state = 2;
                    else
                        state = -1;

                    break;

                case 4:
                    if (('0'<= ch && ch <= '9')|| ( ch == '+' || ch == '-'))
                        state = 5;
                    else
                        state = -1;

                    break;

                case 5:
                    if ( ch == 'e')
                        state = 4;
                    else if ('0'<= ch && ch <= '9')
                        state = 5;
                    else
                        state = -1;

                    break;

            }
            System.out.println(state);
        }
        return state == 5;
    }
    public static void main(String[] args) {

        System.out.println(scan(args[0]) ? "Okay" : "No");
    }}
