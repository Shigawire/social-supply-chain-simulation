package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProductionAgent;
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
	private int subtractionByTrust=0;//for the subtraction from the order caused by knowing he will not order at me
	private int next_demand;
	private int desired_inventory_level;
	private int price;//price for the good
	private int order_quantity;
	private int machine_quantity;
	private int amount_to_produce;
	private DeliveryAgent deliveryAgent;
	private OrderAgent orderAgent;
	private ProductionAgent productionAgent;
	private ArrayList<ProductionBatch> Production;
	private Map<OrderAgent, Integer> buyer = new HashMap<OrderAgent, Integer>();
	
	private ArrayList<ProductionBatch> toProduce;
	private int lead_time = 2;//the time needed to produce
	
	public Manufacturer(int current_incoming_inventory_level,int current_outgoing_inventory_level, int price)
	{
		super(current_incoming_inventory_level, current_outgoing_inventory_level);
		this.price = price;	
		deliveryAgent = new DeliveryAgent(price, this,10,5);
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
		next_demand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
	}
	
	private void deliver() 
	{
		this.deliveryAgent.deliver(this.inventoryAgent);
	}
	public void going2order(OrderAgent noOrderer){
		if(buyer.containsKey(noOrderer)){
			//System.out.println("subtraction"+buyer.get(noOrderer));
			subtractionByTrust+=buyer.get(noOrderer);
		}
		else{
			//System.out.println("ist nicht");
		}
		
	}
	public void updateList(OrderAgent orderer,int orderAtYou){
		//System.out.println("update"+" "+orderer.getParent().getClass());
		if(!buyer.containsKey(orderer)){
			buyer.put(orderer, orderAtYou);
		}
		//System.out.println(buyer.toString());
		int newValue=(buyer.get(orderer)+orderAtYou)/2;
		//System.out.println(newValue);
		buyer.remove(orderer);
		buyer.put(orderer, newValue);
		//System.out.println("new value for him"+buyer.get(orderer));	
	}
	
	public void deliverRawMaterials(){
		
	}
	private void harvest() 
	{
		this.productionAgent.harvest();
	}
	
	private void produce() 
	{		
		if(next_demand-inventoryAgent.getOutgoingInventoryLevel()+ deliveryAgent.getShortage()>=0)
		{

		current_outgoing_inventory_level = this.inventoryAgent.getOutgoingInventoryLevel();
		desired_inventory_level = next_demand*15/10;
		if(current_outgoing_inventory_level>desired_inventory_level){
			deliveryAgent.setShortage(0);
			return;
		}
		//shortage at the current orders will be produced to
		//TODO in which far did he already include this by FABIAN, because he wrote the class
		//System.out.println(subtractionByTrust+" subtraction by trust");

		this.productionAgent.produce(next_demand-inventoryAgent.getOutgoingInventoryLevel()+ deliveryAgent.getShortage());
		}
	}
	
	public DeliveryAgent getDeliveryAgent() 
	{	
		return this.deliveryAgent;
	}
}