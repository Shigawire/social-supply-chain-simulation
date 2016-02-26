package agents;

import java.util.ArrayList;
import java.util.Map;

import SimulationSetups.TrustSetter;

/**
* This class represents a procurement agent.
* He is responsible for choosing the supplier.
*
* @author  PS Development Team
* @since   2015-12-05
*/
public class ProcurementAgent 
{	
	private ArrayList<DeliveryAgent> deliveryAgents;
	private TrustAgent trustAgent;
	private double trustRelevance = 1;
	private double averageDeliveryTimeRelevance = 1;
	private double priceRelevance = 1;
	private double[] bestPossibleDimensionValues = { 1.0, 1.0, 1.0 };
	private int[] oftenUpdated; // how often was delivery time updated
	private double[][] values; // store the values for the dimensions (1. trust, 2. averageDeliveryTime, 3. price)
	
	public ProcurementAgent(ArrayList<DeliveryAgent> deliveryAgents, TrustAgent trustAgent) 
	{
		this.trustAgent = trustAgent;
		this.deliveryAgents = deliveryAgents;
		// how big the array must be
		values = new double[3][deliveryAgents.size()];
		oftenUpdated = new int[deliveryAgents.size()];
		for (int i = 0; i < oftenUpdated.length; i++)
		{
			oftenUpdated[i] = 0; // oftenUpdated with 0 initialized
		}
		valueFill();
	}
	
	private void valueFill() 
	{
		// to fill the array values with initial values
		
		for (int i = 0; i < deliveryAgents.size(); i++)
		{
			// procurement asks trust only at a dummy
			values[0][i] = trustAgent.getTrustValue(deliveryAgents.get(i));
			// System.out.println("Current Trust Value: " + values[0][i]);
			values[1][i] = 1;
			values[2][i] = deliveryAgents.get(i).getPrice();
		}
	}
	
	private void fillBest()
	{
		// best values for every dimension is needed because ot the formula
		for (int i = 0; i < deliveryAgents.size(); i++)
		{
			if (bestPossibleDimensionValues[0] <= values[0][i]) { 
				bestPossibleDimensionValues[0] = values[0][i];
			} 
			// best trust value
			if (bestPossibleDimensionValues[1] >= values[1][i]) {
				bestPossibleDimensionValues[1] = values[1][i];
			} 
			// best average delivery time
			if (bestPossibleDimensionValues[2] >= values[2][i]) {
				bestPossibleDimensionValues[2] = values[2][i];
			} // best price
		}
	}
	
