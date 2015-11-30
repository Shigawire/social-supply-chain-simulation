package customer_Retailer_Relationship;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Retailer extends SupplyChainMember {
	private int demand_amount;
	private int next_demand;
	private int current_inventory_level;
	private int order_quantity;
	private DeliveryAgent deliveryAgent;
	
	private OrderAgent orderAgent;
	
	public Retailer() {
		orderAgent = new OrderAgent(this);
		
	}
	
	public void run() {
		//TODO
		//1. processShipments()
		this.processShipments();
		//2. updateTrust()
		
		//3. deliver()
		this.deliver();
		//4. calculateDemand()
		next_demand = this.forecastAgent.calculateDemand();
		//5. order()
		this.order();
	}
	
	private void processShipments() {
		this.orderAgent.processShipments(this.inventoryAgent);
	}
	
	public void deliver() {
		this.deliveryAgent.deliver(this.inventoryAgent);
	}
}
