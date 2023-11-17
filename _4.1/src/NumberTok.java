public class NumberTok extends Token {
    public String n = "";
	public NumberTok( int tag,String s) {
		super(tag);
		n=s;
	}
	public String toString() { return "<" + tag + ", " + n + ">"; }
	public  String getnum() {return n;}

    // completare token corrispondono a numeri


}




