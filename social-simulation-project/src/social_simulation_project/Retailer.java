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
	private int demand_amount;
	private int next_demand;
	private int current_inventory_level;
	private int price;
	private int order_quantity;
	private DeliveryAgent deliveryAgent;
	private OrderAgent orderAgent;
	
	public Retailer(int price, int current_inventory_level) 
	{
		super(current_inventory_level);
		this.price = price;
		orderAgent = new OrderAgent(this);	
		deliveryAgent = new DeliveryAgent(price);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	
//	@Watch(watcheeClassName = "social_simulation_project.Customer",
//	watcheeFieldNames = "finished",
//	whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void run() 
	{
		//TODO
		//1. processShipments() receive shipments
		this.processShipments();
		//2. updateTrust()	
		//3. deliver()
		this.deliver();
		//4. calculateDemand()
		next_demand = this.forecastAgent.calculateDemand();
		//5. order()
//		this.order();
	}
	
	/**
	   * This method receives goods at the beginning of each tick
	   * 
	   * @return Nothing.
	   */
	private void processShipments() 
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

	public DeliveryAgent getDeliveryAgent() 
	{	
		return this.deliveryAgent;
	}
	
	public int getCurrent_inventory_level()
	{
		return this.inventoryAgent.getInventoryLevel();
	}
	
	public int getPrice()
	{
		return this.price;
	}
}