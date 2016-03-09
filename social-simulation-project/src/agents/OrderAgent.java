package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import SimulationSetups.TrustSetter;
import actors.SellingActor;
import actors.SupplyChainMember;
import artefacts.Order;
import artefacts.trust.DimensionType;
import artefacts.trust.TrustDimension;
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
	//based on profiles
	private double borderLargeTrust;
	private double borderMediumTrust;
	
	//map open orders at every deliveryAgent
	private Map<DeliveryAgent, Integer> numberOfOpenOrders = new HashMap<DeliveryAgent, Integer>();
	
	private SupplyChainMember parent;
	// list of received Orders
	private ArrayList<Order> receivedShipments;
	
	// List which orders have to be made the next tick
	private ArrayList<Order> nextTickOrder = new ArrayList<Order>();
	
	// All suppliers I am going to order during the next ticks. necessary for the indirect trust assessment
	ArrayList<DeliveryAgent> willOrder = new ArrayList<DeliveryAgent>();
	
	private ProcurementAgent procurementAgent;
	private ArrayList<DeliveryAgent> deliveryAgents;
	private int thisTickReceived;
	public OrderAgent(SupplyChainMember orderer, ProcurementAgent procurementAgent, ArrayList<DeliveryAgent> deliveryAgents) 
	{
		this.parent = orderer;
		this.receivedShipments = new ArrayList<Order>();
		this.procurementAgent = procurementAgent;
		this.deliveryAgents = deliveryAgents;
		borderLargeTrust=parent.getProfile().getOrderTickEarlier();
		borderMediumTrust=parent.getProfile().getWillNotOrder();
	}
	
	public void trustWhereIOrder()
	{
		// for every supplier he trust at least medium he tells if he will not order at him 
		for (DeliveryAgent deliverer : deliveryAgents)
		{
			if ((parent.getTrustAgent().getTrustValue(deliverer)) > borderMediumTrust && !willOrder.contains(deliverer)) {	
				deliverer.getParent().going2order(this);
			}
		}
		willOrder.clear();
	}
	
	// send the orders were made last tick
	public void orderIt() 
	{
		// do the last tick order
		if (!nextTickOrder.isEmpty()) {
			for (Order orderToDo : nextTickOrder)
			{
				if (orderToDo != null) {
					orderToDo.getDeliveryAgent().receiveOrder(orderToDo);
				}
			}
		}
		nextTickOrder.clear();
	}
	
	// order the procurement agent chosen supplier
	// order will be received one tick later
	// Because of the structure an order has to be made every tick
	public void order(TrustAgent trustAgent, Order order) 
	{
		if (order != null) {
			
			DeliveryAgent deliveryAgent = procurementAgent.chooseSupplier(numberOfOpenOrders);
			
			//add a new order to the global number of open orders mapped to the suppliers
			int _no_open_orders = 0;
			if(numberOfOpenOrders.containsKey(deliveryAgent)){
				_no_open_orders = (int)numberOfOpenOrders.get(deliveryAgent);
			}
			
			numberOfOpenOrders.put(deliveryAgent, _no_open_orders + 1);
			
			// the delivery Agent is put into a list where are all at which I want to order
			willOrder.add(deliveryAgent);
			
			//get an expected delivery date from the deliveryAgent and set it on the order object.
			double expectedDeliveryDuration = deliveryAgent.getExpectedDeliveryTime();
			
			order.setDeliveryAgent(deliveryAgent);
			
			order.setExpectedDeliveryDuration(expectedDeliveryDuration);
			
			TrustSetter trustOracle = TrustSetter.getInstance();
			if (trustOracle.getInformationSharingIntegrated()) {	
				// if trusted very good (profile) immediately order the last and the actual orders
				if ((parent.getTrustAgent().getTrustValue(order.getDeliveryAgent())) > borderLargeTrust) {
					deliveryAgent.receiveOrder(order);
					// return because all orders are sent
					return;
				}
			}
		}
		// add the open order
		nextTickOrder.add(order);
	}
	
	// for a second order will be used by the lying agent
	public void secondOrder(TrustAgent trustAgent, Order order) 
	{
		// e.g. select Retailer. with customer.procurementAgent
		if (order != null) {
			DeliveryAgent deliveryAgent = procurementAgent.chooseSecondSupplier(numberOfOpenOrders);
			// the delivery Agent is put into a list where are all at which I want to order
			
			//add a new order to the global number of open orders mapped to the suppliers
			int _no_open_orders = 0;
			if(numberOfOpenOrders.containsKey(deliveryAgent)){
				_no_open_orders = (int)numberOfOpenOrders.get(deliveryAgent);
			}
			
			numberOfOpenOrders.put(deliveryAgent, _no_open_orders + 1);
			
			willOrder.add(deliveryAgent);
			double expectedDeliveryDuration = deliveryAgent.getExpectedDeliveryTime();
			order.setDeliveryAgent(deliveryAgent);
			order.setExpectedDeliveryDuration(expectedDeliveryDuration);
			TrustSetter trustOracle = TrustSetter.getInstance();
			if (trustOracle.getInformationSharingIntegrated()) {
				// if trusted very good (profile)immediately order the last and the actual order
				if ((parent.getTrustAgent().getTrustValue(order.getDeliveryAgent())) > borderLargeTrust) {
					deliveryAgent.receiveOrder(order);
					// return because all orders are sent
					return;
				}
			}
		}
		// add the open order
		nextTickOrder.add(order);
	}
	
	public void deliverRawMaterialA(int neededAmount)
	{
		
	}
	
	/*
	 * from the super class like customer
	 */
	public void receiveShipments(InventoryAgent inventoryAgent) 
	{	
		thisTickReceived = 0;
		if (!receivedShipments.isEmpty()) {
			for (Order shipment : receivedShipments) 
			{
				thisTickReceived += shipment.getQuantity();
				inventoryAgent.store(shipment);
				// if the shipment was not cancelled and if it is finally completely fulfilled it will be inspected
				if (!shipment.getCancelled() && shipment.getProcessed()) {
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
		// set time of the first received
		// information for the procurement agent 
		
		receivedShipments.add(shipment);
		
		//remove a shipment from the global mapping to the suppliers
		if (shipment.getProcessed()) {

			numberOfOpenOrders.put(deliverer, numberOfOpenOrders.get(deliverer) -1);

		}
	}
	
	public void clearReceivedShipments() 
	{
		receivedShipments.clear();
	}
	
	/*
	 * GETTERS
	 */
	public SupplyChainMember getOrderer() 
	{	
		return parent;
	}
	
	public SupplyChainMember getParent() 
	{
		return parent;
	}
	
	public ArrayList<Order> getReceivedShipments() 
	{
		return this.receivedShipments;
	}
	
	public int getThisTickReceived()
	{
		return thisTickReceived;
	}
}