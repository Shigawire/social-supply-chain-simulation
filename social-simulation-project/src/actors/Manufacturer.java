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
import artefacts.Profile;
import artefacts.trust.Trust;
import repast.simphony.engine.schedule.ScheduledMethod;
import social_simulation_project.BWeffectMeasurer;

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
	private int produceQuantity;
	private int machineQuantity;
	private DeliveryAgent deliveryAgent;
	private ProductionAgent productionAgent;
	private ArrayList<ProductionBatch> production;
	private Map<OrderAgent, Integer> buyer = new HashMap<OrderAgent, Integer>();
	protected int lastOrderUpToLevel = -1;
	protected int lastDemand = 0;
	
	private ArrayList<ProductionBatch> toProduce;
	private int leadTime = 2; //the time needed to produce
	
	public Manufacturer(int currentIncomingInventoryLevel, int currentOutgoingInventoryLevel, int price,Profile p)
	{
		super(currentIncomingInventoryLevel, currentOutgoingInventoryLevel,p);
		this.price = price;	
		deliveryAgent = new DeliveryAgent(price, this,10,5);
		this.machineQuantity = 5;
		productionAgent = new ProductionAgent(leadTime,machineQuantity, this.inventoryAgent);
		production = new ArrayList<ProductionBatch>();
		toProduce = new ArrayList<ProductionBatch>();
		produceQuantity = 0;
	}
	
	// method for every run, start: start tick, priority: which priority it has in the simulation(higher --> better priority)
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void run() 
	{
		// 1. harvest() = collect the produced goods that are ready now
		this.harvest();
		// 2. deliver() all previously harvested goods that can be delivered to customers
		this.deliver(); 
		
		//System.out.println(inventoryAgent.getOutgoingInventoryLevel());
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
	
	public void deliver() 
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
		nextDemand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
		lastOrderUpToLevel = (lastOrderUpToLevel != -1) ? nextDemand : lastOrderUpToLevel;
		int orderUpToLevel = lastOrderUpToLevel + 1*(nextDemand - lastDemand);
		
		desiredInventoryLevel = orderUpToLevel;
		lastDemand = nextDemand;
		lastOrderUpToLevel = orderUpToLevel;
		// 2.
		currentOutgoingInventoryLevel = this.inventoryAgent.getOutgoingInventoryLevel();
		
		//if current bigger than desiredlevel return		
		if(currentOutgoingInventoryLevel > desiredInventoryLevel){

			return;
		}
		
		// 3.
		TrustSetter s = TrustSetter.getInstance();
		
		if(s.getInformationSharingIntegrated()) {
			produceQuantity = nextDemand - currentOutgoingInventoryLevel+ deliveryAgent.getShortage()-subtractionByTrust;
		}
		else
		{
			produceQuantity = nextDemand - currentOutgoingInventoryLevel+ deliveryAgent.getShortage();
		}
		
		subtractionByTrust=0;
	
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (produceQuantity < 0) 
		{
			//a order with quantity null has to be made for the process in the orderAgent
			// (realize the order of the last tick)
			produceQuantity = 0;

		}		
			BWeffectMeasurer.getMeasurer().updateManufacturer(produceQuantity);
			this.productionAgent.produce(produceQuantity);	
	}
	
	
	public DeliveryAgent getDeliveryAgent() 
	{	
		return this.deliveryAgent;
	}
}