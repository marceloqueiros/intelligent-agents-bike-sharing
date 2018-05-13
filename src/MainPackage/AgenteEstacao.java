package MainPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import jade.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class AgenteEstacao extends Agent {
	private AEInfo aei;
	private double precoPorDist;
	private HashMap<AID,Double> mapIncentivos;
	private HashMap<Integer,Double> distances;
	private BlockingQueue<Map.Entry<AID,Boolean>> answers;

	

	@Override
	protected void setup() {
		Object[] args = getArguments();
		
		this.precoPorDist=(Double) args[0];
		aei=(AEInfo) args[1];
		aei.setAgent(this.getAID());
		mapIncentivos=new HashMap<AID,Double>();
		this.distances=(HashMap<Integer, Double>) args[2];
		addBehaviour(new MsgHandler(aei,mapIncentivos,distances,precoPorDist));

	}
	
}
  