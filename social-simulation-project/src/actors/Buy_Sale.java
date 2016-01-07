package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import artefacts.Order;

public abstract class Buy_Sale extends Buy
{
	protected int next_demand;
	protected int price;
	protected int order_quantity;
	protected DeliveryAgent deliveryAgent;
	protected ArrayList<DeliveryAgent> delivery_agents;
	
	public Buy_Sale(int inventory_level) 
	{
		super(inventory_level);
	}
	
	public void receiveShipments() 
	{
		this.orderAgent.receiveShipments(this.inventoryAgent);
	}
	
	public void updateTrust() 
	{
		this.trustAgent.inspectNewArrivals(orderAgent.getReceivedShipments());
	}
	
	/**
	   * This method delivers goods to the customer
	   * 
	   * @return Nothing.
	   */
	public void deliver() 
	{
		this.deliveryAgent.deliver(this.inventoryAgent);
	}
	
	/**
	   * This method orders goods at the retailer's
	   * supplier.
	   * 
	   * @return Nothing.
	   */
	public void order() 
	{
		// 1. Was brauch ich im nächsten tick?  (forecastagent befragen)
		// 2. Was hab ich noch im Inventar?
		// 3. Differenz bestellen. mit orderArgent
		
		// 1.
		next_demand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
		
		// 2.
		current_inventory_level = this.inventoryAgent.getInventoryLevel();
		
		// 3.
		order_quantity = next_demand - current_inventory_level+ deliveryAgent.getShortage();
		
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (order_quantity < 0) 
		{
			return;
		}
		else
		{
			Order order = new Order(order_quantity, this.orderAgent);
			
			// Choose retailer
			orderAgent.order(this.trustAgent, order);
		}
	}

	/*
	 * GETTERS
	 */
	public DeliveryAgent getDeliveryAgent() 
	{	
		return this.deliveryAgent;
	}
	
	public int getPrice()
	{
		return this.price;
	}	
}