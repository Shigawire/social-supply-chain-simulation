package social_simulation_project;

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
	private int demand_amount;
	private int next_demand;
	private int current_inventory_level;
	private int order_quantity;
	
	public OrderAgent orderAgent;
	
	/**
	   * This constructor gives the customer its own inventory
	   * agent and order agent.
	   * 
	   */
	public Customer() 
	{
		inventoryAgent = new InventoryAgent();
		orderAgent = new OrderAgent(this);
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
	public void run() 
	{
		//TODO
		//1. processShipments()
		this.processShipments();
		//2. updateTrust()
		//3. consume()
		this.consume();
		//4. calculateDemand()
		next_demand = this.forecastAgent.calculateDemand();
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
		demand_amount = 10;//forecastAgent.getNextDemand();
		current_inventory_level = inventoryAgent.getInventoryLevel();
		if (demand_amount > current_inventory_level) 
		{
			//TODO strafkosten/reaktion
			//Inventory ist geringer als Nachfrage
			inventoryAgent.setInventoryLevel(0);
		} 
		else 
		{
			inventoryAgent.setInventoryLevel(current_inventory_level - demand_amount);
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
		order_quantity = next_demand - inventoryAgent.getInventoryLevel();//
		//TODO replenishment policy
		if (order_quantity <= 0) return;
		
		Order order = new Order(order_quantity, this.orderAgent);
		//Choose retailer
		orderAgent.order(this.trustAgent, order);
	}
	
	/**
	   * This method receives goods at the beginning of each tick
	   * 
	   * @return Nothing.
	   */
	private void processShipments() 
	{
		this.orderAgent.processShipments(this.inventoryAgent);
	}

	/*
	 * GETTERS
	 */
	
	
	/* 
	 * SETTERS
	 */
}