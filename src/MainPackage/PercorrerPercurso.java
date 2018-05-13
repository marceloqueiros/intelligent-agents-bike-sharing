package MainPackage;

import java.util.concurrent.TimeUnit;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class PercorrerPercurso extends Behaviour{
	private int stride=2;
	AUInfo aui;
	
	public PercorrerPercurso(AUInfo a){
		this.aui=a;
	}
	
	@Override
	public void action() {
		int d=0;
		int nextPos[]=new int[2];
		
		if(aui.getCoordinates()[0]!=aui.getDestino().getCoords()[0]){
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
			aui.setCoordinates(nextPos);
		}else if (aui.getCoordinates()[1]!=aui.getDestino().getCoords()[1]){
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
			aui.setCoordinates(nextPos);
		}
		if (aui.getPercentagem()!=1.0) {
			block(1000);
		}
		
	}
	
	@Override
	public boolean done() {			
		return aui.getPercentagem()==1.0;
	}

}
