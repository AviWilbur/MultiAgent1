
/*
 * messages communicate sending messages to each other
 */
public class Message {
	private int message;
	private static int totalConstraints=0;
	private int fromID;
	
	// a message should include information.
	// you are required to add corresponding fields and constructor parameters
	// in order to pass on that information
	
	public Message(int id,int s) {
		this.fromID = id;
		this.message = s;
	}
	
	public Message(int c) {
		totalConstraints = totalConstraints + c;
	}
	
	public int getMessage() {
		return this.message;
	}
	
	public int getTotalConstraints() {
		return totalConstraints;
	}
	
	public int fromID() {
		return this.fromID;
	}
}
