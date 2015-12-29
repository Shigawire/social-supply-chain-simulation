package social_simulation_project;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;

/**
* This class represents a retailer. Retailer do not
* differ from wholesalers or distributors. The only 
* difference is their position in the supply chain.
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class Retailer extends SupplyChainMember 
{
	private int next_demand;
	private int price;
	private int order_quantity;
	private DeliveryAgent deliveryAgent;
	private ArrayList<DeliveryAgent> delivery_agents;
	
	public Retailer(ArrayList<Wholesaler> wholesaler_list, int price, int current_inventory_level) 
	{
		super(current_inventory_level);
		delivery_agents = new ArrayList<DeliveryAgent>();
		
		for (Wholesaler wholesaler : wholesaler_list)
		{
			delivery_agents.add(wholesaler.getDeliveryAgent());
		}

		this.price = price;
		orderAgent = new OrderAgent(this);
		deliveryAgent = new DeliveryAgent(price);
		trustAgent = new TrustAgent(delivery_agents);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 4)
	public void run() 
	{
		// 1. processShipments() receive shipments
		this.receiveShipments();
		// 2. updateTrust()	
		// 3. deliver()
		this.deliver();
		// 4. calculateDemand()
		next_demand = this.forecastAgent.calculateDemand();
		// 5. order()
		this.order();
	}
	
	/**
	   * This method receives goods at the beginning of each tick
	   * 
	   * @return Nothing.
	   */
	public void receiveShipments() 
	{
		this.orderAgent.receiveShipments(this.inventoryAgent);
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
		next_demand = this.forecastAgent.calculateDemand();
		
		// 2.
		current_inventory_level = this.inventoryAgent.getInventoryLevel();
		
		// 3.
		order_quantity = next_demand - current_inventory_level;
		
		//System.out.println(order_quantity);
		
		// TODO replenishment policy
		
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
	
	/* 
	 * SETTERS
	 */
}