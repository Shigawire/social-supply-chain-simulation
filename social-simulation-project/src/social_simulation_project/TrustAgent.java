package social_simulation_project;

import java.util.ArrayList;

import repast.simphony.random.RandomHelper;

/**
* This class represents a trust agent. 
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class TrustAgent 
{
	private ArrayList<DeliveryAgent> delivery_agents;
	
	public TrustAgent(ArrayList<DeliveryAgent> delivery_agents)
	{
		this.delivery_agents = delivery_agents;
	}

	public DeliveryAgent chooseSupplier() 
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

	public double getTrust(int place) {
		//hier muss der trust wert zurüclgegeben werden!!!
		return RandomHelper.nextIntFromTo(0, 1);
	}
	
	/*
	 * GETTERS
	 */
	
	/* 
	 * SETTERS
	 */
}