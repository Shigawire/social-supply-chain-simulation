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
public class Retailer extends Buy_Sale
{
	public Retailer(ArrayList<Sale> sailor_list,int incoming_inventory_level, int outgoing_inventory_level,int price) 
	{
		super(sailor_list, incoming_inventory_level, outgoing_inventory_level);
		
		this.price=price;
		deliveryAgent = new DeliveryAgent(price, this,1,1);
		this.productionAgent = new ProductionAgent(1, 1,this.inventoryAgent);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 4)
	public void run() 
	{
		// 1. harvest()
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
		this.productionAgent.label();
	}
	//gives the desired level
	public int desired(){
		if(lying){
			next_demand = 2*(this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders()));
			desired_inventory_level = next_demand*15/10;
			System.out.println("desired_inventory_level"+desired_inventory_level);
			return desired_inventory_level;
		}
		return 1000;
		
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