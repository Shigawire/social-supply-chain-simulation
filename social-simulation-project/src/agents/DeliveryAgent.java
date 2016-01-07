package agents;

import java.util.ArrayList;

import artefacts.Order;
import social_simulation_project.OrderObserver;

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
	private int current_inventory_level;
	private ArrayList<Order> receivedOrders; // Liste um noch offene Orders zu �bertragen
	private ArrayList<Order> everReceivedOrders;
	private ArrayList<Order> openOrders;
	private int shortage = 0;
	
	public DeliveryAgent(int price) 
	{
		this.receivedOrders = new ArrayList<Order>();
		this.everReceivedOrders = new ArrayList<Order>();
		this.openOrders = new ArrayList<Order>();
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
		everReceivedOrders.add(order);
	}
	
	/**
	   * This method receives goods at the beginning of each tick
	   * 
	   * @param inventoryAgent inventory agent used to get information 
	   * 	    about the inventory level of the current deliverer
	   */
	public void deliver(InventoryAgent inventoryAgent)
	{
		current_inventory_level = inventoryAgent.getInventoryLevel();
		shortage=0;
		for (Order order : receivedOrders) 
		{
			if (order.getQuantity() > current_inventory_level) 
			{
				//wenn Inventory nicht ausreicht, wird nicht geliefert;
				//TODO was passiert wenn eine lieferung danach moeglicherweise processed werden koennte? Loesung das return weg, 
				//dann geht er alle restlichen Bestellungen auch noch durch
				openOrders.add(order);
				shortage=+order.getQuantity();
				
				//return;
			} 
			else 
			{
				order.setProcessed(true);
				//sub the amount because the order is not open anymore
				OrderObserver.giveObserver().subAmount(order.getQuantity());
				order.getOrderAgent().receiveShipment(order,this);
				//System.out.println(order.getQuantity());
				inventoryAgent.reduceInventoryLevel(order.getQuantity());
				current_inventory_level = inventoryAgent.getInventoryLevel();
			}
		}
		//the received list must be deleted completly and filled with the openOrder list
		//otherwise RePast has a problem
		receivedOrders.clear();
		receivedOrders.addAll(openOrders);
		openOrders.clear();
	}

	/*
	 * GETTERS
	 */
	public int getPrice() 
	{	
		return this.price;
	}

	public int getShortage() 
	{
		return shortage;
	}
	
	public ArrayList<Order> getAllOrders()
	{	
		return this.everReceivedOrders;
	}
	
	/*
	 * SETTERS
	 */
}