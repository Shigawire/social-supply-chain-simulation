package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
import agents.ProductionAgent;
import agents.TrustAgent;
import repast.simphony.engine.schedule.ScheduledMethod;

/**
* This class represents a retailer. Retailer do not
* differ from wholesalers or distributors. The only 
* difference is their position in the supply chain.
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class Retailer extends BuySale
{
	public Retailer(ArrayList<Sale> sailorList, int incomingInventoryLevel, int outgoingInventoryLevel, int price) 
	{
		super(sailorList, incomingInventoryLevel, outgoingInventoryLevel);
		
		this.price = price;
		deliveryAgent = new DeliveryAgent(price, this, 1, 1);
		this.productionAgent = new ProductionAgent(1, 1, this.inventoryAgent);
	}
	
	// method for every run, start: start tick, priority: which priority it has in the simulation(higher --> better priority)	
	@ScheduledMethod(start = 1, interval = 1, priority = 4)
	public void run() 
	{
		// 1. harvest()
		this.harvest();
		// set the inventory agents desired level
		inventoryAgent.desiredLevel(lying, desired());
		// 2. processShipments() receive shipments
		this.receiveShipments();
		// 3. updateTrust()
		orderAgent.clearReceivedShipments();
		// 4. produce
		this.produce();
		// 5. deliver()
		this.deliver();
		// 6.send order that he made the last tick
		orderAgent.orderIt();
		// 7. order()
		this.order();
		// 8. say those suppliers which I trust, that I will not order at them
		orderAgent.trustWhereIOrder();
	}
	
	private void harvest()
	{
		this.productionAgent.harvest();
	}
	
	private void produce() 
	{
		this.productionAgent.label();
	}
}