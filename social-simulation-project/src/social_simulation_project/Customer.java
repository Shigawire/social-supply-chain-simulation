package social_simulation_project;

import java.util.ArrayList;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;

/**
* This class represents the customer. We have one
* customer in our supply chain. The customer is 
* the only supply chain member, which actually
* consumes goods. He cannot deliver.
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class Customer extends Buy 
{
	// what the customer consumes every tick
	private int consumption;
	
	// what the customer needs for the next tick (forecasted)
	private int next_demand;
	
	// what the customer orders at the end, based 
	// on next_demand and current_inventory_level
	private int order_quantity;
	
	private ArrayList<DeliveryAgent> delivery_agents;
	
	/**
	   * This constructor gives the customer its own inventory
	   * agent and order agent.
	   * 
	   */
	public Customer(ArrayList<Retailer> retailer_list, int inventory_level) 
	{
		super(inventory_level);
		delivery_agents = new ArrayList<DeliveryAgent>();
		
		for (Retailer retailer : retailer_list)
		{
			delivery_agents.add(retailer.getDeliveryAgent());
		}
		trustAgent = new TrustAgent(delivery_agents);
		orderAgent = new OrderAgent(this, this.procurementAgent);
		
		this.procurementAgent = new ProcurementAgent(delivery_agents, trustAgent);
	}
	
	/**
	   * The run method of for the simulation. It represents
	   * the customer's process in every tick.
	   * 1. Receive goods from previous tick.
	   * 2. Update trust based upon delivery experience.
	   * 3. Consume goods.
	   * 4. Calculate demand for the next tick.
	   * 5. Order to match demand for the next tick based upon forecasted demand.
	   * 
	   * @return Nothing.
	   */
	@ScheduledMethod(start = 1, interval = 1, priority = 5)
	public void run() 
	{
		if ((int)RepastEssentials.GetTickCount()==1)
		{
			RunEnvironment.getInstance().setScheduleTickDelay(30);
		}
		
		System.out.println("[Customer] 1. my inventory Level is " + inventoryAgent.getInventoryLevel());
		//1. processShipments()
		this.receiveShipments();
		System.out.println("[Customer] 2. received shipments. Now my inventory Level is " + inventoryAgent.getInventoryLevel());
		
		orderAgent.clearReceivedShipments();
		//2. updateTrust()
		// this.trustAgent.update();
		//3. consume()
		this.consume();
		//4. calculateDemand() ist glaube ich unn�tig sieher Methode order!!!
		//!
		//!
		// 4. calculateDemand() wird in order gemacht
		//next_demand = this.forecastAgent.calculateDemand();
		//5. order()
		this.order();
	}
	
	/**
	   * This method consumes goods and refreshes the
	   * inventory level based upon the actual received 
	   * amount of goods.
	   * The value, that is consumed CAN be different than
	   * the forecasted demand of the previous tick. 
	   * 
	   * @return Nothing.
	   */
	public void consume()
	{
		//TODO temporär, muss noch implementiert werden
		consumption = 10; //forecastAgent.getNextDemand();
		current_inventory_level = inventoryAgent.getInventoryLevel();
		
		if (consumption > current_inventory_level) 
		{
			//TODO strafkosten/reaktion
			//Inventory ist geringer als Nachfrage
			inventoryAgent.setInventoryLevel(0);
		} 
		else 
		{
			inventoryAgent.setInventoryLevel(current_inventory_level - consumption);
		}
	}
	
	/**
	   * This method orders goods at the customer's
	   * supplier.
	   * 
	   * @return Nothing.
	   */
	public void order() 
	{
		// 1. Was brauch ich im naechsten tick?  (forecastagent befragen)
		// 2. Was hab ich noch im Inventar?
		// 3. Differenz bestellen. mit orderArgent
		
		// 1.
		next_demand = this.forecastAgent.calculateDemand();
		System.out.println("[Customer] Next demand is  " + next_demand);
		
		// 2.
		current_inventory_level = this.inventoryAgent.getInventoryLevel();
		
		System.out.println("[Customer] current_inventory_level is  " + this.inventoryAgent.getInventoryLevel());
		
		
		// 3.
		order_quantity = next_demand - current_inventory_level;
		
		System.out.println("[Customer] order_quantity is  " + order_quantity);

		// TODO replenishment policy
		
		// If the inventory level is sufficient for the next demand,
		// do not order
		if (order_quantity <= 0) 
		{
			return;
		}
		else
		{
			Order order = new Order(order_quantity, this.orderAgent);
			
			// Choose retailer
			
			orderAgent.order(this.trustAgent, order);
		}
	}
	
	/**
	   * This method receives goods at the beginning of each tick
	   * 
	   * @return Nothing.
	   */
	public void receiveShipments() 
	{
		this.orderAgent.receiveShipments(this.inventoryAgent);
	}

	/*
	 * GETTERS
	 */
	 
	/* 
	 * SETTERS
	 */
}