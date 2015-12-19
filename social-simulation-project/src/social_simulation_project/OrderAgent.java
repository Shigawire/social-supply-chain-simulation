package social_simulation_project;

import java.util.ArrayList;

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
	private SupplyChainMember orderer;
	private ArrayList<Order> receivedShipments;

	
	public OrderAgent(SupplyChainMember orderer) 
	{
		this.orderer = orderer;
		this.receivedShipments = new ArrayList<Order>();

	}
	
	//TODO 
	//
	public void order(TrustAgent trustAgent, Order order) 
	{
		// select Retailer. mit customer.trustAgent
		// trustAgent must be implemented
		
		DeliveryAgent deliveryAgent = trustAgent.chooseSupplier();
		//add the open order
		OrderObserver.giveObserver().addAmount(order.getQuantity());
		deliveryAgent.receiveOrder(order);
	}
	
	
	/*
	 * geht vom super agent aus
	 */
	public void receiveShipments(InventoryAgent inventoryAgent) 
	{	
		System.out.println("[Order Agent] receiving shipment list");
		if (!receivedShipments.isEmpty())
		{
			for (Order shipment : receivedShipments) 
			{
				inventoryAgent.store(shipment);
			}
			// This can't go in the for loop
			receivedShipments.clear();
		}	
	}
	
	/*
	 * geht vom delivery agent der nächsten Stufe aus
	 * 
	 */
	public void receiveShipment(Order shipment) 
	{
		System.out.println("[Order Agent] received shipment with qty "+shipment.getQuantity());
		receivedShipments.add(shipment);
//		receivedOrders.add(shipment);
	}
	
	/*
	 * GETTERS
	 */
	public SupplyChainMember getOrderer() 
	{	
		return orderer;
	}
	
	/*
	 * SETTERS
	 */
}