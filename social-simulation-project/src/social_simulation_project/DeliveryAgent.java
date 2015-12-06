package social_simulation_project;

import java.util.ArrayList;

/**
* This class represents a delivery agent. They are 
* responsible for delivery (only retailers, wholesalers,
* distributors and the manufacturer). 
* Delivery agents communicate with
* - the user's inventory agent
* - the callee's order agent
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class DeliveryAgent 
{
	private int price;
	private ArrayList<Order> receivedOrders;
	
	public DeliveryAgent(int price) 
	{
		this.receivedOrders = new ArrayList<Order>();
		this.price = price;
	}
	
	/**
	   * This method receives orders.
	   * 
	   * @param order Order of the callee with details about amount etc.
	   * @return Nothing.
	   */
	public void receiveOrder(Order order) 
	{
		receivedOrders.add(order);
	}
	
	/**
	   * This method receives goods at the beginning of each tick
	   * 
	   * @param inventoryAgent inventory agent used to get information 
	   * 	    about the inventory level of the current deliverer
	   */
	public void deliver(InventoryAgent inventoryAgent)
	{
		int current_inventory_level = inventoryAgent.getInventoryLevel();
		
		for (Order order : receivedOrders) 
		{
			if (order.getQuantity() > current_inventory_level) 
			{
				//wenn Inventory nicht ausreicht, wird nicht geliefert;
				//TODO was passiert wenn eine lieferung danach möglicherweise processed werden könnte?
				return;
			} 
			else 
			{
				
				order.setProcessed(true);
				order.getOrderAgent().receiveShipment(order);
//				inventoryAgent.reduceInventoryLevel(order.getQuantity());
				inventoryAgent.reduceInventoryLevel(7);
			}
		}
	}

	public int getPrice() 
	{	
		return this.price;
	}
	
	/*
	 * GETTERS
	 */
	
	/*
	 * SETTERS
	 */
}