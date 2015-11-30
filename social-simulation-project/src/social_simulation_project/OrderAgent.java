package social_Simulation_Project;

import java.util.ArrayList;

public class OrderAgent {
	private SupplyChainMember orderer;
	private ArrayList<Order> receivedShipments;
	
	public OrderAgent(SupplyChainMember orderer) {
		this.orderer = orderer;
	}
	
	//TODO 
	//
	public void order(TrustAgent trustAgent, Order order) {
		//select Retailer. mit customer.trustAgent
		//
		DeliveryAgent deliveryAgent = trustAgent.returnDeliveryAgent();
		deliveryAgent.receiveOrder(order);
	}
	/*
	public void order(Retailer retailer, Order order) {
		
	}
	
	public void order(Distributor distributor, Order order) {
		
	}

	public void order(Wholesaler wholesaler, Order order) {
	
	}
	*/
	
	public void processShipments(InventoryAgent inventoryAgent) {
		for (Order shipment : receivedShipments) {
			inventoryAgent.setInventoryLevel(inventoryAgent.getInventoryLevel() + shipment.getAmount());
		}
		receivedShipments = null;
	}
	
	public void receiveShipment(Order shipment) {
		receivedShipments.add(shipment);
	}
	
	public SupplyChainMember getOrderer() {
		return orderer;
	}
}
