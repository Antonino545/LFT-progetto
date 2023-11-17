import java.io.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }

        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '[':
                peek = ' ';
                return Token.lpq;
            case ']':
                peek = ' ';
                return Token.rpq;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                readch(br);
                if (peek == '*') {
                    peek = ' ';
                    boolean a = false;
                    while (!a) {
                        readch(br);
                        if (peek == '*') {
                            readch(br);
                        }
                        if (peek == '/') {
                            peek = ' ';
                            a = true;
                        }
                    }
                }else if(peek == '/'){
                    readch(br);
                    while (peek!='\n'){
                    readch(br);

                }

                }
                else
                    return Word.div;

            case ';':
                peek = ' ';
                return Token.semicolon;
            case ',':
                peek = ' ';
                return Token.comma;
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
                case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }
                case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                }   else if(peek == '>') 
                        return Word.ne;
                    else{
                        return Word.lt;
                }
                case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }
                case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                    + " after | : "  + peek );
                return null;
                }

            case (char)-1:
                return new Token(Tag.EOF);

            default:

                if (Character.isLetter(peek)) {
                    String s1="";

                    while(Character.isLetter(peek)){
                        s1=s1+peek;
                        readch(br);
                    }
                    switch (s1){
                        case "assign":
                            peek = ' ';
                            return Word.assign;
                        case "to":
                            peek = ' ';
                            return Word.to;
                        case "conditional":
                            peek = ' ';
                            return Word.conditional;
                        case "option":
                            peek = ' ';
                            return Word.option;
                        case "do":
                            peek = ' ';
                            return Word.dotok;
                        case "else":
                            peek = ' ';
                            return Word.elsetok;
                        case "while":
                            peek = ' ';
                            return Word.whiletok;
                        case "begin":
                            peek = ' ';
                            return Word.begin;
                        case "end":
                            peek = ' ';
                            return Word.end;
                        case "print":
                            peek = ' ';
                            return Word.print;
                        case "read":
                            peek = ' ';
                            return Word.read;
                        default:
                            while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                                s1 = s1 + peek;
                                peek = ' ';
                                readch(br);
                            }
                            return new NumberTok(Tag.ID,s1);

                    }
                }

                else if (Character.isDigit(peek)) {
                    String s2="";
                    while (Character.isDigit(peek)){
                        s2=s2+peek;
                        readch(br);
                    }
                    return new NumberTok(Tag.NUM,s2);

                } else if (peek=='_'){
                    String s3="";
                    boolean underscore=true;
                    while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                        if (peek != '_') {
                            underscore=false;
                        }
                        s3 = s3 + peek;
                        peek = ' ';
                        readch(br);

                    }
                    if(!underscore){
                        return new NumberTok(Tag.ID,s3);
                    }
                }
        }  System.err.println("Erroneous character: "
                + peek );
        return null;
   }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/file.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }



}