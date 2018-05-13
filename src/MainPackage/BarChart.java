package MainPackage;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 

public class BarChart extends ApplicationFrame {
   
	private ArrayList<AEInfo> ael;
	private DefaultCategoryDataset dataset;
	
   public BarChart( String applicationTitle , String chartTitle, ArrayList<AEInfo> a) {
      super( applicationTitle );    
      this.ael=a;
      JFreeChart barChart = ChartFactory.createBarChart(
         chartTitle,           
         "AE num",            
         "Nível",            
         createDataset(),          
         PlotOrientation.VERTICAL,           
         true, true, false);
         
      ChartPanel chartPanel = new ChartPanel( barChart );        
      chartPanel.setPreferredSize(new java.awt.Dimension( 1000 , 500 ) );        
      setContentPane( chartPanel ); 
   }
   
   public void addData(){
		for(AEInfo ae : ael){
			final String numero=String.valueOf(ae.getAENum());
		      dataset.addValue( ae.getNivel() , numero , "" );
		}         	   
   }
   
   private CategoryDataset createDataset( ) {
	   
      dataset = new DefaultCategoryDataset( );  
      addData();
      return dataset; 
   }
}