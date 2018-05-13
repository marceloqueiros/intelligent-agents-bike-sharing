package MainPackage;

public class APE {
	private int centerX,centerY;
	private double radius;
	
	public APE(int cx,int cy){
		radius=25;
		centerX=cx;
		centerY=cy;
	}
	
	public int getCenterX(){
		return centerX;
	}
	
	public int getCenterY(){
		return centerY;
	}
	
	public double getRadius(){
		return radius;
	}
}
