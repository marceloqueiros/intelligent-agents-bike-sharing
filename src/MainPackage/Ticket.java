package MainPackage;

import jade.core.AID;

public class Ticket {

	private int destination;
	private AID user;
	
	public Ticket(AID a,int destination){
		this.setDestination(destination);
		setUser(a);
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public AID getUser() {
		return user;
	}

	public void setUser(AID user) {
		this.user = user;
	}
}
