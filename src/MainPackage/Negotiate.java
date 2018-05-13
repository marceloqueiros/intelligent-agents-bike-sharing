package MainPackage;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Negotiate extends OneShotBehaviour {

	private HashMap<AID,Double> mapIncentivos;
	private AEInfo aei;
	private double stride;
	private boolean success=false;
	private AID au;
	
	public Negotiate(AEInfo ae,HashMap<AID,Double> m,double ppm,AID au){
		mapIncentivos=m;
		aei=ae;
		stride=2*ppm;
		this.au=au;
	}
	
	@Override
	public void action() {
		double incentivo;
		if (mapIncentivos.containsKey(au)){
			if (mapIncentivos.get(au)+stride<=aei.getThreshold()){
				
				incentivo=mapIncentivos.get(au)+stride;
				mapIncentivos.put(au,incentivo);
				
				ACLMessage entry=new ACLMessage(ACLMessage.INFORM);
				entry.addReceiver(au);
				entry.setContent(String.valueOf(incentivo));
				myAgent.send(entry);
			}
		}
	}


}
