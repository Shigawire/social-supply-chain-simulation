package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
import agents.TrustAgent;
import artefacts.Order;
import artefacts.Profile;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import social_simulation_project.BWeffectMeasurer;

/**
* This class represents the customer. We have one
* customer in our supply chain. The customer is 
* the only supply chain member, which actually
* consumes goods. He cannot deliver.
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class Customer extends BuyingActor 
{
	
	private int lastOrderUpToLevel = -1;
	private int lastDemand = 0;
	private int nextDemand = 0;
	/**
	   * This constructor gives the customer its own inventory
	   * agent and order agent.
	   */

	public Customer(ArrayList<SellingActor> sellerList, int incomingInventoryLevel, int outgoingInventoryLevel, Profile profile) 
	{
		//call BuyingActor with the parameters
		super(sellerList, incomingInventoryLevel, outgoingInventoryLevel, profile);
	}
	
	/**
	   * The run method of for the simulation. It represents
	   * the customer's process in every tick.
	   * 1. Receive goods from previous tick.
	   * 2. Update trust based upon delivery experience.
	   * 3. Consume goods.
	   * 4. Calculate demand for the next tick.
	   * 5. Order to match demand for the next tick based upon forecasted demand.
	   * 
	   */
	//method for every run, start: start tick, priority: which priority it has in the simulation(higher --> better priority)
	@ScheduledMethod(start = 1, interval = 1, priority = 5)
	public void run() 
	{	

		// set the inventory agents desired level for the incoming inventory
		inventoryAgent.desiredLevel(lying, desiredInventoryLevel());
		// 1. processShipments()
		this.receiveShipments();
		orderAgent.clearReceivedShipments();
		// 2. consume()
		this.consume();
		
		//calculate the demand for the next tick
		calculateNextDemand();
		
		// 3.send order that he made the last tick
		orderAgent.orderIt();
		// 4. order()
		this.order();
		// 5. say those suppliers which I trust, that I will not order at them
		orderAgent.trustWhereIOrder();
	}
	
	//TODO comment
	private int desiredInventoryLevel() 
	{
		lastOrderUpToLevel = (lastOrderUpToLevel != -1) ? this.nextDemand : lastOrderUpToLevel;
		
		int orderUpToLevel = lastOrderUpToLevel + 1*(this.nextDemand - lastDemand);
		
		this.lastDemand = this.nextDemand;
		
		return orderUpToLevel;
	}
	
	private void calculateNextDemand() {
		this.nextDemand = forecastAgent.customerDemand();
	}

	/**
	   * This method consumes goods and refreshes the
	   * inventory level based upon the actual received 
	   * amount of goods.
	   * The value, that is consumed CAN be different than
	   * the forecasted demand of the previous tick. 
	   * 
	   */
	
	public void consume()
	{
		// The customer is supposed to consume 10 on every tick
		int consumption = 10;
		int currentIncomingInventoryLevel = inventoryAgent.getIncomingInventoryLevel();
		
		/**
		 * If the customer wants to consume more than the current inventory allows
		 * the remaining inventory will be consumed and the inventory be set to 0.
		 */
		this.inventoryAgent.reduceIncomingInventoryLevel(consumption);
		if (consumption > currentIncomingInventoryLevel) {
			// Inventory can provide less than requested. Customer consumes as much as possible and not more.
			this.inventoryAgent.setIncomingInventoryLevel(0);
		} else {
			this.inventoryAgent.reduceIncomingInventoryLevel(consumption);
		}
	}
	
	/**
	   * This method orders goods at the customer's
	   * supplier.
	   */
	public void order() 
	{

		//calculate the demand for the next tick
		
		//what's in the current inventory
		int currentIncomingInventoryLevel = this.inventoryAgent.getIncomingInventoryLevel();
		
		int orderQuantity = this.nextDemand - currentIncomingInventoryLevel;
		
		// If the inventory level is sufficient for the next demand, do not order
		if (orderQuantity <= 0) {
			//TODO fix this. Jakobs fault!
			// a order with quantity null has to be made for the process in the orderAgent
			// (realize the order of the last tick
			orderQuantity = 0;
		} else {
			
			//craft a new Order object
			Order order = new Order(orderQuantity, this.orderAgent);
			// Choose retailer
			orderAgent.order(this.trustAgent, order);
			// if he is lying he will order the same at a second supplier
			if (lying) {
				Order order2 = new Order(orderQuantity, this.orderAgent);
				orderAgent.secondOrder(this.trustAgent, order2);
			}
		}
	}
	
	
	/**
	   * This method receives shipments from outstanding orders at the beginning of each tick.
	   * 
	   */
	public void receiveShipments() 
	{
		//tell the order agent to receive shipments and use the inventory agent to store them.
		this.orderAgent.receiveShipments(this.inventoryAgent);
	}
	
	public OrderAgent getOrderAgent() 
	{
		return this.orderAgent;
	}
	
	public TrustAgent getTrustAgent() 
	{
		return this.trustAgent;
	}	 
	
	
	//The following methods are necessary for JUnit tests. They aren't being used anywhere else in the documentation
	public int getNextDemand() {
		return this.nextDemand;
	}
	
	public int getNextOrderQuantity() {
		return this.nextDemand - this.inventoryAgent.getIncomingInventoryLevel();
		
	}
}