package social_simulation_project;

import java.util.ArrayList;
import java.util.ListIterator;

import repast.simphony.engine.schedule.ScheduledMethod;

/**
* This class represents the manufacturer. The manufacturer
* does not order, but produces. He has no trust agent (?).
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class Manufacturer extends SupplyChainMember
{	
	private int demand_amount;
	private int next_demand;
	private int price;
	private int order_quantity;
	private DeliveryAgent deliveryAgent;
	private OrderAgent orderAgent;
	private ArrayList<ProductionBatch> Production;
	private int lead_time = 2;
	
	public Manufacturer(int price, int current_inventory_level)
	{
		super(current_inventory_level);
		this.price = price;
		orderAgent = new OrderAgent(this);	
		deliveryAgent = new DeliveryAgent(price);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 6)
	public void run() {
		// 1. harvest() = collect the produced goods that are ready now
		this.harvest();
		// 2. deliver()
		this.deliver();
		//3. calculateDemand()
		this.calculateDemand();
		//4. produce();
		produce();
		
		
	}

	public void receiveShipments() {
		harvest();
	}
	
	private void calculateDemand() {
		this.next_demand = this.forecastAgent.calculateDemand();
	}
	
	private void deliver() 
	{
		this.deliveryAgent.deliver(this.inventoryAgent);
	}
	
	private void harvest() {
	//	for (final ListIterator<ProductionBatch> i = Production.listIterator(); i.hasNext();) {
	//		final ProductionBatch current_batch = i.next();
		for (ProductionBatch current_batch : Production) {
			current_batch.incrementTimeInProduction();
			if (current_batch.getTimeInProduction() >= this.lead_time) {
				//This batch is ready to be added to inventory
				this.inventoryAgent.setInventoryLevel(this.inventoryAgent.getInventoryLevel() + current_batch.getQuantity());
				Production.remove(current_batch);
			}
		}
		
		
	}
	private void produce() {
		ProductionBatch new_production_order = new ProductionBatch(this.lead_time, next_demand);
		Production.add(new_production_order);
	}
	
	/*
	 * GETTERS
	 */
	
	/*
	 * SETTERS
	 */
}