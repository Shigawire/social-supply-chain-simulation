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
	protected int almostFinished;
	
	public Wholesaler(ArrayList<Distributor> distributor_list, int price, int current_incoming_inventory_level,int current_outgoing_inventory_level) 
	{
		super(current_incoming_inventory_level, current_outgoing_inventory_level);
		delivery_agents = new ArrayList<DeliveryAgent>();
		
		for (Distributor distributor : distributor_list)
		{
			delivery_agents.add(distributor.getDeliveryAgent());
		}

		this.price = price;
		trustAgent = new TrustAgent(delivery_agents);
		this.procurementAgent=new ProcurementAgent(delivery_agents, trustAgent);
		orderAgent = new OrderAgent(this, procurementAgent);
		
		deliveryAgent = new DeliveryAgent(price);
		this.productionQueue=0;
		this.almostFinished=0;
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 3)
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
		this.inventoryAgent.increaseOutgoingInventoryLevel(almostFinished);
		almostFinished = productionQueue;
		productionQueue = this.inventoryAgent.getIncomingInventoryLevel();
		this.inventoryAgent.setIncomingInventoryLevel(0);
	}
}