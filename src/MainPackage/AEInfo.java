package MainPackage;

import jade.core.AID;

public class AEInfo {
	private int AENum,coords[];
	private double precoMedio,nivel;
	private double threshold;

	private double bicInit,bicNum;
	
	private double num,sum;
	
	private AID agent;
	private APE ape;
	
	public AEInfo(int AENum,int cx,int cy,int bicInit){
		num=sum=0.0;
		this.ape=new APE(cx,cy);
		this.bicInit=bicInit;
		this.AENum=AENum;
		bicNum=bicInit;
		nivel=bicNum/bicInit;
		coords=new int[2];
		coords[0]=cx;
		coords[1]=cy;
	}
	
	public double getThreshold(){
		if (nivel==0.0){
			threshold=10000.0; // aumentar indefinidamente
		}else if (nivel>0.0&&nivel<1.0/2.0){
			threshold=precoMedio;
		}else if (nivel>=1.0/2.0 && nivel<1.0){
			threshold=precoMedio/3.0;
		}
		return threshold;
	}
	
	public synchronized int[] getCoords() {
		return coords;
	}

	public double getBicNum(){
		return bicNum;
	}
	
	public int getAENum() {
		return AENum;
	}

	public void setAgent(AID a){
		agent=a;
	}
	
	public AID getAgent(){
		return this.agent;
	}
	
	public APE getAPE(){
		return ape;
	}
	
	public synchronized double getNivel(){
		return nivel;
	}
	public synchronized void setNivel(double n){
		nivel=n;
	}	
	
	public synchronized void updateBicNum(int b){
		bicNum+=b;
		nivel=(bicNum/bicInit);
	}

	public double getPrecoMedio() {
		return precoMedio;
	}

	public void setPrecoMedio(double preco) {
		sum+=preco;
		num++;
		this.precoMedio = sum/num;
	}
}
