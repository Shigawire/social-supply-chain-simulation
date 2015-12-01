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
	}
	
	//TODO 
	//
	public void order(TrustAgent trustAgent, Order order) 
	{
		//select Retailer. mit customer.trustAgent
		//
		DeliveryAgent deliveryAgent = trustAgent.returnDeliveryAgent();
		deliveryAgent.receiveOrder(order);
	}
	/*
	public void order(Retailer retailer, Order order) 
	{
		
	}
	
	public void order(Distributor distributor, Order order) 
	{
		
	}

	public void order(Wholesaler wholesaler, Order order) 
	{
	
	}
	*/
	
	public void processShipments(InventoryAgent inventoryAgent) 
	{
		for (Order shipment : receivedShipments) 
		{
			inventoryAgent.setInventoryLevel(inventoryAgent.getInventoryLevel() + shipment.getAmount());
		}
		receivedShipments = null;
	}
	
	public void receiveShipment(Order shipment) 
	{
		receivedShipments.add(shipment);
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