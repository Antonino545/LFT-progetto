public class NumberTok extends Token {
    public String num = "";
	public NumberTok( int tag,String s) {
		super(tag);
		num=s;
	}
	public String toString() { return "<" + tag + ", " + num + ">"; }

}




