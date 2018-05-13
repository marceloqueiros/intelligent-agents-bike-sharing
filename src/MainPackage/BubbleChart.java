package MainPackage;

import java.awt.Color; 
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel; 

import org.jfree.chart.*; 
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BubbleXYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer; 
import org.jfree.data.xy.DefaultXYZDataset; 
import org.jfree.data.xy.XYZDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities;
  
public class BubbleChart extends ApplicationFrame {
	private DefaultXYZDataset dataset = new DefaultXYZDataset();
	private ArrayList<AEInfo> ael;
	private ArrayList<AUInfo> aul;
	private static final long serialVersionUID = 1L;
	private XYBubbleRenderer renderer;
	
   public BubbleChart( String title,ArrayList<AEInfo> ael,ArrayList<AUInfo> aul) {

	    super(title);
	   	this.ael=ael;
	   	this.aul=aul;
	    // Create dataset
	    XYZDataset dataset = createDataset();

	    // Create chart
	    JFreeChart chart = ChartFactory.createBubbleChart("Mapa", "X", "Y", dataset, PlotOrientation.HORIZONTAL, true, true, false);

	    
	    // Set range for X-Axis
	    XYPlot plot = chart.getXYPlot();
	    NumberAxis domain = (NumberAxis) plot.getDomainAxis();
	    domain.setRange(0, 100);

	    // Set range for Y-Axis
	    NumberAxis range = (NumberAxis) plot.getRangeAxis();
	    range.setRange(0, 100);
	    
	    //Format label
	    renderer=(XYBubbleRenderer)plot.getRenderer();
	    BubbleXYItemLabelGenerator generator=new BubbleXYItemLabelGenerator(
	        " {0}:({1},{2},{3}) ",new DecimalFormat("0"),
	        new DecimalFormat("0"),
	        new DecimalFormat("0"));
	    renderer.setBaseItemLabelGenerator(generator);
	    renderer.setBaseItemLabelsVisible(true);
	    
	    // Create Panel
	    ChartPanel panel = new ChartPanel(chart);
	    setContentPane(panel);
   }

   public synchronized void addData(){

		for(AUInfo au : aul){
			if (!au.getStatus()){
				final String numero="Nome: "+au.getName();
			    dataset.addSeries(numero, new double[][] { 
			        { au.getCoordinates()[0] }, // X-Value 
			        { au.getCoordinates()[1] }, // Y-Value 
			        { 2 }  // Z-Value 
			       });	
			}
		}	
   }
   
   public XYZDataset createDataset( ) {
	   dataset = new DefaultXYZDataset();
		for(AEInfo ae : ael){
			final String numero=String.valueOf(ae.getAENum());
		    dataset.addSeries(numero, new double[][] { 
		        { ae.getCoords()[0] }, // X-Value 
		        { ae.getCoords()[1] }, // Y-Value 
		        { ae.getAPE().getRadius() }  // Z-Value 
		       });	
		}		   
	   addData();
 
      return dataset; 
   }


}