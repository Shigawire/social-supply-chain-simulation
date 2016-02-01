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

	protected int next_demand;//demand of next tick
	protected int price;//price for our goods
	protected int order_quantity;
	protected DeliveryAgent deliveryAgent;
	protected ProductionAgent productionAgent;
	protected ArrayList<DeliveryAgent> delivery_agents;
	
	private Map<OrderAgent, Integer> buyer = new HashMap<OrderAgent, Integer>();
	public Buy_Sale(ArrayList<Sale> sailor_list,int incoming_inventory_level, int outgoing_inventory_level) 
	{
		super(sailor_list, incoming_inventory_level, outgoing_inventory_level);
	}
	
	public void receiveShipments() 
	{
		this.orderAgent.receiveShipments(this.inventoryAgent);
	}
	
	public void updateTrust() 
	{
		//erstmal auskommentiert solange es noch nicht laeuft
		//this.trustAgent.inspectNewArrivals(orderAgent);
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

		int desired_inventory_level;
		// 1. Was brauch ich im nächsten tick?  (forecastagent befragen)
		// 2. Was hab ich noch im Inventar?
		// 3. Differenz bestellen. mit orderArgent

		// 1. need in the next tick
		// 2. whats about my inventory
		// 3. order difference

		
		// 1.
		next_demand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
		desired_inventory_level = next_demand*15/10;
		System.out.println("desired"+desired_inventory_level);
		
		// 2.
		current_outgoing_inventory_level = this.inventoryAgent.getOutgoingInventoryLevel();
		
		// 3.
		order_quantity = next_demand + deliveryAgent.getShortage()- current_outgoing_inventory_level;
		System.out.println("order_quantity"+order_quantity);
		
		System.out.println(subtractionByTrust+" subtraction by trust");
		order_quantity = next_demand - current_outgoing_inventory_level+ deliveryAgent.getShortage()-subtractionByTrust;
		subtractionByTrust=0;

		//System.out.println("[Buy_Sale] order_quantity is  " + order_quantity);
		
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (order_quantity < 0) 
		{
			//a order with quantity null has to be made for the process in the orderAgent
			// (realize the order of the last tick
			order_quantity=0;
			orderAgent.order(this.trustAgent, null);
		}
		else
		{
			//System.out.println("[Buy_Sale] order_quantity is  " + order_quantity);
			Order order = new Order(order_quantity, this.orderAgent);
			
			// Choose retailer
			orderAgent.order(this.trustAgent, order);
		}
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
		int newValue=(buyer.get(orderer)+orderAtYou)/2;
		//System.out.println(newValue);
		buyer.remove(orderer);
		buyer.put(orderer, newValue);
		//System.out.println("new value for him"+buyer.get(orderer));
		
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
}