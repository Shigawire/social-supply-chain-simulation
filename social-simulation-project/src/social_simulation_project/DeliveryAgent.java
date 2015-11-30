package social_Simulation_Project;

import java.util.ArrayList;

public class DeliveryAgent {
	private ArrayList<Order> receivedOrders;
	
	public void receiveOrder(Order order) {
		receivedOrders.add(order);
	}
	
	public void deliver(InventoryAgent inventoryAgent){
		int current_inventory_level = inventoryAgent.getInventoryLevel();
		
		for (Order order : receivedOrders) {
			if (order.getAmount() > current_inventory_level) {
				//wenn Inventory nicht ausreicht, wird nicht gelierft;
				//TODO was passiert wenn eine lieferung danach möglicherweise processed werden könnte?
				return;
			} else {
				order.setProcessed(true);
				order.getOrderAgent().receiveShipment(order);
			}
		}
		
	}

}
