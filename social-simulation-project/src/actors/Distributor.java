package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
import agents.TrustAgent;
import artefacts.Order;
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
	public Distributor(ArrayList<Sale> sailor_list,int incoming_inventory_level, int outgoing_inventory_level,int price) 
	{
		super(sailor_list, incoming_inventory_level, outgoing_inventory_level);
		
		this.price = price;
		
		this.deliveryAgent = new DeliveryAgent(price, this);
		this.productionQueue=0;
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
		productionQueue = this.inventoryAgent.getIncomingInventoryLevel()/2;
		this.inventoryAgent.setIncomingInventoryLevel(0);
	}
	public void order() 
	{
		// 1. Was brauch ich im nächsten tick?  (forecastagent befragen)
		// 2. Was hab ich noch im Inventar?
		// 3. Differenz bestellen. mit orderArgent
		
		// 1.
		next_demand = 2*(this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders()));
		
		// 2.
		current_inventory_level = this.inventoryAgent.getOutgoingInventoryLevel();
		
		// 3.
		order_quantity = next_demand - current_inventory_level+ deliveryAgent.getShortage();
		
		//System.out.println("[Buy_Sale] order_quantity is  " + order_quantity);
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (order_quantity < 0) 
		{
			//System.out.println("ja ich bin im if");
			order_quantity=0;
			return;
		}
		else
		{
			//System.out.println("[Buy_Sale] order_quantity is  " + order_quantity);
			Order order = new Order(order_quantity, this.orderAgent);
			
			// Choose retailer
			orderAgent.order(this.trustAgent, order);
		}
	}
}