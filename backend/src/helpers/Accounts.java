package helpers;

/*
 * @class Accounts
 * @desc Contains Accounts Data Structure class
 */
public class Accounts {
	public int number; // Contains Number
	public String name; // Contains Account Name
	public boolean is_active; // Is account active?
	public float balance; // Contains balance information
	public int trans_count; // Transaction count
	public boolean is_student;
	
	public float getFee(){
		return is_student ? 0.05f : 0.10f;
	}
}
 