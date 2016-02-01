package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
import agents.ProductionAgent;
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
		this.productionAgent = new ProductionAgent(1, 1,this.inventoryAgent);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 2)
	public void run() 
	{
		// 1. harvest
		this.harvest();
		// 2. processShipments() receive shipments
		this.receiveShipments();
		// 3. updateTrust()	
		this.updateTrust();
		//clear receivedShipments;
		orderAgent.clearReceivedShipments();
		// 4. produce
		this.produce();
		// 5. deliver()
		this.deliver();
		// 6. order()
		this.order();
	}
	
	private void harvest(){
		this.productionAgent.harvest();
	}
	private void produce(){
		this.productionAgent.label();
	}
	public void order() 
	{
		// 1. Was brauch ich im nächsten tick?  (forecastagent befragen)
		// 2. Was hab ich noch im Inventar?
		// 3. Differenz bestellen. mit orderArgent
		
		// 1.
		next_demand = 2*(this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders()));
		
		// 2.
		current_outgoing_inventory_level = this.inventoryAgent.getOutgoingInventoryLevel();
		
		// 3.
		order_quantity = next_demand - current_outgoing_inventory_level+ deliveryAgent.getShortage()-subtractionByTrust;
		subtractionByTrust=0;
		//System.out.println("[Buy_Sale] order_quantity is  " + order_quantity);
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (order_quantity < 0) 
		{
			//a order with quantity null has to be made for the process in the orderAgent
			// (realize the order of the last tick
			order_quantity=0;
			orderAgent.order(this.trustAgent, null);
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