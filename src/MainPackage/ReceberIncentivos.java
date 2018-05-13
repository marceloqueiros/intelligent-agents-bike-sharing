package MainPackage;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jess.*;

public class ReceberIncentivos extends Behaviour {
	private int stride=2;
	private AUInfo aui;
	private Rete engine;
	private HashMap<AID,AEInfo> aelist;
	private HashMap<Double,AEInfo> incentivos;
	
	
	public ReceberIncentivos(AUInfo a,HashMap<Double,AEInfo> incList,HashMap<AID,AEInfo> aelist){
		aui=a;
		incentivos=incList;
		this.aelist=aelist;
	}
	
	@Override
	public void action() {
		
		double incentivo;
		ACLMessage msg=null;
		msg=myAgent.receive();
		if(msg!=null) {
			incentivo=Double.parseDouble(msg.getContent());
			ACLMessage response=msg.createReply();
			response.setPerformative(ACLMessage.INFORM);			
			
			incentivos.put(incentivo,aelist.get(msg.getSender()));
			if((incentivo/aui.getRemaining())>=aui.getPrecoPMetro() && !existeMelhor(aui.getCoordinates(),incentivo)){
				response.setContent("A"); //Aceitar
				aui.updateDestination(aelist.get(msg.getSender()));
				System.out.println(myAgent.getAID().getLocalName()+" aceitando incentivo de: "+incentivo+" proveniente de: "+msg.getSender().getLocalName());
			}else{	
				System.out.println(myAgent.getAID().getLocalName()+" rejeitando incentivo de: "+incentivo+" proveniente de: "+msg.getSender().getLocalName());
				response.setContent("R"); //Rejeitar
			}
			myAgent.send(response);
		}else{
			block();
		}
	}
	
	public boolean existeMelhor(int[] coords,Double incentivo){
		boolean res=false;
		for (HashMap.Entry<Double,AEInfo> inc : incentivos.entrySet()){
			if (inc.getKey()>=incentivo && stillAvailable(aui.getDestino(),inc.getValue().getCoords())){
				res=true;
				break;
			}
		}
		
		return res;
	}
	
	public boolean stillAvailable(AEInfo ae,int coords[]){
		boolean res=false;
		double d=0,distX,distY;
		distX=Math.abs(aui.getCoordinates()[0]-aui.getDestino().getCoords()[0]);
		distY=Math.abs(aui.getCoordinates()[0]-aui.getDestino().getCoords()[0]);
		
		while(d<distX && !res){
			d+=stride;
			res=inside(ae.getAPE(),translateCoordsX(aui.getCoordinates()));
		}		
		d=0;
		while(d<distY && !res){
			d+=stride;
			res=inside(ae.getAPE(),translateCoordsY(aui.getCoordinates()));
		}			
		return res;
	}

	public int[] translateCoordsY(int[] c){
		int nextPos[]=new int[2];
		if (Math.abs(aui.getCoordinates()[1]-aui.getDestino().getCoords()[1])>stride){
			if (aui.getCoordinates()[1]<aui.getDestino().getCoords()[1]){
				nextPos[1]=aui.getCoordinates()[1]+stride;
			}else{
				nextPos[1]=aui.getCoordinates()[1]-stride;
			}
		}else{
			nextPos[1]=aui.getDestino().getCoords()[1];
		}
		nextPos[0]=aui.getCoordinates()[0];
		return nextPos;
	}
	
	public int[] translateCoordsX(int[] c){
		int nextPos[]=new int[2];
		if (Math.abs(aui.getCoordinates()[0]-aui.getDestino().getCoords()[0])>stride){
			if (aui.getCoordinates()[0]<aui.getDestino().getCoords()[0]){
				nextPos[0]=aui.getCoordinates()[0]+stride;
			}else{
				nextPos[0]=aui.getCoordinates()[0]-stride;
			}
		}else{
			nextPos[0]=aui.getDestino().getCoords()[0];
		}
		nextPos[1]=aui.getCoordinates()[1];
		return nextPos;
	}	
	
	public boolean inside(APE ape,int coords[]){
		return ( Math.pow(coords[0] - ape.getCenterX(),2) + Math.pow(coords[1] - ape.getCenterY(),2) < Math.pow(ape.getRadius(),2) );
	}

	@Override
	public boolean done() {
		return aui.getPercentagem()==1.0;
	}
}
 

