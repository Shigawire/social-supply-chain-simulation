package artefacts;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.engine.environment.RunEnvironment;
import social_simulation_project.BWeffectMeasurer;
import social_simulation_project.OrderObserver;
import actors.SupplyChainMember;
import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;

/**
* This class represents an order. 
*
* @author  PS Development Team
* @since   ?
*/
public class Order 
{
	//depicts the quantity of the order
	private int quantity;
	
	private int sum;
	
	//depicts the percentage of how many items are arriving in a "damaged" state
	private double failurePercentage;	
	
	//The tick the order is placed
	private int orderedAt; 
	
	//The tick the order is fully received and 100% fulfilled
	private int receivedAt;
	
	//has the order been cancelled?
	boolean cancelled = false;
	
	// Who ordered?
	private OrderAgent orderAgent;
	
	//who is the supplier for the order?
	private DeliveryAgent deliveryAgent;
	
	//expected tick when the order will be fully processed
	private double expectedDelivery;
	
	// is completely processed --> done
	private boolean processed; 
	
	//private int sum;
	
	//How many items from the desired quantity are already fulfilled?
	private int fullfilledQuantity;
	
	//current part Delivery
	private int partDelivery;
	
	private double shipmentQuality;
	
	private Map<Integer, Integer> partialDeliveryHistory = new HashMap<Integer, Integer>();
	
	public Order(int quantity, OrderAgent orderAgent) 
	{
		OrderObserver.giveObserver().addAmount(quantity);
		
		this.quantity = quantity;
		this.sum = quantity;
		this.orderedAt = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		this.orderAgent = orderAgent;
		this.fullfilledQuantity = 0;
		this.partDelivery = 0;
		this.processed = false;
		
		System.out.println(quantity);
		BWeffectMeasurer.getMeasurer().update(this);
	}
	
//	public boolean finished() 
//	{
//		return (this.backlog > 0);
//	}
	
	private void received() 
	{
		this.receivedAt = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	}
	
	// a part or the whole delivery
	public void fulfill(int delivery)
	{	
		this.partDelivery = delivery;
		OrderObserver.giveObserver().subAmount(delivery);
		fullfilledQuantity += delivery;
		if (this.fullfilledQuantity == this.quantity) {
			this.received();
		}
		
		this.partialDeliveryHistory.put((int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount(), fullfilledQuantity);
	}
	
	/*
	 * GETTERS
	 */
	public int getPartDelivery()
	{
		return partDelivery;
	}
	
	public OrderAgent getOrderAgent() 
	{
		return this.orderAgent;	
	}
	
	
	public int getOrderedAt() 
	{
		return this.orderedAt;
	}
	
	public SupplyChainMember getOrderer() 
	{
		return this.orderAgent.getOrderer();
	}
	
	public int getQuantity() 
	{
		return this.quantity;
	}
	
	public double getfailurePercentage()
	{
		return this.failurePercentage;
	}
	
	public int getUnfullfilledQuantity()
	{
		return this.quantity-this.fullfilledQuantity;
	}
	
	public int getFullfilledQuantity()
	{
		return this.fullfilledQuantity;
	}
	
	public boolean getProcessed() 
	{
		// TODO Auto-generated method stub
		return processed;
	}
	
	public int getReceivedAt() 
	{
		return this.receivedAt;
	}
	
	public DeliveryAgent getDeliveryAgent() 
	{
		return this.deliveryAgent;
	}
	
	public double getExpectedDeliveryDate()
	{
		return this.expectedDelivery;
	}
	
	public double getShipmentQuality() 
	{
		return this.shipmentQuality;
	}
	
	public int getSum()
	{
		return sum;
	}
	
	public boolean getCancelled() 
	{
		return cancelled;	
	}

	/*
	 * SETTERS
	 */
	public void setProcessed(boolean processed) 
	{
		// set the time it needed to deliver
		if (!cancelled) {
			ProcurementAgent receiver = (ProcurementAgent) orderAgent.getParent().getProcurementAgent();
			receiver.updateShipmentRuntime(this.getOrderedAt() - this.getReceivedAt(), deliveryAgent);
		}
		// System.out.println(id + " " + quantity +" " + oftenProcessed);
		this.processed = processed;	
	}
	
	public void setExpectedDeliveryDuration(double duration) 
	{
		//TODO evaluate in which way this changes the trust assessment quality
        //this.expectedDelivery = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount() + duration;
		this.expectedDelivery = this.orderedAt + duration;
	}
	
	public void setCancelled() 
	{
		cancelled = true;
		// TODO Auto-generated method stub	
	}
	
//	public void setQuantity(int quantity) 
//	{
//		this.quantity = quantity;
//	}
	
	public void setfailurePercentage(double failure)
	{
		this.failurePercentage = failure;
	}
	
	public void setSum(int quantity) 
	{
		this.sum = quantity;
	}
	
	public void setDeliveryAgent(DeliveryAgent deliveryAgent) 
	{
		this.deliveryAgent = deliveryAgent;
	}
	
	public void setShipmentQuality(double shipmentQuality) 
	{
		this.shipmentQuality = shipmentQuality;
	}
	
	public Map<Integer, Integer> getPartialDeliveryHistory() 
	{
		return this.partialDeliveryHistory;
	}
}