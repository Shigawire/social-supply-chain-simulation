package actors;

import java.util.HashMap;
import java.util.Map;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProductionAgent;

// Interface for all actors who sell
public interface SellingActor 
{
	
	int price=0;
	// a map with all buyers and the value they ordered last for information sharing
	Map<BuyingActor, Integer> buyer = null;
	DeliveryAgent deliveryAgent = null;
	ProductionAgent productionAgent=null;
	
	public void deliver();
	// the update list method for register the buyers
	public void updateClientList(OrderAgent orderer, int orderAtYou);
	public void going2order(OrderAgent orderAgent);
	public void produce();
	DeliveryAgent getDeliveryAgent();
}