
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public class Agent implements Runnable {

	private int id,assignment,lastAgent;
	private int loop,consistency = 0;
	private int counter = -1;
	private Mailer mailer;
	private HashMap<VarTuple, ConsTable> constraints;
	private Message myMessage,mail;
	
	/*
	 * constructor parameters -
	 * agent's id
	 * a reference to mailer
	 * a reference to csp
	 */
	public Agent(int id, Mailer mailer, HashMap<VarTuple, ConsTable> constraints,int lastAgent,int domainSize) {
		this.id = id;
		this.mailer = mailer;
		this.constraints = constraints;
		this.lastAgent = lastAgent -1;
		Random r = new Random();
		this.assignment = r.nextInt((domainSize));
		myMessage(this.id,assignment);
		this.mailer.put(id);
	}
	
	public void myMessage(int id,int s) {
		this.myMessage = new Message(id,s);
	}

	@Override
	public void run() {
		for (Entry<VarTuple, ConsTable> e : constraints.entrySet()) {
			if(e.getKey().getI()== id) {
				this.mailer.send(e.getKey().getJ(),myMessage);
			}else {
				this.mailer.send(e.getKey().getI(),myMessage);
			}
		}
		while(loop<constraints.size()) {
			this.mail = this.mailer.readOne(id);
			if(mail != null) {
				loop++;
				for (Entry<VarTuple, ConsTable> t : constraints.entrySet()) {
					if(mail.fromID() == t.getKey().getJ()) {
						if(t.getValue().getConsTable(assignment,mail.getMessage())) {
							consistency++;
						}
				}	if(mail.fromID() == t.getKey().getI()) {
						if(t.getValue().getConsTable(mail.getMessage(),assignment)) {
							consistency++;
						}
				    }
			   }
		    }
		}
		System.out.println("ID: "+ id +", assignment: " + assignment +", successful constraint checks: " + consistency);
		this.mail = new Message(consistency);
		this.mailer.send(lastAgent, mail);
		if (id == lastAgent) {
			while(counter != lastAgent) {
				this.mail = this.mailer.readOne(lastAgent);
				if(mail != null) {
					counter++;
				}	
			}
		}
		if (counter == lastAgent) {
			System.out.println("total number of constraint checks: " + mail.getTotalConstraints());
		}
	}
}
