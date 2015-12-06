package social_simulation_project;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;

/**
* This class represents the customer. We have one
* customer in our supply chain. The customer is 
* the only supply chain member, which actually
* consumes goods. He cannot deliver.
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class Customer extends SupplyChainMember 
{
	// what the customer consumes every tick
	private int consumption;
	
	// what the customer needs for the next tick (forecasted)
	private int next_demand;
	
	private int current_inventory_level;
	
	// what the customer orders at the end, based 
	// on next_demand and current_inventory_level
	private int order_quantity;
	
	private ArrayList<DeliveryAgent> delivery_agents;
	public OrderAgent orderAgent;
	
	private boolean finished = false;
	
	
	/**
	   * This constructor gives the customer its own inventory
	   * agent and order agent.
	   * 
	   */
	public Customer(ArrayList<Retailer> retailer_list, int inventory_level) 
	{
		super(inventory_level);
		delivery_agents = new ArrayList();
		
		for (Retailer retailer : retailer_list)
		{
			delivery_agents.add(retailer.getDeliveryAgent());
		}

		orderAgent = new OrderAgent(this);
		trustAgent = new TrustAgent(delivery_agents);
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
	@ScheduledMethod(start = 1, interval = 1, priority = 2)
	public void run() 
	{
		
		//TODO
		//1. processShipments()
		this.receiveShipments();
		//2. updateTrust()
		// this.trustAgent.update();
		//3. consume()
		this.consume();
		//4. calculateDemand()
		next_demand = this.forecastAgent.calculateDemand();
		//5. order()
		this.order();
		
		this.finished = true;
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
		consumption = 10;//forecastAgent.getNextDemand();
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
	   * retailers.
	   * 
	   * @return Nothing.
	   */
	public void order() 
	{
		// 1. Was brauch ich im nächsten tick?  (forecastagent befragen)
		// 2. Was hab ich noch im Inventar?
		// 3. Differenz bestellen. mit orderArgent
		
		// 1.
		next_demand = this.forecastAgent.calculateDemand();
		
		// 2.
		current_inventory_level = this.inventoryAgent.getInventoryLevel();
		
		// 3.
		
		order_quantity = next_demand - current_inventory_level;
		System.out.println(order_quantity);
		
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
	private void receiveShipments() 
	{
		this.orderAgent.receiveShipments(this.inventoryAgent);
	}

	/*
	 * GETTERS
	 */
	public int getCurrent_inventory_level()
	{
		return this.inventoryAgent.getInventoryLevel();
	}
	
	/* 
	 * SETTERS
	 */
}