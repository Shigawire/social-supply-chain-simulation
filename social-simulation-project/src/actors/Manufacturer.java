package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProductionAgent;
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
	private int machine_quantity;
	private int amount_to_produce;
	private DeliveryAgent deliveryAgent;
	private OrderAgent orderAgent;
	private ProductionAgent productionAgent;
	private ArrayList<ProductionBatch> Production;
	
	private ArrayList<ProductionBatch> toProduce;
	private int lead_time = 2;
	
	public Manufacturer(int price, int current_incoming_inventory_level,int current_outgoing_inventory_level)
	{
		super(current_incoming_inventory_level, current_outgoing_inventory_level);
		this.price = price;	
		deliveryAgent = new DeliveryAgent(price, this);
		this.machine_quantity = 3;
		productionAgent = new ProductionAgent(lead_time,machine_quantity, this.inventoryAgent);
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
		this.productionAgent.harvest();
	}
	
	private void produce() 
	{		
		this.productionAgent.produce(next_demand-inventoryAgent.getOutgoingInventoryLevel()+ deliveryAgent.getShortage());
	}
	
	public DeliveryAgent getDeliveryAgent() 
	{	
		return this.deliveryAgent;
	}
}