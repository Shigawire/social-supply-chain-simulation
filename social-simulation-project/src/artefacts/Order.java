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
	private final int quantity;
	private int ordered_at; // tick
	private int received_at;
	int oftenProcessed=0;
	private String id;
	// Who ordered?
	private OrderAgent orderAgent;
	private DeliveryAgent deliveryAgent;
	
	private double expectedDelivery;
	
	// Order received and sent
	private boolean processed;
	private int sum;
	
	private double shipmentQuality;
	
	public Order(int quantity, OrderAgent orderAgent) 
	{
		
		OrderObserver.giveObserver().addAmount(quantity);
		this.quantity = quantity;
		//generate an ID, so it is easier to track the Order through the system :)
		this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
		this.ordered_at = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		this.orderAgent = orderAgent;
		this.processed = false;
		System.out.println(id+" " +quantity+" "+oftenProcessed);
		sum=quantity;
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
		oftenProcessed++;
		System.out.println(id+" " +quantity+" "+oftenProcessed);
		OrderObserver.giveObserver().subAmount(quantity);
		this.processed = true;
		
	}
	
	public void setExpectedDeliveryDuration(double duration) {
		this.expectedDelivery = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount() + duration;
	}
	
//	public void setQuantity(int quantity) 
//	{
//		this.quantity = quantity;
//	}
	public void setSum(int quantity){
		this.sum = quantity;
	}
	public int getSum(){
		return sum;
	}
	
	public void setDeliveryAgent(DeliveryAgent deliveryAgent) {
		this.deliveryAgent = deliveryAgent;
	}
	
	public double getShipmentQuality() {
		return this.shipmentQuality;
	}
	
	public void setShipmentQuality(double shipmentQuality) {
		this.shipmentQuality = shipmentQuality;
	}
}