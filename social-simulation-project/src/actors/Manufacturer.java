package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import SimulationSetups.TrustSetter;
import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProductionAgent;
import artefacts.Order;
import artefacts.ProductionBatch;
import artefacts.trust.Trust;
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
	private int subtractionByTrust = 0; // for the subtraction from the order caused by knowing he will not order at me
	private int nextDemand;
	private int desiredInventoryLevel;
	private int price; // price for the good
	private int orderQuantity;
	private int machineQuantity;
	private int amountToProduce;
	private DeliveryAgent deliveryAgent;
	private OrderAgent orderAgent;
	private ProductionAgent productionAgent;
	private ArrayList<ProductionBatch> production;
	private Map<OrderAgent, Integer> buyer = new HashMap<OrderAgent, Integer>();
	
	private ArrayList<ProductionBatch> toProduce;
	private int leadTime = 2; //the time needed to produce
	
	public Manufacturer(int currentIncomingInventoryLevel, int currentOutgoingInventoryLevel, int price)
	{
		super(currentIncomingInventoryLevel, currentOutgoingInventoryLevel);
		this.price = price;	
		deliveryAgent = new DeliveryAgent(price, this,10,5);
		this.machine_quantity = 5;
		productionAgent = new ProductionAgent(lead_time,machine_quantity, this.inventoryAgent);
		Production = new ArrayList<ProductionBatch>();
		toProduce = new ArrayList<ProductionBatch>();
		produce_quantity = 0;
	}
	
	// method for every run, start: start tick, priority: which priority it has in the simulation(higher --> better priority)
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void run() 
	{
		// 1. harvest() = collect the produced goods that are ready now
		this.harvest();
		// 2. deliver() all previously harvested goods that can be delivered to customers
		this.deliver(); 
		
		System.out.println(inventoryAgent.getOutgoingInventoryLevel());
		//3. calculateDemand() = calculate own demand
		this.calculateDemand();
		// 4. produce() = produce new goods()
		this.produce();
		this.deliverRawMaterials();
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
		// ask the forecast Agent about the next demand
		nextDemand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
	}
	
	private void deliver() 
	{
		// delegate delivery of produced goods the delivery Agent
		this.deliveryAgent.deliver(this.inventoryAgent);
	}
	
	// if a possible buyer trust this actor enough, but will not order at him, he will tell it
	public void going2order(OrderAgent noOrderer)
	{
		if (buyer.containsKey(noOrderer)) {
			subtractionByTrust += buyer.get(noOrderer);
		}	
	}
	
	// the list of what amount which orderAgent ordered is updated with every order
	public void updateList(OrderAgent orderer, int orderAtYou)
	{
		// when the buyer is not already in the map
		if (!buyer.containsKey(orderer)) {
			buyer.put(orderer, orderAtYou);
		}
		
		// the value is changed by the value he ordered this time
		int newValue = (buyer.get(orderer) + orderAtYou) / 2;
		// because of RePast the new value has to be put int the map this way
		buyer.remove(orderer);
		buyer.put(orderer, newValue);	
	}
	
	public void deliverRawMaterials()
	{
		
	}
	
	private void harvest() 
	{
		this.productionAgent.harvest();
	}
	
	
	
	public void produce() 
	{
		// 1. need in the next tick
		// 2. whats about my inventory
		// 3. order difference: +shortage-the value I do not need because of information sharing

		// 1.
		next_demand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
		desired_inventory_level = next_demand*15/10;
		// 2.
		current_outgoing_inventory_level = this.inventoryAgent.getOutgoingInventoryLevel();
		
		//if current bigger than desiredlevel return

		
		if(current_outgoing_inventory_level > desired_inventory_level){
//			deliveryAgent.setShortage(0);
			return;
		}
		
		// 3.
		TrustSetter s = TrustSetter.getInstance();
		
		if(s.getInformationSharingIntegrated()) {
			produce_quantity = next_demand - current_outgoing_inventory_level+ deliveryAgent.getShortage()-subtractionByTrust;
		}
		else
		{
			produce_quantity = next_demand - current_outgoing_inventory_level+ deliveryAgent.getShortage();
		}
		
		subtractionByTrust=0;
	
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (produce_quantity < 0) 
		{
			//a order with quantity null has to be made for the process in the orderAgent
			// (realize the order of the last tick)
			produce_quantity = 0;

		}
		else
		{
			this.productionAgent.produce(produce_quantity);
		}
	}
	
	
	
	
	//TODO collaborative commenting
//	private void produce() 
//	{	
//		// Produce if inventory insufficient for next demand
//		// 
//		current_outgoing_inventory_level = this.inventoryAgent.getOutgoingInventoryLevel();
//		desired_inventory_level = next_demand*15/10;
//		
//		
//		if(next_demand - inventoryAgent.getOutgoingInventoryLevel() + deliveryAgent.getShortage() >= 0)
//		{
//			if(current_outgoing_inventory_level > desired_inventory_level){
//				deliveryAgent.setShortage(0);
//				return;
//			}
//		//shortage at the current orders will be produced to
//		//TODO in which far did he already include this by FABIAN, because he wrote the class
//			this.productionAgent.produce(next_demand-inventoryAgent.getOutgoingInventoryLevel()+ deliveryAgent.getShortage()-subtractionByTrust);
//			subtractionByTrust=0;
//		}
//	}
	
	public DeliveryAgent getDeliveryAgent() 
	{	
		return this.deliveryAgent;
	}
}