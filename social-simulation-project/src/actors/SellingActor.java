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
	
	// a list of all purchasers and their submitted substractionByTrust value
	Map<BuyingActor, Integer> buyer = null;
	
	DeliveryAgent deliveryAgent = null;
	ProductionAgent productionAgent = null;
	
	public void deliver();
	
	// the update list method for register the buyers
	public void updateClientList(OrderAgent orderer, int orderAtYou);
	
	// if a possible buyer trusts this actor enough, but will not order at him, he will tell him this
	public void going2order(OrderAgent orderAgent);
	public void produce();
	
	DeliveryAgent getDeliveryAgent();
}