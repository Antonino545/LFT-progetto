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

    public void start() {
        if (look.tag == '(' || look.tag == Tag.NUM) {/*First element is "(" or "NUM"  another element is an error*/
            expr(); //call fuction expr
            match(Tag.EOF);//match EOF at the end of the file
        } else {//error
            error(" error int start");
        }
    }

    private void expr() {
        if(look.tag==Tag.NUM||look.tag=='(') {/*The element is "(" or "NUM"  another element is an error*/
            term();//call term
            exprp();// match + or -(the function ignore this element: ")" )
        }
        else {
            error("error in expr");
        }

  }

    private void exprp() {
        switch (look.tag) {
            case '+':
                match('+');
                term();
                exprp();
                break;
            case '-':
                match('-');
                term();
                exprp();
                break;
            case ')':
                break;
            case Tag.EOF:
                break;
            default:
                error("error in  exprp ");

        }

}

    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM) {/*element is "(" or "NUM"  another element is an error*/
            fact();//call which recognizes the element
            termp();// match * or / (the function ignore this element: "(,+,-" ) another element is an error
        } else {
            error("error in  term");
        }

    }
    private void termp() {
        switch (look.tag) {
            case ')':
                break;
            case '+':
                break;
            case '-':
                break;
            case '*':
                match(42);
                fact();
                termp();
                break;
            case '/':
                match(47);
                fact();
                termp();
            case Tag.EOF:
                break;
            default:
                error("error in  termp");

        }    }

        private void fact() {// match "NUM" or "("
         switch (look.tag) {
                case '(':
                match('(');
                expr();
                match(')');
                break;
                case 256:
                match(256);
                break;
                default:
                error("error in  fact");
        }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/file.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}