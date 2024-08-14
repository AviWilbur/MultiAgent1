//Distributed constraint satisfaction problem (CSP) using multiple autonomous agents that work together while communicating through a messaging
//system. Each agent is responsible for ensuring that its assigned value is consistent with the constraints it shares with other agents, 
//meaning that the assignments do not violate any of the predefined rules or conditions. The agents communicate with one another to share their
//current assignments and check whether their combined assignments satisfy the overall constraints of the problem. The ultimate objective is
//for all agents to reach a consistent state where no constraints are violated, thereby solving the CSP collaboratively in a distributed manner.


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// extract parameters
		int n = Integer.valueOf(args[0]).intValue();
		int d = Integer.valueOf(args[1]).intValue();
		double p1 = Double.valueOf(args[2]).doubleValue();
		double p2 = Double.valueOf(args[3]).doubleValue();
		
			// generate and print CSP
		Generator gen = new Generator(n, d, p1, p2);
		
		CSP csp = gen.generateDCSP();
//				csp.print();

				// initialize mailer
		Mailer mailer = new Mailer();
		for (int i = 0; i < n; i++) {
				mailer.put(i);
			}		

				// create agents
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < n; i++) {
				// use the csp to extract the private information of each agent
			HashMap<VarTuple, ConsTable> private_information = new HashMap<VarTuple, ConsTable>();
			for (Entry<VarTuple, ConsTable> e : csp.getTable().entrySet()) {
				if (e.getKey().getI() == i || e.getKey().getJ()== i) {                                        
					private_information.put(e.getKey(), e.getValue());
					}
				}
			Thread t = new Thread(new Agent(i, mailer, private_information,n,d));
			threads.add(t);
		}

				// run agents as threads
		for (Thread t : threads) {
				t.start();
			}
	
				// wait for all agents to terminate
		for (Thread t : threads) {
				t.join();
			}
		}
}
