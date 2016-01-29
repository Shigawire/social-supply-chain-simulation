package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.ProductionAgent;
import artefacts.Order;
//Combination of Interface Sale and class buy
public abstract class Buy_Sale extends Buy implements Sale
{
	protected int next_demand;
	protected int price;
	protected int order_quantity;
	protected DeliveryAgent deliveryAgent;
	protected ProductionAgent productionAgent;
	protected ArrayList<DeliveryAgent> delivery_agents;
	
	public Buy_Sale(ArrayList<Sale> sailor_list,int incoming_inventory_level, int outgoing_inventory_level) 
	{
		super(sailor_list, incoming_inventory_level, outgoing_inventory_level);
	}
	
	public void receiveShipments() 
	{
		this.orderAgent.receiveShipments(this.inventoryAgent);
	}
	
	public void updateTrust() 
	{
		//erstmal auskommentiert solange es noch nicht laeuft
		//this.trustAgent.inspectNewArrivals(orderAgent);
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
		current_inventory_level = this.inventoryAgent.getOutgoingInventoryLevel();
		
		// 3.
		order_quantity = next_demand - current_inventory_level+ deliveryAgent.getShortage();
		
		//System.out.println("[Buy_Sale] order_quantity is  " + order_quantity);
		
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (order_quantity < 0) 
		{
			//a order with quantity null has to be made for the process in the orderAgent
			// (realize the order of the last tick
			order_quantity=0;
			orderAgent.order(this.trustAgent, null);
		}
		else
		{
			//System.out.println("[Buy_Sale] order_quantity is  " + order_quantity);
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