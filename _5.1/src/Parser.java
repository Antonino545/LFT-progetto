import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("Near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    public void prog() {
        if(look.tag==Tag.EOF||look.tag==Tag.ASSIGN|| look.tag==Tag.COND|| look.tag==Tag.WHILE|| look.tag==Tag.PRINT|| look.tag==Tag.READ|| look.tag=='{' ){
            statlist(); //call fuction Statlis
            match(Tag.EOF);//match EOF at the end of the file
    }else{
        error("Error in prog "+look.tag);
    }}

    private void statlist() {
        if(look.tag==Tag.ASSIGN|| look.tag==Tag.COND|| look.tag==Tag.WHILE|| look.tag==Tag.PRINT|| look.tag==Tag.READ|| look.tag=='{' ) {
            stat();
            statlistp();
        }

        else{
            error("Error in Statlist "+look.tag);
        }}

    private void statlistp() {
        switch (look.tag){
            case 59:
            match(59);
            stat();
            statlistp();
            break;
            case Tag.EOF:
            break;
            case '}':
            break;
            default :
        error("errore in statlistp "+look.tag);





    }
    }
    private void stat() {
        switch (look.tag) {
            case Tag.ASSIGN://assign
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;
            case Tag.PRINT://assign
                match(Tag.PRINT);
                match('[');
                exprlist();
                match(']');
                break;
            case Tag.READ://assign
                match(Tag.READ);
                match('[');
                exprlist();
                match(']');
                break;
            case Tag.WHILE://assign
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                stat();
                break;
            case Tag.COND://assign
                match(Tag.COND);
                match('[');
                optlist();
                match(']');
                statp();
                break;
            case '{':
                match('{');
                statlist();
                match('}');
                break;

            case -1:
                break;

            default:
                error("in stat "+look.tag);
                break;
        }
    }
    public void statp(){
    switch (look.tag){
        case Tag.END:
            match(Tag.END);
            break;
        case Tag.ELSE :
            match(Tag.ELSE);
            stat();
            match(Tag.END);
            break;
        default:
        error("in statp "+look.tag);

    }
    }
    private void optlist() {
    if(look.tag==Tag.OPTION){
        optitem();
        optlistp();}
        else{
    error("in optlist "+look.tag);

}
    }

    private void optlistp() {
        switch (look.tag){
        case Tag.OPTION:
            optitem();
            optlistp();
            case']'://verificare
                break;
            default:
            error("in optlistp "+look.tag);

        }



    }

    private void optitem() {
        switch (look.tag){
         case Tag.OPTION:
            match(Tag.OPTION);
            match('(');
            bexpr();
            match(')');
            match(Tag.DO);
            stat();
            break;

            default:
                error("in optitem "+look.tag);
        }
    }
    private void idlist() {
        if(look.tag==Tag.ID){
        match(Tag.ID);
        idlistp();
        }else {
            error("Errore in Indlist"+look.tag);
        }


    }

    private void idlistp() {
        switch (look.tag){
            case ',':
                match(',');
                match(Tag.ID);
                idlistp();
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

    private void expr() {

        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;

            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr();
                break;
            case '-':
                match('-');
                expr();
                expr();
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;

            default:
            error("error in expr "+look.tag);
                break;


        }

}
    private void bexpr() {

        if(look.tag== Tag.RELOP){
            match(Tag.RELOP);
            expr();
            expr();
        }else {
            error("in bexpr "+look.tag);
        }
    }


    private void exprlist() {
        if(look.tag=='+'|| look.tag=='-'| look.tag=='/'|| look.tag=='*'|| look.tag==Tag.NUM|| look.tag==Tag.ID ) {
            expr();
        exprlistp();}
        else {
            error("errore in exprlist");
        }
    }
    private void exprlistp() {
        switch (look.tag){
            case ',':
                match(',');
                expr();
                exprlistp();
                break;
            case ']':
            case ')':
                break;
            default:
                error("errore in exprlistp");

        }
    }


    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/es.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}