package MainPackage;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;



public class AgenteUtilizador extends Agent {
	private AUInfo info;
	private HashMap<AID,AEInfo> aelist;
	private HashMap<Double,AEInfo> incentivos;
	private AEInfo origem,destino;
	private double shortestDist;
	

	@Override
	protected void setup() {
 
		Object[] args = getArguments();
		aelist=new HashMap<AID,AEInfo>();
		
		this.incentivos=(HashMap<Double, AEInfo>) args[2];
		this.info=(AUInfo) args[0];
		int[] coords=new int[2];
		ArrayList<AEInfo> aes=(ArrayList<AEInfo>) args[1];
		for(AEInfo ae : aes){
			aelist.put(ae.getAgent(), ae);
		}
		
		Random random = new Random();
		coords[0]=random.nextInt(100 - 0 + 1) + 0;
		coords[1]=random.nextInt(100 - 0 + 1) + 0;
		origem=getClosestAE(coords);
		do{
			coords[0]=random.nextInt(100 - 0 + 1) + 0;
			coords[1]=random.nextInt(100 - 0 + 1) + 0;
			destino=getClosestAE(coords);
		}while(origem.getAENum()==destino.getAENum());
        info.setDestino(destino);
        info.setOrigem(origem);
        info.setCoordinates(origem.getCoords());

		PagarPercurso();
		
		ParallelBehaviour b= new ParallelBehaviour(this,ParallelBehaviour.WHEN_ANY) {
			@Override
			public int onEnd() {
				ACLMessage entrega=new ACLMessage(ACLMessage.INFORM);
				entrega.addReceiver(info.getDestino().getAgent());
				entrega.setContent("F");
				myAgent.send(entrega);
				info.finished();
				return 0;					
			}
		};
		
		info.setDistance(Math.sqrt(Math.pow(destino.getCoords()[0]-origem.getCoords()[0], 2)+Math.pow(destino.getCoords()[1]-origem.getCoords()[1], 2)));
		
		b.addSubBehaviour(new PercorrerPercurso(info));
		b.addSubBehaviour(new AEPing(info,aes));
		b.addSubBehaviour(new ReceberIncentivos(info,incentivos,aelist));
		
		addBehaviour(b);
		
	}
	
	public AEInfo getClosestAE (int c[]){
		int aeCoords[]=new int[2];
		double dist;
		AEInfo closest,curr;
		
		Iterator<HashMap.Entry<AID,AEInfo>> it = aelist.entrySet().iterator();
		closest=it.next().getValue();
		aeCoords=closest.getCoords();
		shortestDist=Math.sqrt((aeCoords[0]-c[0])*(aeCoords[0]-c[0])+(aeCoords[1]-c[1])*(aeCoords[1]-c[1]));
		
		while (it.hasNext()) {
			curr=it.next().getValue();
			aeCoords=curr.getCoords();
			dist=Math.sqrt((aeCoords[0]-c[0])*(aeCoords[0]-c[0])+(aeCoords[1]-c[1])*(aeCoords[1]-c[1]));
			if (dist<shortestDist){
				shortestDist=dist;
				closest=curr;
			}
		}		
		
		return closest;
	}
	
	public void PagarPercurso() {
		String paymentDetails;
		ACLMessage entry=new ACLMessage(ACLMessage.INFORM);
		entry.addReceiver(info.getOrigem().getAgent());
		entry.setContent("D-"+info.getDestino().getAENum());
		send(entry);
		
		ACLMessage msg=null;
		while(msg==null) {msg=receive();}
		paymentDetails=msg.getContent();
		info.setPreco(Double.parseDouble(paymentDetails));			
		
	}	
	
	
}
