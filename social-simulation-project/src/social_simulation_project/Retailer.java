package social_simulation_project;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;

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
	private int order_quantity;
	private DeliveryAgent deliveryAgent;
	private OrderAgent orderAgent;
	
	public Retailer() 
	{
		orderAgent = new OrderAgent(this);	
	}
	
	public void run() 
	{
		//TODO
		//1. processShipments()
		this.processShipments();
		//2. updateTrust()	
		//3. deliver()
		this.deliver();
		//4. calculateDemand()
		next_demand = this.forecastAgent.calculateDemand();
		//5. order()
		this.order();
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
}