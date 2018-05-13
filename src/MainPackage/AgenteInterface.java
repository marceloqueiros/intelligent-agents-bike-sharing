package MainPackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.jfree.ui.RefineryUtilities;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;



public class AgenteInterface extends Agent {
	
	private ArrayList<AEInfo> ael;
	private ArrayList<AUInfo> aul;
	private BarChart chart;
	@Override
	protected void setup() {
		Object[] args = getArguments();
		this.ael=(ArrayList<AEInfo>) args[0];
		this.aul=(ArrayList<AUInfo>) args[1];
	      final BarChart chart = new BarChart("Níveis", "Nível de bicicletas nas AEs",ael);
	      final BubbleChart loc = new BubbleChart("Localizações",ael,aul);
	      
	      chart.pack( );        
	      RefineryUtilities.positionFrameOnScreen(chart, 0.0, 0.5);
	      chart.setVisible( true );
	      
	      loc.setSize(1600,800);
	      loc.setLocation(1025, 500);
	      loc.setVisible(true);
	      
		addBehaviour(new CyclicBehaviour(){
			@Override
			public void action() {
					
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
				chart.addData();
				loc.addData();
			}
		});				
	}
	
}
