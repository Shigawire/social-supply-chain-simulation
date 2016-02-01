package actors;

import java.util.HashMap;
import java.util.Map;

import agents.DeliveryAgent;
import agents.OrderAgent;
//Interface for all actors who sell
public interface Sale {
	Map<Buy, Integer> buyer = null;
	DeliveryAgent deliveryAgent=null;
	public void updateList(OrderAgent orderer,int orderAtYou);
	DeliveryAgent getDeliveryAgent();
}
