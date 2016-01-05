package agents;

import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import social_simulation_project.Order;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.Observation;
import net.sourceforge.openforecast.models.AbstractForecastingModel;
import net.sourceforge.openforecast.models.MovingAverageModel;

/**
* This class represents the forecast agent. 
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class ForecastAgent 
{
	//List with all previous orders
	private ArrayList<Order> orderHistory;	
	//DataSet used to calculate next demand, DataSets get "pushed" into the ForecastingModel
	private DataSet observedData;
	//empty Datapoint to add Observations into DataSet
    private DataPoint dp;
    //abstract Model later specified
    private AbstractForecastingModel fcModel;
    //number of last periods to consider
    private int reviewPeriod;  
    private double b;
    private int lastForecast;
    private int lastOrderPoint;

    
    public ForecastAgent(){
    	reviewPeriod = 10;	
    	this.orderHistory = new ArrayList<Order>();
    	lastForecast = 0;
    }
	/**
	   * This method calculates demand for a supply
	   * chain member
	 * @param everReceivedOrders 
	   * 
	   * @return dem random forecast.
	   */
	public int calculateDemand(ArrayList<Order> everReceivedOrders)
	{
		for (int i=0; i<everReceivedOrders.size();i++){
			orderHistory.add(everReceivedOrders.get(i));
		}
		System.out.println(orderHistory.size());
		this.observedData = new DataSet();
		createDataset();
		int output = getForecast();
		orderHistory.clear();
		return output;
	}
	
	
		/**creates the DataSet used to calculate next demand with the last "reviewPeriod" orders as data:
		 * Ticks were nothing got ordered are filled with last order
		 */
	    public void createDataset(){
	    	double d;
	    	
	    	if (orderHistory.size()<=reviewPeriod){
	    		for (int i=0; i<orderHistory.size();i++){
	    			Order order = orderHistory.get(i);
	    			double b = order.getQuantity();
	    			if (orderHistory.size()==1){
	    				dp = new Observation(b);
	    				dp.setIndependentValue("ordered at", 0);
	    				observedData.add(dp);
	    			}

	    				dp = new Observation(b);
		    			dp.setIndependentValue("ordered at", i+1);
		    			observedData.add(dp);

	    		}
	    	}else
	    	{
	    		for (int i=orderHistory.size()-reviewPeriod; i<orderHistory.size(); i++){
	    			Order order = orderHistory.get(i);
	    			double b = order.getQuantity();

	    			dp = new Observation(b);
	    			dp.setIndependentValue("ordered at", i);
	    			observedData.add(dp);

	    		}
	    	}
	    	
	    }
	    
	    // forecasts amount for next period:
	    public int getForecast(){  
	    	System.out.println(observedData.toString());
	    	if (observedData.size()==0) return lastForecast;
	    	double b=0;		//variable for output	

	    		fcModel = new MovingAverageModel(observedData.size());
	    	//initiate ForeCastingModel and commit DataSet
	    	fcModel.init(observedData);	
	    	//create empty DataSet for output
	    	DataSet nextPeriod = new DataSet();	
	    	//add an empty observation to the empty DataSet
	    	dp= new Observation(0.0);
	    	dp.setIndependentValue("ordered at", (int)RepastEssentials.GetTickCount());
	    	nextPeriod.add(dp);		
	    	//invoke forecast method, which writes the forecast into nextPeriod
	    	fcModel.forecast(nextPeriod);		
	    	Iterator it = nextPeriod.iterator();
			while(it.hasNext()){		
				DataPoint dp = (DataPoint)it.next();
				b = dp.getDependentValue(); //write forecast into output variable
			}
			System.out.println(b);
			return (int)b;
	    }
	    public void addToHistory(Order order){
			orderHistory.add(order);
		}
	    
		public int customerDemand() {

			int dem = RandomHelper.nextIntFromTo(2, 25);
			//System.out.println(dem);
			return dem;
		}
	
	/*
	 * GETTERS
	 */
	public int getOutput(){
		return (int)b;
	}
	/* 
	 * SETTERS
	 */

}