package actors;

import agents.DeliveryAgent;
//Interface for all actors who sell
public interface Sale {
	
	DeliveryAgent deliveryAgent=null;

	DeliveryAgent getDeliveryAgent();
}
