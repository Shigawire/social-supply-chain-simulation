package agents;

import java.util.ArrayList;

import actors.Sale;
import actors.SupplyChainMember;
import artefacts.Order;
import social_simulation_project.OrderObserver;

/**
* This class represents a delivery agent. They are 
* responsible for delivery (only retailers, wholesalers,
* distributors and the manufacturer). 
* Delivery agents communicate with
* - the user's inventory agent
* - the callee's order agent
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class OrderAgent 
{
	private SupplyChainMember parent;
	//list of received Orders
	private ArrayList<Order> receivedShipments;
	//List which orders have to be made amd which this tick
	private ArrayList<Order> nextTickOrder= new ArrayList<Order>();
	//where will I order next ticks
	ArrayList<DeliveryAgent> willOrder= new ArrayList<DeliveryAgent>();
	private ProcurementAgent procurementAgent;
	private ArrayList<DeliveryAgent> delivery_agents;
	private int thisTickReceived;
	public OrderAgent(SupplyChainMember orderer, ProcurementAgent procurementAgent, ArrayList<DeliveryAgent> delivery_agents) 
	{
		this.parent = orderer;
		this.receivedShipments = new ArrayList<Order>();
		this.procurementAgent=procurementAgent;
		this.delivery_agents=delivery_agents;
	}
	public void trustWhereIOrder(){
		//for every supplier he trust more then 0.3 he tells if he will not order at him 
		for (DeliveryAgent deliverer : delivery_agents)
		{
			if((parent.getTrustAgent().getTrustValue(deliverer))>0.3&&!willOrder.contains(deliverer)){	
				deliverer.getParent().going2order(this);
			}
		}
		willOrder.clear();
	}
	//send the orders were made last tick
	public void orderIt() {
		//do the last tick order
		
		if(!nextTickOrder.isEmpty()){
			for (Order orderToDo : nextTickOrder)
			{
				if(orderToDo!=null){
					orderToDo.getDeliveryAgent().receiveOrder(orderToDo);
				}

			}
		}
		nextTickOrder.clear();
		
	}
	// order at the by the procuremnt agent choosen deliverer
	//order will be recieved one tick later
	//Because of the structure an order (even one that is empty) has to be made every tick
	public void order(TrustAgent trustAgent, Order order) 
	{
		// e.g. select Retailer. with customer.procurementAgent
		if(order!=null){
			DeliveryAgent deliveryAgent=procurementAgent.chooseSupplier();
			//the delivery Agent is put into a list where are all at which I want to order
			willOrder.add(deliveryAgent);
			double expectedDeliveryDuration = deliveryAgent.getExpectedDeliveryTime();
			order.setDeliveryAgent(deliveryAgent);
			order.setExpectedDeliveryDuration(expectedDeliveryDuration);
			//if trustvalue > 0.6 immediatly order the last and the actual order
			if((parent.getTrustAgent().getTrustValue(order.getDeliveryAgent()))>0.6){
				deliveryAgent.receiveOrder(order);
				//return because all orders are send!
				return;
			}
			
		}
		//add the open order
		nextTickOrder.add(order);
		
		
	}
	//for a second order will be used by the lying agent
	public void secondOrder(TrustAgent trustAgent, Order order) 
	{
		// e.g. select Retailer. with customer.procurementAgent
		if(order!=null){
			DeliveryAgent deliveryAgent=procurementAgent.chooseSecondSupplier();
			//the delivery Agent is put into a list where are all at which I want to order
			willOrder.add(deliveryAgent);
			double expectedDeliveryDuration = deliveryAgent.getExpectedDeliveryTime();
			order.setDeliveryAgent(deliveryAgent);
			order.setExpectedDeliveryDuration(expectedDeliveryDuration);
			//if trustvalue > 0.6 immediatly order the last and the actual order
			if((parent.getTrustAgent().getTrustValue(order.getDeliveryAgent()))>0.6){
				deliveryAgent.receiveOrder(order);
				//return because all orders are sent!
				return;
			}
			
		}
		//add the open order
		nextTickOrder.add(order);
		
		
	}
	public void deliverRawMaterialA(int neededAmount)
	{
		
	}
	
	
	/*
	 * from the Superagent like customer
	 */
	public void receiveShipments(InventoryAgent inventoryAgent) 
	{	
		//System.out.println("[Order Agent] receiving shipment list");
		thisTickReceived = 0;
		if (!receivedShipments.isEmpty())
		{
			for (Order shipment : receivedShipments) 
			{
				thisTickReceived+=shipment.getQuantity();
				inventoryAgent.store(shipment);
				//if the shipment was not cancelled and if it is finally completely fulfilled it will be inspected
				if(!shipment.getCancelled() && shipment.getProcessed()){
					parent.getTrustAgent().inspectShipment(this, shipment);
				}

			}
		}
		receivedShipments.clear();
	}
	
	/*
	 * from delivery agent of the next tier
	 * this structure for one tick delay between receive by the order and actual receiving by the super agent
	 */
	public void receiveShipment(Order shipment, DeliveryAgent deliverer) 
	{
		//System.out.println("[Order Agent] received shipment with qty "+shipment.getQuantity());
		//set time of the first received
		//shipment.received();
		//information for the procurement agent 
		
		receivedShipments.add(shipment);
//		receivedOrders.add(shipment);
	}
	
	/*
	 * GETTERS
	 */
	public SupplyChainMember getOrderer() 
	{	
		return parent;
	}
	
	public SupplyChainMember getParent() {
		return parent;
	}
	
	public void clearReceivedShipments() 
	{
		receivedShipments.clear();
	}
	
	public ArrayList<Order> getReceivedShipments() 
	{
		return this.receivedShipments;
	}
	public int getThisTickReceived(){
		return thisTickReceived;
	}
	
	/*
	 * SETTERS
	 */
}