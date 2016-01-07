package artefacts;

import actors.SupplyChainMember;
import agents.OrderAgent;
import repast.simphony.engine.environment.RunEnvironment;

/**
* This class represents an order. 
*
* @author  PS Development Team
* @since   ?
*/
public class Order 
{
	private int quantity;
	private int ordered_at; // tick
	private int received_at;
	private String id;
	// Who ordered?
	private OrderAgent orderAgent;
	
	
	// Order received and sent
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
	
//	public boolean finished() 
//	{
//		return (this.backlog > 0);
//	}
	
	public void received() 
	{
		this.received_at = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	}
	
	/*
	 * GETTERS
	 */
	public OrderAgent getOrderAgent() 
	{
		return this.orderAgent;	
	}
	
	public String getId() 
	{
		return this.id;
	}
	
	public int getOrderedAt() 
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
	
	public boolean getProcessed() 
	{
		// TODO Auto-generated method stub
		return processed;
	}
	
	public int getReceivedAt() 
	{
		return this.received_at;
	}
	
	/*
	 * SETTERS
	 */
	public void setProcessed(boolean processed) 
	{
		this.processed = true;
		
	}
	
	public void setQuantity(int quantity) 
	{
		this.quantity = quantity;
	}
}