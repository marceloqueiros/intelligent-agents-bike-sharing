package MainPackage;

import jade.util.leap.Serializable;
import jess.Rete;

import java.util.Random;

public class AUInfo{
	private int coords[];
	private AEInfo origem,destino;
	private double preco,distance;
	private boolean moving,finished,started;
	private String name;
	
	public AUInfo (int c[],String n){
		started=false;
		moving=false;
		finished=false;
		coords=c;
		name=n;
	}
	
	public String getName(){
		return name;
	}
	
	public double getPrecoPMetro(){
		return preco/distance;
	}
	
	public double getRemaining(){
		return Math.sqrt((destino.getCoords()[0]-coords[0])*(destino.getCoords()[0]-coords[0])+(destino.getCoords()[1]-coords[1])*(destino.getCoords()[1]-coords[1]));
	}
	
	
	public void setDestino(AEInfo d){
		destino=d;
	}
	public void setOrigem(AEInfo o){
		origem=o;
	}	
	public AEInfo getDestino(){
		return this.destino;
	}
	public AEInfo getOrigem(){
		return this.origem;
	}
	public synchronized int[] getCoordinates(){
		return coords;
	}
	public boolean getMovement(){
		return moving;
	}
	public double getDistance(){
		return distance;
	}
	public void updateDestination(AEInfo d){
		destino=d;
	}
	
	public synchronized boolean getStatus(){
		return finished||!started;
	}	
	
	public synchronized void finished(){
		finished=true;
	}
	
	public void setDistance(double d){
		distance=d;
	}
	public synchronized void setCoordinates(int c[]){
		if (!started) started=true;
		coords=c;
	}
	
	public void setMoving(boolean b){
		moving=b;
	}
	
	public synchronized double getPercentagem(){
		int ocoords[],dcoords[];
		
		ocoords=origem.getCoords();
		dcoords=destino.getCoords();
		double dist=Math.sqrt((coords[0]-ocoords[0])*(coords[0]-ocoords[0])+(coords[1]-ocoords[1])*(coords[1]-ocoords[1]));
		double fullDist=Math.sqrt((dcoords[0]-ocoords[0])*(dcoords[0]-ocoords[0])+(dcoords[1]-ocoords[1])*(dcoords[1]-ocoords[1]));
		
		return Math.abs(dist/fullDist);
	}
	
	public void setPreco(double p){
		preco=p;
	}
	
}
