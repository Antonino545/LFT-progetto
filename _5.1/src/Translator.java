import java.io.*;

 public class  Translator {
        private final Lexer lex;
        private final BufferedReader pbr;
        private Token look;

        SymbolTable st = new SymbolTable();
        CodeGenerator code = new CodeGenerator();
        int count=0;
        int num=0;

        public Translator(Lexer l, BufferedReader br) {
            lex = l;
            pbr = br;
            move();
        }
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("Near line " + Lexer.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    public void prog() {
        if(look.tag==Tag.EOF||look.tag==Tag.ASSIGN|| look.tag==Tag.COND|| look.tag==Tag.WHILE|| look.tag==Tag.PRINT|| look.tag==Tag.READ|| look.tag=='{' ){
            int lnext_prog = code.newLabel();//lnext_prog e il label che punta alla fine del programma
            statlist(lnext_prog);// statlist e la funzione che gestisce una lista di istruzioni
            code.emitLabel(lnext_prog);// il label punta alla fine del programma quindi L0:
            match(Tag.EOF);
            try {
                code.toJasmin();
            }
            catch(java.io.IOException e) {
                System.out.println("IO error\n");
            }
        }else{
            error("Error in prog "+look.tag);
        }}

    private void statlist(int lnext_statlist) {//statlist e la funzione che gestisce una lista di istruzioni
        if(look.tag==Tag.ASSIGN|| look.tag==Tag.COND|| look.tag==Tag.WHILE|| look.tag==Tag.PRINT|| look.tag==Tag.READ|| look.tag=='{' ) {
            stat(lnext_statlist);// stat e la funzione che gestisce le istruzioni singolarmente
            statlistp();//statlistp e la funzione che gestisce una lista di istruzioni dopo il ;
        }

        else{
            error("Error in Statlist "+look.tag);
        }}

    private void statlistp() {//statlistp e la funzione che gestisce una lista di istruzioni dopo il ; e gestisce EOF
        switch (look.tag){
            case 59:// incrementa il label di 1 per puntare al nuovo Label ogni volta che fa il match di ;
                match(59);
                int label= code.newLabel();//crea un nuovo label
                code.emit(OpCode.GOto, label);// punta al nuovo label
                code.emitLabel(label);
                stat(label);
                statlistp();
                break;
            case Tag.EOF:// goto a L0 quando legge EOF
                code.emit(OpCode.GOto,0);
                break;
            case '}':
                break;
            default :
                error("Errore in statlistp "+look.tag);
        }
    }
    private void stat(int lnext_stat) {//stat e la funzione che gestisce le istruzioni
        switch (look.tag) {
            case Tag.ASSIGN://assign
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist("assign");
                break;
            case Tag.PRINT://print
                match(Tag.PRINT);
                match('[');
                exprlist("print");// exprlist e la funzione che gestisce una lista di istrizioni per il print
                match(']');
                break;
            case Tag.READ://read
                match(Tag.READ);
                match('[');
                idlist("read");
                match(']');
                break;
            case Tag.WHILE://while
                match(Tag.WHILE);
                match('(');
                int w_true = code.newLabel();//w_true e il label che punta al true
                int  w_end= code.newLabel();//w_false e il label che punta al false
                bexpr(w_true);//bexpr è la funzione che gestisce la condizione del while
                match(')');
                code.emit(OpCode.GOto, w_end);// se la condizione e false va a w_false che si trova a fine del while
                code.emitLabel(w_true);// se la condizione e true va a w_true che si trova all'inizio del while
                stat(w_end);// stat e la funzione che gestisce una singola istruzione
                code.emit(OpCode.GOto, lnext_stat);// dopo aver eseguito la singola istruzione va al inizio del while
                code.emitLabel(w_end);
                break;
            case Tag.COND://cond
                match(Tag.COND);
                int c_end =code.newLabel(); //c_end e il label che punta a fine del conditional
                match('[');
                optlist(c_end);
                match(']');
                statp(c_end);
                break;
            case '{':
                match('{');
                statlist(lnext_stat);
                match('}');
                break;

            case -1:
                break;

            default:
                error("in stat "+look.tag);
                break;
        }
    }
     public void statp(int c_end) {// statp e la funzione che gestisce la parte else ed end del conditional
         switch (look.tag) {
             case Tag.END -> {
             match(Tag.END);// dal label false o true va a quel che ce dopo il conditional
             code.emit(OpCode.GOto,c_end);
             code.emitLabel(c_end);
             }
             case Tag.ELSE -> {
             match(Tag.ELSE);
             stat(c_end);// se entrambi gli option sono falsi va al else
             code.emit(OpCode.GOto, c_end);// se entrambi gli option sono falsi va al else
             match(Tag.END);
             code.emitLabel(c_end);

             }
             default -> error("Errore in statp " + look.tag);
         }
     }
     private void optlist(int c_end) {// optlist gestisce la lista  di option nel conditional
         if(look.tag==Tag.OPTION){
             optitem(c_end);
             optlistp(c_end);
         }
         else{
             error("in optlist "+look.tag);

         }
     }

     private void optlistp(int c_end) {// optlistp gestisce la lista  di option nel conditional
         switch (look.tag){
             case Tag.OPTION:
                 optitem( c_end);
                 optlistp( c_end);
             case']':
                 break;
             default:
                 error("in optlistp "+look.tag);

         }

     }

     private void optitem(int c_end) {// optitem e la funzione che gestisce le condizione nel conditional
         if (look.tag == Tag.OPTION) {
             match(Tag.OPTION);
             match('(');
             int c_true = code.newLabel(); //c_true e il label che punta alla casistica vere del option
             int c_false = code.newLabel(); //c_false e il label che punta alla casistica falsa del option
             bexpr(c_true);// bexpr e la funzione che gestisce la condizione del option
             code.emit(OpCode.GOto, c_false);
             code.emitLabel(c_true);
             match(')');
             match(Tag.DO);
             stat(c_true);
             code.emit(OpCode.GOto, c_end);//c_end e il label che punta a fine del conditional
             code.emitLabel(c_false);

         } else {
             error("in optitem " + look.tag);
         }
     }
     private void idlist(String idlist) {// idlist e la funzione che fa l'identificazione di un id per la read o l'assign
         if (look.tag == Tag.ID) {
             String identifier = ((Word) look).lexeme;// prende il nome dell'identificatore
             int id_addr = st.lookupAddress(identifier); // cerca l'indirizzo dell'identificatore
             if (id_addr == -1) { // se non lo trova lo inserisce nella symbol table
                 id_addr = count; // e lo assegna un indirizzo
                 st.insert(identifier, count++); // e lo incrementa
             }
             match(Tag.ID);
             if(idlist.equals("read")) code.emit(OpCode.invokestatic,0);
             code.emit(OpCode.istore, id_addr);
             idlistp(idlist,id_addr);
         } else {
             error("in idlist " + look.tag);
         }
     }

    private void idlistp(String idlistp,int id_addrpass){// idListp che verifica altri identificatori nella read o assign
        switch (look.tag){
            case ',':
                match(',');
                String identifier = ((Word) look).lexeme;// prende il nome dell'identificatore
                int id_addr = st.lookupAddress(identifier); // cerca l'indirizzo dell'identificatore
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(identifier, count++);
                }
                match(Tag.ID);
                if(idlistp.equals("read")) code.emit(OpCode.invokestatic,0);
                if(idlistp.equals("assign")){
                    code.emit(OpCode.iload, id_addrpass);
                }
                code.emit(OpCode.istore, id_addr);
                idlistp(idlistp,id_addr);
                break;
            case 59:
            case Tag.EOF:
            case Tag.END:
            case '}':
            case ']':
                break;
            default:
                error(" idList "+ look.tag);

        }
    }

    private void expr() { // expr e la funzione che gestisce le operazioni
        switch (look.tag) {
            case '-' -> {
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
            }
            case '/' -> {
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
            }
            case '+' -> {
                match('+');
                match('(');
                exprlist("add");//passiamo add per il caso in cui ci siano piu di due operatori dobbiamo ripetere l'add
                match(')');
            }
            case '*' -> {
                match('*');
                match('(');
                exprlist("mul");//passiamo mul per il caso in cui ci siano piu di due operatori dobbiamo ripetere l'mul
                match(')');

            }

            case Tag.NUM -> {//num
                NumberTok numberTok = (NumberTok) look;
                num= Integer.parseInt(numberTok.num);
                match(Tag.NUM);
                code.emit(OpCode.ldc, num);

            }
            case Tag.ID -> {//id
                String identifier = ((Word) look).lexeme;
                int id_addr = st.lookupAddress(identifier);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(identifier, count++);
                }
                match(Tag.ID);
                code.emit(OpCode.iload, id_addr);

            }
            default -> error("error in expr " + look.tag);
        }

    }


    private void exprlist(String Symbol) {
        if(look.tag=='+'|| look.tag=='-'| look.tag=='/'|| look.tag=='*'|| look.tag==Tag.NUM|| look.tag==Tag.ID ) {
            expr();
            if(Symbol.equals("print")) code.emit(OpCode.invokestatic,1); // se è una print chiama la funzione print
            exprlistp( Symbol); // chiama la funzione exprlistp per vedere se ci sono altri numeri

        }
        else {
            error("errore in exprlist");
        }
    }
    private void exprlistp(String Symbol){ // exprlistp che verifica altri numeri nella add o mul o print
        switch (look.tag){
            case ',':
                match(',');
                expr();
                if(Symbol.equals("add")) code.emit(OpCode.iadd);//
                if(Symbol.equals("mul")) code.emit(OpCode.imul);
                if(Symbol.equals("print")) code.emit(OpCode.invokestatic,1);
                exprlistp(Symbol);

                break;
            case ']':
            case ')':
                break;
            default:
                error("errore in exprlistp");
        }
    }
     private void bexpr(int b_true) {// bexpr e la funzione che gestisce le condizioni
         if(look.tag== Tag.RELOP){
             String a= ((Word) look).lexeme;
             match(Tag.RELOP);
             expr();
             expr();
             switch (a) {
                 case "<" -> code.emit(OpCode.if_icmplt, b_true);//Maggiore
                 case ">" -> code.emit(OpCode.if_icmpgt, b_true);//Minore
                 case "<=" -> code.emit(OpCode.if_icmple, b_true);//Maggiore uguale
                 case ">=" -> code.emit(OpCode.if_icmpge, b_true);//Minore uguale
                 case "==" -> code.emit(OpCode.if_icmpeq, b_true);//Uguale
                 case "<>" -> code.emit(OpCode.if_icmpne, b_true);//Diverso
             }

         }else {
             error("in bexpr "+look.tag);
         }
     }



     public static void main(String[] args) {
         Lexer lex = new Lexer();
         String path = "src/W5.lft"; // il percorso del file da leggere
         try {
             BufferedReader br = new BufferedReader(new FileReader(path));
             Translator translator = new Translator(lex, br);
             translator.prog();

             br.close();
         } catch (IOException e) {e.printStackTrace();}
     }
}