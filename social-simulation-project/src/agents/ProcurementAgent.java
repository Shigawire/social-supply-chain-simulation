package agents;

import java.util.ArrayList;

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
	private ArrayList<DeliveryAgent> delivery_agents;
	private TrustAgent trustAgent;
	private double trustrelevance = 1;
	private double averageDeliveryTimeRelevance = 1;
	private double priceRelevance = 1;
	private double[] best = { 1.0, 1.0, 1.0 };
	private int[] oftenUpdatet;//how often was delivery time updated
	private double[][] values; //store the values for the dimensions (1. trust,2.averageDeliveryTime 3.price)
	
	public ProcurementAgent(ArrayList<DeliveryAgent> delivery_agents, TrustAgent trustAgent) 
	{
		this.trustAgent = trustAgent;
		this.delivery_agents = delivery_agents;
		// how big the array must be
		values = new double[3][delivery_agents.size()];
		oftenUpdatet = new int[delivery_agents.size()];
		for(int i=0;i<oftenUpdatet.length;i++){
			oftenUpdatet[i]=0;//oftenUpdatet with 0 initialized
		}
		valueFill();
	}
	
	private void valueFill() 
	{
		// to fill the array values with initial values
		
		for (int i = 0; i < delivery_agents.size(); i++)
		{
			//procurement asks trust only at a dummy
			values[0][i] = trustAgent.getTrustValue(delivery_agents.get(i));
			//System.out.println("Current Trust Value: " + values[0][i]);
			values[1][i] = 1;
			values[2][i] = delivery_agents.get(i).getPrice();
		}
	}
	
	private void fillBest()
	{
		//best values for every dimension is needed because ot the formula
		for (int i = 0; i < delivery_agents.size(); i++)
		{
			if (best[0] <= values[0][i])
			{ 
				best[0] = values[0][i];
			} //best trust value
			if (best[1] >= values[1][i])
			{
				best[1] = values[1][i];
			} //best average delivery time
			if (best[2] >= values[2][i])
			{
				best[2] = values[2][i];
			} //best price
		}
	}
	
	public DeliveryAgent chooseSupplier() 
	{
		TrustSetter s = TrustSetter.getInstance();
		if(s.getTrustIntegrated())
		{
			updateTrust();
			fillBest();
			double highest = 0;
			double moment;
			int highestPoint = 0;
			for (int i = 0; i < delivery_agents.size(); i++)
			{
				// calculation according to concept team
				moment = values[0][i] / best[0]*trustrelevance+best[1]/values[1][i]*averageDeliveryTimeRelevance+best[2]/values[2][i]*priceRelevance;
				if (moment >= highest)
				{	
					highestPoint = i;
					highest = moment;
				}
			}
			return delivery_agents.get(highestPoint);
		}
		else
		{

			DeliveryAgent cheapestSupplier = delivery_agents.get(0);
			
			for (int i = 0; i < delivery_agents.size() - 1; i++)
			{
				if (delivery_agents.get(i).getPrice() < delivery_agents.get(i+1).getPrice())
				{
					cheapestSupplier = delivery_agents.get(i);					
				}
				else
				{
					cheapestSupplier = delivery_agents.get(i+1);
				}
			}
			return cheapestSupplier;
		}
	}
	//method for choosing the second best supplier
	public DeliveryAgent chooseSecondSupplier() 
	{
		updateTrust();
		fillBest();
		double highest = 0;
		double moment;
		double secondMoment=0;
		int secondPoint = 0;
		int highestPoint = 0;
		for (int i = 0; i < delivery_agents.size(); i++)
		{
			// calculation according to concept team
			moment = values[0][i] / best[0]*trustrelevance+best[1]/values[1][i]*averageDeliveryTimeRelevance+best[2]/values[2][i]*priceRelevance;
			if (moment >= highest){
				highestPoint = i;
				highest = moment;
			}
			//if the moment is higher than the second moment but not lower than the highest
			//it is the second highest
			if(moment>=secondMoment&&moment<highest){
				secondMoment=moment;
				secondPoint=i;
			}
		}
		return delivery_agents.get(secondPoint);
	}
	
	private void updateTrust() {
		for (int i = 0; i < delivery_agents.size(); i++)
		{
			//procurement fragt beim trust nur dummy
			values[0][i] = trustAgent.getTrustValue(delivery_agents.get(i));
			//System.out.println("Current Trust Value: " + values[0][i]);
			values[2][i] = delivery_agents.get(i).getPrice();
		}
	}

	//updates the needed time by the last order arrived
	public void updateTime(int ticksTillDelivery, DeliveryAgent deliverer) 
	{
		int updaten = delivery_agents.indexOf(deliverer);
		values[2][updaten] = (values[2][updaten]*oftenUpdatet[updaten]+ticksTillDelivery)/(oftenUpdatet[updaten]+1);
		oftenUpdatet[updaten]++;
	}
	
	/*
	 * GETTERS
	 */
	public double getTrustrelevance() 
	{
		return trustrelevance;
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
	public void setTrustrelevance(double trustrelevance) 
	{
		this.trustrelevance = trustrelevance;
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