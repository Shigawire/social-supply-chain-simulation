package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import artefacts.ProductionBatch;
import repast.simphony.engine.schedule.ScheduledMethod;

/**
* This class represents the manufacturer. The manufacturer
* does not order, but produces. He has no trust agent (?).
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class Manufacturer extends SupplyChainMember implements Sale
{	
	private int next_demand;
	private int price;
	private int order_quantity;
	private int amount_to_produce;
	private DeliveryAgent deliveryAgent;
	private ArrayList<ProductionBatch> Production;
	
	private ArrayList<ProductionBatch> toProduce;
	private int lead_time = 2;
	
	public Manufacturer(int price, int current_inventory_level)
	{
		super(current_inventory_level);
		this.price = price;	
		deliveryAgent = new DeliveryAgent(price);
		Production = new ArrayList<ProductionBatch>();
		toProduce = new ArrayList<ProductionBatch>();
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void run() 
	{
		// 1. harvest() = collect the produced goods that are ready now
		this.harvest();
		// 2. deliver()
		this.deliver();
		//3. calculateDemand()
		this.calculateDemand();
		//4. produce();
		produce();
	}

	/**
	   * This method receives goods at the beginning of each tick
	   * 
	   * @return Nothing.
	   */
	public void receiveShipments() 
	{
		harvest();
	}
	
	private void calculateDemand() 
	{
		next_demand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
	}
	
	private void deliver() 
	{
		this.deliveryAgent.deliver(this.inventoryAgent);
	}
	
	private void harvest() 
	{
		for (ProductionBatch current_batch : Production) 
		{
			current_batch.incrementTimeInProduction();
			if (current_batch.getTimeInProduction() >= this.lead_time) 
			{
				//This batch is ready to be added to inventory
				this.inventoryAgent.setInventoryLevel(this.inventoryAgent.getInventoryLevel() + current_batch.getQuantity());
				//Production.remove(current_batch);
			} 
			else
			{
				toProduce.add(current_batch);
			}
		}
		
		Production.clear();
		Production.addAll(toProduce);
		toProduce.clear();
	}
	
	private void produce() 
	{		
		current_inventory_level = this.inventoryAgent.getInventoryLevel();
		//shortage at the current orders will be produced to
		//TODO in which far did he already include this by FABIAN, because he wrote the class
		amount_to_produce = next_demand - current_inventory_level+ deliveryAgent.getShortage();
		amount_to_produce = (amount_to_produce > 0) ? amount_to_produce : 0;
		
		ProductionBatch new_production_order = new ProductionBatch(this.lead_time, amount_to_produce);
		Production.add(new_production_order);
	}
	
	public DeliveryAgent getDeliveryAgent() 
	{	
		return this.deliveryAgent;
	}
}