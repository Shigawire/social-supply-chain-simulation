package actors;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;
import agents.DeliveryAgent;
import agents.ProductionAgent;

/**
* This class represents a wholesaler. Wholesalers do not
* differ from retailers or distributors. The only 
* difference is their position in the supply chain.
*
* @author  PS Development Team
* @since   2015-12-04
*/
public class Wholesaler extends Buy_Sale
{
	protected int almostFinished;
	
	public Wholesaler(ArrayList<Sale> sailor_list,int incoming_inventory_level, int outgoing_inventory_level, int price) 
	{
		super(sailor_list, incoming_inventory_level, outgoing_inventory_level);
		
		this.price=price;
	
		deliveryAgent = new DeliveryAgent(price, this,3,4);
		this.productionAgent = new ProductionAgent(2, 1,this.inventoryAgent);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 3)
	public void run() 
	{
		// 1. harvest
		this.harvest();
		//set the inventory agents desired level
		inventoryAgent.desiredLevel(lying, desired());		
		// 2. processShipments() receive shipments
		this.receiveShipments();
		// 3. updateTrust()	
		orderAgent.clearReceivedShipments();
		// 4. produce
		this.produce();
		// 5. deliver()
		this.deliver();
		//6.send order that he made the last tick
		orderAgent.orderIt();
		// 7. order()
		this.order();
		//8. say those suppliers which I trust, that I will not order at them
		orderAgent.trustWhereIOrder();
	}
	
	private void harvest(){
		this.productionAgent.harvest();
	}
	private void produce(){

		this.productionAgent.process();
	}
}