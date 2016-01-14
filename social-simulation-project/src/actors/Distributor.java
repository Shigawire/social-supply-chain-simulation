package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
import agents.TrustAgent;
import repast.simphony.engine.schedule.ScheduledMethod;

/**
* This class represents a distributor. Distributors do not
* differ from retailers or wholesalers. The only 
* difference is their position in the supply chain.
*
* @author  PS Development Team
* @since   2015-12-04
*/
public class Distributor extends Buy_Sale 
{
	public Distributor(ArrayList<Sale> sailor_list,int price, int current_inventory_level) 
	{
		super(sailor_list, current_inventory_level);
		delivery_agents = new ArrayList<DeliveryAgent>();
		
		
		
		this.price = price;

		this.procurementAgent=new ProcurementAgent(delivery_agents, trustAgent);
		this.orderAgent = new OrderAgent(this, procurementAgent);	
		this.deliveryAgent = new DeliveryAgent(price);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 2)
	public void run() 
	{
		// 1. processShipments() receive shipments
		this.receiveShipments();
		// 2. updateTrust()	
		this.updateTrust();
		//clear receivedShipments;
		orderAgent.clearReceivedShipments();
		// 3. deliver()
		this.deliver();
		// 4. calculateDemand() wird in order gemacht
		//next_demand = this.forecastAgent.calculateDemand();
		// 5. order()
		this.order();
	}
}