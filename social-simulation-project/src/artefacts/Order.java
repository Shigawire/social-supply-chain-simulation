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
	//quantity that has to be completly fullfilled
	private final int quantity;
	private int ordered_at; // tick it is orderd
	private int received_at;//tick it is received
	private int oftenProcessed=0;//how often has parts of the order been processed
	private int firstDelivery; //how much was fullfilled in the first fullfillment
	private int firstTick;
	private String id;
	// Who ordered?
	private OrderAgent orderAgent;//who ordered
	private DeliveryAgent deliveryAgent;//who delivered
	
	private double expectedDelivery;//expected time the delivery will need
	
	// Order received and sent
	private boolean processed;//is completly processed --> done
	private int sum;
	private int fullfilledQuantity;//till now fullfilled quantity
	private int partDelivery=0;//partdelivery at the moment
	
	private double shipmentQuality;
	
	public Order(int quantity, OrderAgent orderAgent) 
	{
		
		OrderObserver.giveObserver().addAmount(quantity);
		this.quantity = quantity;
		//generate an ID, so it is easier to track the Order through the system :)
		this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
		this.ordered_at = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		this.orderAgent = orderAgent;
		fullfilledQuantity=0;
		this.processed = false;
		//System.out.println(id+" " +quantity+" "+oftenProcessed);
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
	//a part or the whole delivery
	public void partDelivery(int delivery){
		if(oftenProcessed==0){
			firstDelivery=delivery;
			firstTick=(int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		}
		oftenProcessed++;
		this.partDelivery=delivery;
		OrderObserver.giveObserver().subAmount(delivery);
		fullfilledQuantity+=delivery;

	}
	/*
	 * GETTERS
	 */
	public int getPartDelivery(){
		return partDelivery;
	}
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
	public int getUnfullfilledQuantity(){
		return this.quantity-this.fullfilledQuantity;
	}
	public int getFullfilledQuantity(){
		return this.fullfilledQuantity;
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
		System.out.println(id+" " +quantity+" "+oftenProcessed);
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