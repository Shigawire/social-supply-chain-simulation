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
* This class represents a distributor. 
*
* @author  PS Development Team
* @since   2015-12-04
*/
public class Distributor extends BuySale 
{
	//construct a Distributor
	public Distributor(ArrayList<SellingActor> sellerList, int incomingInventoryLevel, int outgoingInventoryLevel, int price, Profile profile) 
	{
		super(sellerList, incomingInventoryLevel, outgoingInventoryLevel, profile);
		
		this.price = price;
		this.deliveryAgent = new DeliveryAgent(price, this, 2, 2);
		this.productionAgent = new ProductionAgent(1, 1, this.inventoryAgent, this);
	}
	
	// method for every run, start: start tick, priority: which priority it has in the simulation(higher --> better priority)
	@ScheduledMethod(start = 1, interval = 1, priority = 2)
	public void run() 
	{
		//move incoming items into the outgoing inventory
		this.productionAgent.transferInventories();		
		
		// set the inventory agents desired level(doubled because of production process)
		inventoryAgent.desiredLevel(this.lying, desired());
		
		// receive incoming shipments and process them
		this.receiveShipments();
		
		// clear receivedShipments - i.e. erase them from the incoming list.
		//This is a special case here - but since the receivedShipments are further processed we need to tell the orderAgent specifically to clear this list.
		orderAgent.clearReceivedShipments();
		
		// produce
		this.produce();
		// deliver produced items to the customers
		this.deliver();
		
		// Order the items at a supplier (chosen by the procurementAgent) that were defined in the previous tick
		// we are delaying orders here to better simulate the delay of information flow in a supply chain
		orderAgent.orderIt();
		
		// create new orders based on the inventory levels and demand
		this.order();
		
		// tell those suppliers which I trust, that I will not order at them
		orderAgent.trustWhereIOrder();
	}
	
	
	public void produce()
	{
		this.productionAgent.produce();
	}	
}