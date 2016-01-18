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
	public Retailer(ArrayList<Wholesaler> wholesaler_list, int price, int current_inventory_level) 
	{
		super(current_inventory_level);
		delivery_agents = new ArrayList<DeliveryAgent>();
		
		for (Wholesaler wholesaler : wholesaler_list)
		{
			delivery_agents.add(wholesaler.getDeliveryAgent());
		}

		this.price = price;
		trustAgent = new TrustAgent(delivery_agents, this.dimensionRatings);
		this.procurementAgent=new ProcurementAgent(delivery_agents, trustAgent);
		orderAgent = new OrderAgent(this, procurementAgent);
		deliveryAgent = new DeliveryAgent(price, this);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 4)
	public void run() 
	{
		// 1. processShipments() receive shipments
		this.receiveShipments();
		// 2. updateTrust()
		orderAgent.clearReceivedShipments();
		// 3. deliver()
		this.deliver();
		// 4. calculateDemand() wird in order gemacht
		//next_demand = this.forecastAgent.calculateDemand();
		// 5. order()
		this.order();
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