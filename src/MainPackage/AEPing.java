package MainPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AEPing extends Behaviour {

	private AUInfo aui;
	private HashMap<AEInfo,Boolean> ael; 
		
	
	public AEPing(AUInfo a,ArrayList<AEInfo> al){
		aui=a;
		ael=new HashMap<AEInfo,Boolean>();
		for (AEInfo ae : al) {
		   ael.put(ae,false);
		}
	}
	
	@Override
	public void action() {
		double thrsh=3.0/4.0;
		if(aui.getPercentagem()>thrsh){
		//send ping to AE when entering or leaving APE
		
			for(AEInfo ae : ael.keySet()){
				if (ae.getAENum()!=aui.getDestino().getAENum()&&ae.getAENum()!=aui.getOrigem().getAENum()){
					ACLMessage ping=new ACLMessage(ACLMessage.INFORM);
					ping.addReceiver(ae.getAgent());
					if (inside(ae.getAPE(),aui.getCoordinates())&&(!ael.get(ae))){
						ping.setContent("E"); //meaning agent is entering APE
						myAgent.send(ping);
						ael.put(ae,true);
					}else if(!inside(ae.getAPE(),aui.getCoordinates())&&(ael.get(ae))){
						ping.setContent("L"); //meaning agent is leaving APE
						myAgent.send(ping);
						ael.put(ae,false);
					}
				}
			}
		}
		block(1000);
	}

	public boolean inside(APE ape,int coords[]){
		return ( Math.pow(coords[0] - ape.getCenterX(),2) + Math.pow(coords[1] - ape.getCenterY(),2) < Math.pow(ape.getRadius(),2) );
	}

	@Override
	public boolean done() {
		return aui.getPercentagem()==1.0;
	}
}
