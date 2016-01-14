package actors;

import agents.DeliveryAgent;

public interface Sale {
	DeliveryAgent deliveryAgent=null;

	DeliveryAgent getDeliveryAgent();
}
