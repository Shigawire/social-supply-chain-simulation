package actors;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;
import SimulationSetups.TrustSetter;
import agents.DeliveryAgent;
import agents.ProductionAgent;
import artefacts.Order;
import artefacts.Profile;

/**
* This class represents a wholesaler. Wholesalers do not
* differ from retailers or distributors. The only 
* difference is their position in the supply chain.
*
* @author  PS Development Team
* @since   2015-12-04
*/
public class Wholesaler extends BuySale
{	
	//historical order up to value
	protected int lastOrderUpToLevel = -1;
	
	//historical demand
	protected int lastDemand = 0;
	
	public Wholesaler(ArrayList<SellingActor> sellerList, int incomingInventoryLevel, int outgoingInventoryLevel, int price, Profile p) 
	{
		super(sellerList, incomingInventoryLevel, outgoingInventoryLevel,p);
		
		this.price = price;
		deliveryAgent = new DeliveryAgent(price, this, 3, 4);
		this.productionAgent = new ProductionAgent(2, 1, this.inventoryAgent, this);
	}
	
	// method for every run, start: start tick, priority: which priority it has in the simulation(higher --> better priority)	
	@ScheduledMethod(start = 1, interval = 1, priority = 3)
	public void run() 
	{
		//move incoming items into the outgoing inventory
		
		this.productionAgent.transferInventories();
		
		// set the inventory agents desired level
		inventoryAgent.desiredLevel(isLying, desired());		
		
		//receive incoming shipments and process them
		this.receiveShipments();
		
		// clear receivedShipments - i.e. erase them from the incoming list.
		// This is a special case here - but since the receivedShipments are further processed we need to tell the orderAgent specifically to clear this list.
		orderAgent.clearReceivedShipments();
		
		// produce items
		this.produce();
		
		// deliver produced items to the customers
		this.deliver();
		
		// Order the items at a supplier (chosen by the procurementAgent) that were defined in the previous tick
		// we are delaying orders here to better simulate the delay of information flow in a supply chain
		orderAgent.orderIt();
		
		// create new orders for the inventory levels and demand.
		this.order();
		
		// tell those suppliers which I trust enough that I am NOT going to order at them
		orderAgent.trustWhereIOrder();
	}
	
	
	public void produce()
	{
		this.productionAgent.produce();
	}
	
	public void order() 
	{
		// 1. need in the next tick
		// 2. whats about my inventory
		// 3. order difference: +shortage-the value I do not need because of information sharing
		
		// 1. multiplied with 2 because he need twice of the outgoing because ot the production process
		nextDemand = 2*(this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders()));
		
		//if this is the first time that we're setting lastOrderUpToLevel use the nextDemand variable
		lastOrderUpToLevel = (lastOrderUpToLevel != -1) ? nextDemand : lastOrderUpToLevel;
		
		int orderUpToLevel = lastOrderUpToLevel + 1*(nextDemand - lastDemand);
		
		desiredInventoryLevel = orderUpToLevel;
		lastDemand = nextDemand;
		lastOrderUpToLevel = orderUpToLevel;
		
		
		// 2.
		int currentOutgoingInventoryLevel = this.inventoryAgent.getOutgoingInventoryLevel();
		// if current bigger than desiredlevel return
		if (currentOutgoingInventoryLevel > desiredInventoryLevel) {
			return;
		}
		// 3.
		TrustSetter s = TrustSetter.getInstance();
		if (s.getInformationSharingIntegrated() == true) {
			orderQuantity = nextDemand - currentOutgoingInventoryLevel + deliveryAgent.getShortage() - subtractionByTrust;
		} else {
			orderQuantity = nextDemand - currentOutgoingInventoryLevel + deliveryAgent.getShortage();
		}
		orderQuantity = nextDemand - currentOutgoingInventoryLevel + deliveryAgent.getShortage() - subtractionByTrust;
		subtractionByTrust = 0;
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (orderQuantity < 0) {
			// a order with quantity null has to be made for the process in the orderAgent
			// realize the order of the last tick
			orderQuantity = 0;
			orderAgent.order(this.trustAgent, null);
		} else {
			Order order = new Order(orderQuantity, this.orderAgent);
			// Choose seller
			orderAgent.order(this.trustAgent, order);
			// if he is lying he will order the same at a second supplier
			if (isLying) {
				Order order2 = new Order(orderQuantity, this.orderAgent);
				orderAgent.secondOrder(this.trustAgent, order2);
			}
		}
	}
}