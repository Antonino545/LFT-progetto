import java.io.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {//read next char
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') { //skip space, tab, newline
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

            case ',':
                peek = ' ';
                return Token.comma;
            case ';':
                peek = ' ';
                return Token.semicolon;
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                 System.err.println("errore carattere dopo & : " + peek);
                    return null;
                }
                case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("errore carattere dopo | : " + peek);

                    return null;
                }
                case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                }   else if(peek == '>'){
                    peek = ' ';
                    return Word.ne;}
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
                    System.err.println("errore carattere dopo = : " + peek);

                    return null;
                }
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
                    return lexical_scan(br);
                }else if (peek == '/') {
                    while (peek != '\n'){
                    readch(br);
                    }
                    return lexical_scan(br);
                } else
                    return Word.div;


            case (char)-1:
                return new Token(Tag.EOF);

            default:

                if (Character.isLetter(peek)) {
                    String s1="";
                    while(Character.isLetter(peek)){
                        s1=s1+peek;
                        readch(br);
                    }
                    switch (s1) {
                        case "assign" -> {
                            return Word.assign;
                        }
                        case "to" -> {
                            return Word.to;
                        }
                        case "conditional" -> {
                            return Word.conditional;
                        }
                        case "option" -> {
                            return Word.option;
                        }
                        case "do" -> {
                            return Word.dotok;
                        }
                        case "else" -> {
                            return Word.elsetok;
                        }
                        case "while" -> {
                            return Word.whiletok;
                        }
                        case "begin" -> {
                            return Word.begin;
                        }
                        case "end" -> {
                            return Word.end;
                        }
                        case "print" -> {
                            return Word.print;
                        }
                        case "read" -> {
                            return Word.read;
                        }
                        default -> {
                            while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                                s1 = s1 + peek;
                                peek = ' ';
                                readch(br);
                            }
                            return new Word(Tag.ID, s1);
                        }
                    }
                }

                else if (Character.isDigit(peek)) {
                    String s2="";
                    while (Character.isDigit(peek)){
                        s2=s2+peek;
                        readch(br);
                    }
                    if(s2.charAt(0)=='0'){//se la prima cifra e 0 contralla se sia 0
                        if(!s2.equals("0")) {System.err.println(" 0 non puo essere la prima cifra di primo numero");
                            return null;
                        }
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
                        return new Word(Tag.ID,s3);
                    }else {
                        System.err.println("un indentificatore non puo essere di soli _ ") ;
                        return null;
                    }
                }
        }  System.err.println("Erroneous character: "
                + peek );
        return null;
   }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/es.lft"; // il percorso del file da leggere
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