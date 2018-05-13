package MainPackage;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class MsgHandler extends Behaviour {

	private HashMap<AID,Double> usersInAPE;
	private BlockingQueue<Ticket> lobby;
	private BlockingQueue<AID> closeAUs;
	private AEInfo aei;
	private HashMap<Integer,Double> distances;
	private double precoPorDist;
	
	public MsgHandler (AEInfo a,HashMap<AID,Double> u,HashMap<Integer,Double> d,double ppd){
		usersInAPE=u;
		aei=a;
		distances=d;
		precoPorDist=ppd;
		lobby=new LinkedBlockingQueue<Ticket>();
	}
	
	
	@Override
	public void action() {
		Ticket t;
		String content;
		int destination;
		ACLMessage msg=null;
		msg=myAgent.receive();
		if(msg!=null){
			content=msg.getContent();
			
			if (content.equals("E")){
				System.out.println("AU entering "+myAgent.getAID().getLocalName()+"'s APE.");
				usersInAPE.put(msg.getSender(),0.0);
				if (aei.getNivel()<1.0) {myAgent.addBehaviour(new Negotiate(aei,usersInAPE,precoPorDist,msg.getSender()));block();};				
			}else if (content.equals("L")){
				System.out.println("AU leaving AE"+aei.getAENum()+"'s APE.");
				usersInAPE.remove(msg.getSender());
			}else if (content.equals("F")){
				aei.updateBicNum(1);
				if (!lobby.isEmpty()){
					t=lobby.remove();
					serveWaitingCustomers(msg.getSender(),t.getDestination());
				}
				usersInAPE.remove(msg.getSender());
			}else if (content.equals("A")){
				usersInAPE.remove(msg.getSender());
			}else if (content.equals("R")){
				if (aei.getNivel()<1.0) {myAgent.addBehaviour(new Negotiate(aei,usersInAPE,precoPorDist,msg.getSender()));block();};
			}else{
				destination=Integer.parseInt(content.split("-")[1]);
				
				if (aei.getBicNum()!=0.0){
					serveWaitingCustomers(msg.getSender(),destination);
				}else{
					lobby.add(new Ticket(msg.getSender(),destination));
				}
			}
		}else{
			block();
		}
	}


	public void serveWaitingCustomers(AID sender, int destination){
		double distance, preco;
		distance=distances.get(destination);
		preco=precoPorDist*distance;
		aei.setPrecoMedio(preco);
		
		ACLMessage payment=new ACLMessage(ACLMessage.INFORM);
		payment.addReceiver(sender);
		payment.setContent(String.valueOf(preco));
		myAgent.send(payment);				
		aei.updateBicNum(-1);
	}
	
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
