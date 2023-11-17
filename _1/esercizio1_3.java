public class esercizio1_3 {
    public static void main(String[] args) {

        System.out.println(scan(args[0]) ? "Okay" : "No");
    }
    public static boolean scan(String s) {
        /*riconosce stringhe contengono un numero di matricola seguito (subito) da un cognome, dove la combinazione di
        matricola e cognome corrisponde a studenti del turno 2 o del turno 3 del laboratorio di Linguaggi
        Formali e Traduttori*/
        int state = 0;/** state rapresenta lo stato del a automa quindi q1,q2,q3...ecc*/
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                  if (ch =='1'||ch =='3'||ch =='5'||ch =='7'||ch =='9')
                        state = 1;/*si mette -1 per fare conclude il ciclo essendo che abbiamo impostato state a >=0*/
                    else if (ch =='0'||ch =='2'||ch =='4'||ch =='6'||ch =='8')
                      state = 2;/*si mette -1 per fare conclude il ciclo essendo che abbiamo impostato state a >=0*/
                  else
                      state=-1;
                    break;
                case 1:
                    if (ch =='1'||ch =='3'||ch =='5'||ch =='7'||ch =='9')
                        state = 1;/*si mette -1 per fare conclude il ciclo essendo che abbiamo impostato state a >=0*/
                    else if (ch =='0'||ch =='2'||ch =='4'||ch =='6'||ch =='8')
                        state = 2;/*si mette -1 per fare conclude il ciclo essendo che abbiamo impostato state a >=0*/
                    else if(ch >= 'L' && ch <= 'Z')
                        state =4;
                    break;
                case 2:
                    if (ch =='1'||ch =='3'||ch =='5'||ch =='7'||ch =='9')
                        state = 1;/*si mette -1 per fare conclude il ciclo essendo che abbiamo impostato state a >=0*/
                    else if (ch =='0'||ch =='2'||ch =='4'||ch =='6'||ch =='8')
                        state = 2;/*si mette -1 per fare conclude il ciclo essendo che abbiamo impostato state a >=0*/
                    else if(ch >= 'A' && ch <= 'K')
                        state =3;
                    break;
            }
        }
        return state==3||state==4;/*se l'automa si trova nello stato finale cioé 3 e restitiusce true*/
    }
}
