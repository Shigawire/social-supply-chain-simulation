package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProductionAgent;
import artefacts.Order;
//Combination of Interface Sale and class buy
public abstract class Buy_Sale extends Buy implements Sale
{
	protected int subtractionByTrust=0;//for the subtraction from the order caused by knowing he will not order at me
	protected int desired_inventory_level;
	protected int next_demand;//demand of next tick
	protected int price;//price for our goods
	protected int order_quantity;//the quantity should be ordered this tick
	protected DeliveryAgent deliveryAgent;
	protected ProductionAgent productionAgent;
	protected ArrayList<DeliveryAgent> delivery_agents;
	
	private Map<OrderAgent, Integer> buyer = new HashMap<OrderAgent, Integer>();
	public Buy_Sale(ArrayList<Sale> sailor_list,int incoming_inventory_level, int outgoing_inventory_level) 
	{
		super(sailor_list, incoming_inventory_level, outgoing_inventory_level);
	}
	//receiving the shipments that was delivered last tick
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
		// 1. need in the next tick
		// 2. whats about my inventory
		// 3. order difference: +shortage-the value I do not need because of information sharing

		// 1.
		next_demand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
		desired_inventory_level = next_demand*15/10;
		// 2.
		current_outgoing_inventory_level = this.inventoryAgent.getOutgoingInventoryLevel();
		//if current bigger than desiredlevel return
		if(current_outgoing_inventory_level>desired_inventory_level){
			return;
		}
		// 3.
		order_quantity = next_demand - current_outgoing_inventory_level+ deliveryAgent.getShortage()-subtractionByTrust;
		
		subtractionByTrust=0;
	
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (order_quantity < 0) 
		{
			//a order with quantity null has to be made for the process in the orderAgent
			// (realize the order of the last tick)
			order_quantity=0;
			orderAgent.order(this.trustAgent, null);
		}
		else
		{
			
			Order order = new Order(order_quantity, this.orderAgent);
			
			// Choose seller
			orderAgent.order(this.trustAgent, order);
			//if he is lying he will order the same at a second supplier
			if(lying){
				Order order2 = new Order(order_quantity, this.orderAgent);
				orderAgent.secondOrder(this.trustAgent, order2);
			}
			
		}
	}
	// just a method used if he is a lying agent 
	public int desired(){
		if(lying){
			next_demand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
			desired_inventory_level = next_demand*15/10;
			//System.out.println("desired_inventory_level"+desired_inventory_level);
			return desired_inventory_level;
		}
		return 1000;
		
	}
	// if a possible buyer trust this actor enough, but will not order at him, he will tell it
		public void going2order(OrderAgent noOrderer){
			if(buyer.containsKey(noOrderer)){
				subtractionByTrust+=buyer.get(noOrderer);
			}	
		}
		public void updateList(OrderAgent orderer,int orderAtYou){
			//when the buyer is not already in the map
			if(!buyer.containsKey(orderer)){
				buyer.put(orderer, orderAtYou);
			}
			//the value is changed by the value he ordered this time
			int newValue=(buyer.get(orderer)+orderAtYou)/2;
			//because of RePast the new value has to be put int the map this way
			buyer.remove(orderer);
			buyer.put(orderer, newValue);	
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
	public int getOrderQuantity(){
		return this.order_quantity;
	}
	public int getThisTickReceived(){
		return this.orderAgent.getThisTickReceived();
	}
}