	public DeliveryAgent chooseSupplier(Map<DeliveryAgent, Integer> numberOfOpenOrders) 
	{
		TrustSetter s = TrustSetter.getInstance();
		if (s.getTrustIntegrated()) {
			updateTrust();
			fillBest();
			double highest = 0;
			double moment;
			int highestPoint = 0; // mit Index benennen
			System.out.println("---------Supplier Selection----------");
			for (int i = 0; i < deliveryAgents.size(); i++)
			{
				// calculation according to concept team
				moment = values[0][i] / bestPossibleDimensionValues[0] * trustRelevance + bestPossibleDimensionValues[1] / values[1][i] * averageDeliveryTimeRelevance + bestPossibleDimensionValues[2] / values[2][i] * priceRelevance;
				System.out.println("Delivery Agent: " + deliveryAgents.get(i));
				System.out.println("Dimensions: [Trust: " + values[0][i] + "; Average Delivery Time: " + values[1][i] + "; Price: " + values[2][i] + "]");
				System.out.println("Moment: " + moment);
				boolean has_many_open_orders= false;
				if(numberOfOpenOrders.containsKey(deliveryAgents.get(i))){
					if(numberOfOpenOrders.get(deliveryAgents.get(i)) > 4){
						has_many_open_orders=true;
					}
				}
				
				if (moment >= highest && !has_many_open_orders) {	
					highestPoint = i;
					highest = moment;
				}
				
				//if there are already a lot of open orders with this supplier we don't really want to order at him
				//System.out.println("Number of open orders for selected supplier (" + deliveryAgents.get(i) + "): "+ numberOfOpenOrders.get(deliveryAgents.get(i)));
				//if (numberOfOpenOrders.get(deliveryAgents.get(i)) > 3) {
					
				//}
						
			}
			System.out.println("Most suitable: " + deliveryAgents.get(highestPoint));
			return deliveryAgents.get(highestPoint);
		} else {
			//choose cheapest supplier
			DeliveryAgent cheapestSupplier = deliveryAgents.get(0);
	
			for (int i = 0; i < deliveryAgents.size() - 1; i++)
			{	
				boolean has_many_open_orders= false;
				if(numberOfOpenOrders.containsKey(deliveryAgents.get(i))){
					if(numberOfOpenOrders.get(deliveryAgents.get(i)) > 4){
						has_many_open_orders=true;
					}
				}
				if (deliveryAgents.get(i).getPrice() < cheapestSupplier.getPrice() && !has_many_open_orders) {
					cheapestSupplier = deliveryAgents.get(i);					
				} else {
				}
			}
			return cheapestSupplier;
		}
	}
	// method for choosing the second best supplier
	public DeliveryAgent chooseSecondSupplier(Map<DeliveryAgent, Integer> numberOfOpenOrders) 
	{
		TrustSetter s = TrustSetter.getInstance();
		if (s.getTrustIntegrated()) {
			updateTrust();
			fillBest();
			double highest = 0;
			double moment;
			double secondMoment = 0;
			int secondPoint = 0;
			int highestPoint = 0;
			for (int i = 0; i < deliveryAgents.size(); i++)
			{
				boolean has_many_open_orders= false;
				if(numberOfOpenOrders.containsKey(deliveryAgents.get(i))){
					if(numberOfOpenOrders.get(deliveryAgents.get(i)) > 4){
						has_many_open_orders=true;
					}
				}
				// calculation according to concept team
				moment = values[0][i] / bestPossibleDimensionValues[0] * trustRelevance + bestPossibleDimensionValues[1] / values[1][i] * averageDeliveryTimeRelevance + bestPossibleDimensionValues[2] / values[2][i] * priceRelevance;
				if (moment >= highest&& !has_many_open_orders) {
					highestPoint = i;
					highest = moment;
				}
				// if the moment is higher than the second moment but not lower than the highest
				// it is the second highest
				if (moment >= secondMoment && moment < highest&& !has_many_open_orders) {
					secondMoment = moment;
					secondPoint = i;
				}
			}
			return deliveryAgents.get(secondPoint);
		}
		else {
			//choose cheapest supplier
			
			DeliveryAgent cheapestSupplier = deliveryAgents.get(0);
			DeliveryAgent secondCheapestSupplier = deliveryAgents.get(0);
			for (int i = 0; i < deliveryAgents.size() - 1; i++)
			{
				boolean has_many_open_orders= false;
				if(numberOfOpenOrders.containsKey(deliveryAgents.get(i))){
					if(numberOfOpenOrders.get(deliveryAgents.get(i)) > 4){
						has_many_open_orders=true;
					}
				}
				if (deliveryAgents.get(i).getPrice() < cheapestSupplier.getPrice()&& !has_many_open_orders) {
					secondCheapestSupplier=cheapestSupplier;
					cheapestSupplier = deliveryAgents.get(i);			
				} else{
				}
			}
			return secondCheapestSupplier;
		}
	}
	
	private void updateTrust() 
	{
		for (int i = 0; i < deliveryAgents.size(); i++)
		{
			// procurement fragt beim trust nur dummy
			values[0][i] = trustAgent.getTrustValue(deliveryAgents.get(i));
			// System.out.println("Current Trust Value: " + values[0][i]);
			values[2][i] = deliveryAgents.get(i).getPrice();
		}
	}

	// updates the needed time by the last order arrived
	public void updateTime(int ticksTillDelivery, DeliveryAgent deliverer) 
	{
		int update = deliveryAgents.indexOf(deliverer);
		values[1][update] = (values[1][update] * oftenUpdated[update] + ticksTillDelivery) / (oftenUpdated[update] + 1);
		oftenUpdated[update]++;
	}
	
	/*
	 * GETTERS
	 */
	public double gettrustRelevance() 
	{
		return trustRelevance;
	}
	
	public double getAverageDeliveryTimeRelevance() 
	{
		return averageDeliveryTimeRelevance;
	}
	
	public double getPriceRelevance() 
	{
		return priceRelevance;
	}
	
	/* 
	 * SETTERS
	 */
	public void settrustRelevance(double trustRelevance) 
	{
		this.trustRelevance = trustRelevance;
	}
	
	public void setAverageDeliveryTimeRelevance(double averageDeliveryTimeRelevance) 
	{
		this.averageDeliveryTimeRelevance = averageDeliveryTimeRelevance;
	}
	
	public void setPriceRelevance(double priceRelevance) 
	{
		this.priceRelevance = priceRelevance;
	}
}