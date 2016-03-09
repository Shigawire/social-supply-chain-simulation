package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import SimulationSetups.TrustSetter;
import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProductionAgent;
import artefacts.Order;
import artefacts.Profile;

// Combination of Interface Sale and class buy
public abstract class BuySale extends BuyingActor implements SellingActor
{
	// for the subtraction from the order caused by knowing he will not order at me
	protected int subtractionByTrust = 0; 
	
	
	protected int desiredInventoryLevel;
	
	// demand of next tick
	protected int nextDemand; 
	
	// price for our goods
	protected int price; 
	
	 // the quantity should be ordered this tick
	protected int orderQuantity;
	
	protected DeliveryAgent deliveryAgent;
	protected ProductionAgent productionAgent;
	
	protected ArrayList<DeliveryAgent> deliveryAgents;
	protected int lastOrderUpToLevel = -1;
	protected int lastDemand = 0;
	
	private Map<OrderAgent, Integer> buyer = new HashMap<OrderAgent, Integer>();
	

	public BuySale(ArrayList<SellingActor> sellerList, int incomingInventoryLevel, int outgoingInventoryLevel, Profile p) 
	{
		super(sellerList, incomingInventoryLevel, outgoingInventoryLevel, p);
	}
	
	// receiving the shipments that was delivered last tick
	public void receiveShipments() 
	{
		this.orderAgent.receiveShipments(this.inventoryAgent);
	}
	
	
	/**
	   * This method delivers goods to the customer
	   * 
	   */
	public void deliver() 
	{
		this.deliveryAgent.deliver(this.inventoryAgent);
	}
	
	/**
	   * This method orders goods at the retailer's
	   * supplier.
	   * 
	   */
	public void order() 
	{

		//calculate the next demand
		nextDemand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
		
		//check if the last order up to level is not the initial one
		lastOrderUpToLevel = (lastOrderUpToLevel != -1) ? nextDemand : lastOrderUpToLevel;
		
		//calculate a new order up to level 
		int orderUpToLevel = lastOrderUpToLevel + 1*(nextDemand - lastDemand);
		
		desiredInventoryLevel = orderUpToLevel;
		lastDemand = nextDemand;
		lastOrderUpToLevel = orderUpToLevel;
		
		//get the current outgoing inventory level
		int currentOutgoingInventoryLevel = this.inventoryAgent.getOutgoingInventoryLevel();
		// if current bigger than the desired inventory level return
		if (currentOutgoingInventoryLevel > desiredInventoryLevel) {
			return;
		}
		
		//check if trust assessment should be incorporated to the order decision
		
		TrustSetter trustOracle = TrustSetter.getInstance();
		if (trustOracle.getInformationSharingIntegrated()) {
			orderQuantity = nextDemand - currentOutgoingInventoryLevel + deliveryAgent.getShortage() - subtractionByTrust;
		} else {
			orderQuantity = nextDemand - currentOutgoingInventoryLevel + deliveryAgent.getShortage();
		}
		
		subtractionByTrust = 0;
	
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (orderQuantity < 0) {
			// a order with quantity null has to be made for the process in the orderAgent
			// (realize the order of the last tick)
			orderQuantity = 0;
		} else {
			
			//create the order object for the order with the desired quantity
			Order order = new Order(orderQuantity, this.orderAgent);
			
			//hand over the order to the order agent
			orderAgent.order(this.trustAgent, order);
			
			// if he is lying he will order the same at a second supplier
			if (isLying) {
				Order order2 = new Order(orderQuantity, this.orderAgent);
				orderAgent.secondOrder(this.trustAgent, order2);
			}
		}
	}
	
	// just a method used if he is a lying agent 
	public int desired()
	{
		if (isLying) {
			nextDemand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
			desiredInventoryLevel = nextDemand * 15 / 10;
			return desiredInventoryLevel;
		}
		return 0;
	}
	
	// if a possible buyer trust this actor enough, but will not order at him, he will tell it
	public void going2order(OrderAgent noOrderer)
	{
		if (buyer.containsKey(noOrderer)) {
			subtractionByTrust+=buyer.get(noOrderer);
		}	
	}
	
	//update the list of all 
	public void updateClientList(OrderAgent orderer, int orderAtYou)
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
	
	public int getOrderQuantity()
	{
		return this.orderQuantity;
	}
	
	public int getThisTickReceived()
	{
		return this.orderAgent.getThisTickReceived();
	}
}