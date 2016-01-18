package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
import agents.TrustAgent;
import repast.simphony.engine.schedule.ScheduledMethod;

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
	public Wholesaler(ArrayList<Distributor> distributor_list, int price, int current_inventory_level) 
	{
		super(current_inventory_level);
		delivery_agents = new ArrayList<DeliveryAgent>();
		
		for (Distributor distributor : distributor_list)
		{
			delivery_agents.add(distributor.getDeliveryAgent());
		}

		this.price = price;
		trustAgent = new TrustAgent(delivery_agents, this.dimensionRatings);
		this.procurementAgent=new ProcurementAgent(delivery_agents, trustAgent);
		orderAgent = new OrderAgent(this, procurementAgent);
		
		deliveryAgent = new DeliveryAgent(price, this);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 3)
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
}