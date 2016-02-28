package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
import agents.ProductionAgent;
import agents.TrustAgent;
import artefacts.Order;
import artefacts.Profile;
import repast.simphony.engine.schedule.ScheduledMethod;

/**
* This class represents a distributor. Distributors do not
* differ from retailers or wholesalers. The only 
* difference is their position in the supply chain.
*
* @author  PS Development Team
* @since   2015-12-04
*/
public class Distributor extends BuySale 
{
	public Distributor(ArrayList<Sale> sailorList, int incomingInventoryLevel, int outgoingInventoryLevel, int price, Profile p) 
	{
		super(sailorList, incomingInventoryLevel, outgoingInventoryLevel,p);
		
		this.price = price;
		
		this.deliveryAgent = new DeliveryAgent(price, this, 2, 2);
		this.productionAgent = new ProductionAgent(1, 1, this.inventoryAgent);
	}
	
	// method for every run, start: start tick, priority: which priority it has in the simulation(higher --> better priority)
	@ScheduledMethod(start = 1, interval = 1, priority = 2)
	public void run() 
	{
		// 1. harvest
		this.harvest();		
		// set the inventory agents desired level(doubled because of prouction process)
		inventoryAgent.desiredLevel(lying, desired());
		// 2. processShipments() receive shipments
		this.receiveShipments();
		// clear receivedShipments;
		orderAgent.clearReceivedShipments();
		// 3. produce
		this.produce();
		// 4. deliver()
		this.deliver();
		// 5.send order that he made the last tick
		orderAgent.orderIt();
		// 6. order()
		this.order();
		// 7. say those suppliers which I trust, that I will not order at them
		orderAgent.trustWhereIOrder();
	}
	
	private void harvest()
	{
		this.productionAgent.harvest();
	}
	
	public void produce()
	{
		this.productionAgent.produce1();
	}	
}