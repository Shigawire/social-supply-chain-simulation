package actors;

import java.util.HashMap;
import java.util.Map;

import agents.DeliveryAgent;
import agents.OrderAgent;

// Interface for all actors who sell
public interface Sale 
{
	// a map with all buyers and the value they ordered last for information sharing
	Map<Buy, Integer> buyer = null;
	DeliveryAgent deliveryAgent = null;
	
	// the update list method for register the buyers
	public void updateList(OrderAgent orderer, int orderAtYou);
	DeliveryAgent getDeliveryAgent();
}