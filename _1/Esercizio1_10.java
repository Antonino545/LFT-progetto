public class Esercizio1_10 {
    public static boolean scan (String s) {// automa che riconosce i commenti /* */ con la possibilita di avere a prima o dopo il commento
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if(ch=='a')
                        state=0;
                    else if (ch == '/')
                        state= 1;
                    else
                        state=-1;
                    break;
                case 1:
                    if (ch == '*')
                        state= 2;
                    else
                        state=-1;
                    break;
                case 2:
                    if (ch == 'a'||ch=='/')
                        state= 2;
                    else if (ch == '*')
                        state= 3;
                    else
                        state=-1;
                    break;
                case 3:
                    if (ch == '/')
                        state= 4;
                    else if (ch=='a')
                        state= 2;


                    else if(ch=='*')
                        state=3;
                    else
                        state=-1;
                    break;
                case 4:
                    if(ch=='a')
                        state=4;
                    else
                    state=-1;
                    break;
                case 5:
                    if (ch == '/')
                        state= 4;
                    else if(ch=='*')
                        state=3;
                    else if(ch=='a')
                        state=2;
                    else
                        state=-1;
                    break;


            }
            System.out.println(state);

        }
        return state==4;
    }

    public static void main(String[] args) {

        System.out.println(scan(args[0]) ? "Okay" : "No");
    }}
