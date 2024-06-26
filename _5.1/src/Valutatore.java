import java.io.*; 

public class Valutatore {
    private final Lexer lex;
    private final BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) { 
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
	int expr_val;
     if (look.tag == '(' || look.tag == Tag.NUM){
        expr_val = expr();
	    match(Tag.EOF);
        System.out.println(expr_val);}
        else {//error
            error(" error int start");
        }
    }

    private int expr() { 
	int term_val, exprp_val=0;
        if(look.tag==Tag.NUM||look.tag=='(') {
        term_val = term();
	    exprp_val = exprp(term_val);
            return exprp_val;
        }
        else {
            error("error in expr");
        }
    return exprp_val;

    }

    private int exprp(int exprp_i) {
	int term_val;
    int exprp_val=0 ;
        switch (look.tag) {
            case '+' -> {
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
            }
            case '-' -> {
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
            }
            case ')', Tag.EOF -> exprp_val = exprp_i;
            default -> error("Syntax error exprp");
        }
    return exprp_val;
    }

    private int term() {
        int termp_i, term_val = 0;
        if (look.tag == '(' || look.tag == Tag.NUM) {
           termp_i=fact();
           term_val=termp(termp_i);
        } else {

            error("error in  term");
        }
           return term_val;
    }
    
    private int termp(int termp_i) {
        int termp_val = 0;
        int fact_val;
        switch (look.tag) {
            case '*' -> {
                match('*');
                fact_val = fact();
                termp_val = exprp(termp_i * fact_val);
            }
            case '/' -> {
                match('/');
                fact_val = fact();
                termp_val = exprp(termp_i / fact_val);
            }
            case ')', '+', '-', Tag.EOF -> termp_val = termp_i;
            default -> error("Syntax error termp");
        }
        return termp_val;
    }
    
    private int fact() {
        int fact_val = 0;
        switch (look.tag) {
            case '(' -> {
                match('(');
                fact_val = expr();
                match(')');
            }
            case Tag.NUM -> {
                NumberTok a = (NumberTok) look;//casting
                fact_val = Integer.parseInt(a.num);
                match(Tag.NUM);
            }
            default -> error("Syntax fact");
        }
        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/es.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
