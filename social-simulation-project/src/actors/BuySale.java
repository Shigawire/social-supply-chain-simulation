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
public abstract class BuySale extends Buy implements Sale
{
	protected int subtractionByTrust = 0; // for the subtraction from the order caused by knowing he will not order at me
	protected int desiredInventoryLevel;
	protected int nextDemand; // demand of next tick
	protected int price; // price for our goods
	protected int orderQuantity; // the quantity should be ordered this tick
	protected DeliveryAgent deliveryAgent;
	protected ProductionAgent productionAgent;
	protected ArrayList<DeliveryAgent> deliveryAgents;
	protected int lastOrderUpToLevel = -1;
	protected int lastDemand = 0;
	
	private Map<OrderAgent, Integer> buyer = new HashMap<OrderAgent, Integer>();
	
	public BuySale(ArrayList<Sale> sailorList, int incomingInventoryLevel, int outgoingInventoryLevel,Profile p) 
	{
		super(sailorList, incomingInventoryLevel, outgoingInventoryLevel, p);
	}
	
	// receiving the shipments that was delivered last tick
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
		nextDemand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
		//desiredInventoryLevel = nextDemand * 15 / 10;
		lastOrderUpToLevel = (lastOrderUpToLevel != -1) ? nextDemand : lastOrderUpToLevel;
		
		int orderUpToLevel = lastOrderUpToLevel + 1*(nextDemand - lastDemand);
		
		desiredInventoryLevel = orderUpToLevel;
		lastDemand = nextDemand;
		lastOrderUpToLevel = orderUpToLevel;
		
		// 2.
		currentOutgoingInventoryLevel = this.inventoryAgent.getOutgoingInventoryLevel();
		// if current bigger than desiredlevel return
		if (currentOutgoingInventoryLevel > desiredInventoryLevel) {
			return;
		}
		
		// 3.
		TrustSetter s = TrustSetter.getInstance();
		if (s.getInformationSharingIntegrated()) {
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
			//orderAgent.order(this.trustAgent, null);
		} else {
			Order order = new Order(orderQuantity, this.orderAgent);
			
			// Choose seller
			orderAgent.order(this.trustAgent, order);
			// if he is lying he will order the same at a second supplier
			if (lying) {
				Order order2 = new Order(orderQuantity, this.orderAgent);
				orderAgent.secondOrder(this.trustAgent, order2);
			}
		}
	}
	
	// just a method used if he is a lying agent 
	public int desired()
	{
		if (lying) {
			nextDemand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
			desiredInventoryLevel = nextDemand * 15 / 10;
			// System.out.println("desiredInventoryLevel" + desiredInventoryLevel);
			return desiredInventoryLevel;
		}
		return 0;
		//return 1000;
	}
	
	// if a possible buyer trust this actor enough, but will not order at him, he will tell it
	public void going2order(OrderAgent noOrderer)
	{
		if (buyer.containsKey(noOrderer)) {
			subtractionByTrust+=buyer.get(noOrderer);
		}	
	}
	
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