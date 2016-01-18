package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
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
public class Retailer extends Buy_Sale
{
	public Retailer(ArrayList<Sale> sailor_list,int incoming_inventory_level, int outgoing_inventory_level,int price) 
	{
		super(sailor_list, incoming_inventory_level, outgoing_inventory_level);
		
		this.price=price;
		deliveryAgent = new DeliveryAgent(price, this);
		this.productionQueue=0;

	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 4)
	public void run() 
	{
		// 1. processShipments() receive shipments
		this.receiveShipments();
		// 2. updateTrust()
		orderAgent.clearReceivedShipments();
		// 3. produce
		this.produce();
		// 4. deliver()
		this.deliver();
		// 5. calculateDemand() wird in order gemacht
		//next_demand = this.forecastAgent.calculateDemand();
		// 6. order()
		this.order();
	}
	private void produce(){
		this.inventoryAgent.increaseOutgoingInventoryLevel(productionQueue);
		productionQueue = this.inventoryAgent.getIncomingInventoryLevel();
		this.inventoryAgent.setIncomingInventoryLevel(0);
	}
	/**
	   * This method receives goods at the beginning of each tick
	   * 
	   * @return Nothing.
	   */
	
	
	/* 
	 * SETTERS
	 */
}