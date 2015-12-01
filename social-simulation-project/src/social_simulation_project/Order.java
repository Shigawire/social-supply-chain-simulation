package social_simulation_project;

import repast.simphony.engine.environment.RunEnvironment;

/**
* This class represents an order. 
*
* @author  Fabian 
* @since   ?
*/
public class Order 
{
	private int quantity;
	private int ordered_at;
	private int backlog;
	private String id;
	private OrderAgent orderAgent;
	private boolean processed;
	
	public Order(int quantity, OrderAgent orderAgent) 
	{
		this.quantity = quantity;
		//generate an ID, so it is easier to track the Order through the system :)
		this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
		this.ordered_at = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		this.orderAgent = orderAgent;
		this.processed = false;
	}
	
	public boolean finished() 
	{
		return (this.backlog > 0);
	}
	
	/*
	 * GETTERS
	 */
	public OrderAgent getOrderAgent() 
	{
		return this.orderAgent;	
	}
	
	public int getAmount() 
	{
		return this.quantity;
	}
	
	public String getId() 
	{
		return this.id;
	}
	
	public int getOrderDate() 
	{
		return this.ordered_at;
	}
	
	public SupplyChainMember getOrderer() 
	{
		return this.orderAgent.getOrderer();
	}
	
	public int getQuantity() 
	{
		return this.quantity;
	}
	
	/*
	 * SETTERS
	 */
	public void setProcessed(boolean pr) 
	{
		this.processed = true;
	}
	
	public void setQuantity(int quantity) 
	{
		this.quantity = quantity;
	}
}