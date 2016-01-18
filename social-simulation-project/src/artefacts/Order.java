package artefacts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import social_simulation_project.OrderObserver;
import actors.SupplyChainMember;
import agents.DeliveryAgent;
import agents.OrderAgent;

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
	private DeliveryAgent deliveryAgent;
	
	private double expectedDelivery;
	
	// Order received and sent
	private boolean processed;
	
	public Order(int quantity, OrderAgent orderAgent) 
	{
		OrderObserver.giveObserver().addAmount(quantity);
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
	
	public DeliveryAgent getDeliveryAgent() {
		return this.deliveryAgent;
	}
	
	public double getExpectedDeliveryDate(){
		return this.expectedDelivery;
	}
	
	/*
	 * SETTERS
	 */
	public void setProcessed(boolean processed) 
	{
		OrderObserver.giveObserver().subAmount(quantity);
		this.processed = true;
		
	}
	
	public void setExpectedDeliveryDuration(double duration) {
		this.expectedDelivery = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount() + duration;
	}
	
	public void setQuantity(int quantity) 
	{
		this.quantity = quantity;
	}
	
	public void setDeliveryAgent(DeliveryAgent deliveryAgent) {
		this.deliveryAgent = deliveryAgent;
	}
	
	public double getShipmentQuality() {
		return RandomHelper.nextDoubleFromTo(0.95, 1);
	}
}