package actors;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;
import SimulationSetups.TrustSetter;
import agents.DeliveryAgent;
import agents.ProductionAgent;
import artefacts.Order;

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
	//method for every run, start: start tick, priority: which priority it has in the simulation(higher --> better priority)	
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
	public void order() 
	{
		// 1. need in the next tick
		// 2. whats about my inventory
		// 3. order difference: +shortage-the value I do not need because of information sharing
		
		// 1. multiplied with 2 because he need twice of the outgoing because ot the production process
		next_demand = 2*(this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders()));
		desired_inventory_level = next_demand*15/10;
		// 2.
		current_outgoing_inventory_level = this.inventoryAgent.getOutgoingInventoryLevel();
		//if current bigger than desiredlevel return
		if(current_outgoing_inventory_level>desired_inventory_level){
			//deliveryAgent.setShortage(0);
			return;
		}
		// 3.
		TrustSetter s = TrustSetter.getInstance();
		if(s.getInformationSharingIntegrated()==true)
		{
		order_quantity = next_demand - current_outgoing_inventory_level+ deliveryAgent.getShortage()-subtractionByTrust;
		}
		else
		{
			order_quantity = next_demand - current_outgoing_inventory_level+ deliveryAgent.getShortage();
		}
		order_quantity = next_demand - current_outgoing_inventory_level+ deliveryAgent.getShortage()-subtractionByTrust;
		subtractionByTrust=0;
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
			// Choose seller
			orderAgent.order(this.trustAgent, order);
			//if he is lying he will order the same at a second supplier
			if(lying){
				Order order2 = new Order(order_quantity, this.orderAgent);
				orderAgent.secondOrder(this.trustAgent, order2);
			}
		}
	}
}