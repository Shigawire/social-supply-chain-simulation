package agents;

import java.util.ArrayList;
import java.util.Map;

import actors.SupplyChainMember;
import artefacts.Profile;
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
	
	//initialize the relevances will be later on defined by the profile of the parent
	private double trustRelevance = 1;
	private double averageDeliveryTimeRelevance = 1;
	private double priceRelevance = 1;
	
	//best possible Dimension for every Dimension will be needed for the computation
	private double[] bestPossibleDimensionValues = { 1.0, 1.0, 1.0 };
	
	// how often was delivery time updated needed for the average deliverytime
	private int[] oftenUpdated;
	
	// store the values for the dimensions (1. trust, 2. averageDeliveryTime, 3. price)
	private double[][] values; 
	
	public ProcurementAgent(ArrayList<DeliveryAgent> deliveryAgents, TrustAgent trustAgent, Profile myProfile) 
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
		
		//fill with initalizision values
		valueFill();
		
		//get profilerelavances
		trustRelevance = myProfile.getTrustRelevance();
		averageDeliveryTimeRelevance = myProfile.getDeliveryTimeRelevance();
		priceRelevance = myProfile.getPriceRelevance();
	}
	
	private void valueFill() 
	{
		// to fill the array values with initial values
		
		for (int i = 0; i < deliveryAgents.size(); i++)
		{
			// procurement asks trust only at a dummy
			values[0][i] = trustAgent.getTrustValue(deliveryAgents.get(i));
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
	
	//choose supplier 
	public DeliveryAgent chooseSupplier(Map<DeliveryAgent, Integer> numberOfOpenOrders) 
	{

			updateTrust();
			fillBest();
			double highest = 0;
			double moment;
			int highestPoint = 0; // start with index
			for (int i = 0; i < deliveryAgents.size(); i++)
			{
				// calculation according to concept team
				moment = values[0][i] / bestPossibleDimensionValues[0] * trustRelevance + bestPossibleDimensionValues[1] / values[1][i] * averageDeliveryTimeRelevance + bestPossibleDimensionValues[2] / values[2][i] * priceRelevance;
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
						
			}
			return deliveryAgents.get(highestPoint);
	}
	// method for choosing the second best supplier
	public DeliveryAgent chooseSecondSupplier(Map<DeliveryAgent, Integer> numberOfOpenOrders) 
	{
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
					if(numberOfOpenOrders.get(deliveryAgents.get(i)) > 1){
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
	
	//updated after every order
	private void updateTrust() 
	{
		TrustSetter trustOracle = TrustSetter.getInstance();
		for (int i = 0; i < deliveryAgents.size(); i++)
		{
			//if trust assessment is enabled
			if (trustOracle.getTrustIntegrated()){
				values[0][i] = trustAgent.getTrustValue(deliveryAgents.get(i));
			}
			else{
				values[0][i]=0;
			}
			
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