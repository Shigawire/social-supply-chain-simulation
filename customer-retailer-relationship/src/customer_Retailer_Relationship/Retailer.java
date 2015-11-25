package customer_Retailer_Relationship;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Retailer extends Echelon {
	private int stock;
	//private Order[] orderQueue;
	private int current_demand;
	
	private List<Order> incomingOrderQueue = new ArrayList<Order>();
	
	private List<Order> historicalOrders = new ArrayList<Order>();
	
	@ScheduledMethod(start=1,interval=1)
	public void run() {
		//check if we need to restock.
		if (restockRequired()) restock();
		processOrders();
	}
	
	public void receiveOrder(Order order) {
		int quantity = order.getQuantity();
		//receive an order
		System.out.println("[Retailer] - Received order with qty. " + quantity);
		incomingOrderQueue.add(order);
	}
	
	private void processOrders() {
		//either process all orders
		boolean ret = false;
		while (!incomingOrderQueue.isEmpty()) {
			//or break if current stock is not sufficient
			Order current_order = incomingOrderQueue.get(0);
			if (current_order.getQuantity() > stock) {
				ret = true;
			}
			
			if (ret) return;
			
			stock -= current_order.getQuantity();
			current_order.getOrderer().beSupplied(current_order);
			
			incomingOrderQueue.remove(0);
			
			historicalOrders.add(current_order);
		}
	}
	
	private int[] replenishmentStrategy() {
		//return: if lower than 10, restock with qty. 30, resp. the replenishment Strategy
		
		double accumulated = 0;
		
		for (Order order : historicalOrders) {
			accumulated += order.getQuantity();
		}
		
		double simple_forecast = 30;
		if (historicalOrders.size() > 0) {
			simple_forecast = (accumulated/historicalOrders.size()) * incomingOrderQueue.size();
		}
				
		int[] thresholds = {10, (int)simple_forecast};
		return thresholds;
	}
	
	private boolean restockRequired() {
		
		//if stock is below threshold or lower than latest order, reorder!
		return ((stock <= replenishmentStrategy()[0]) || ((incomingOrderQueue.size() > 0) && (stock <= incomingOrderQueue.get(0).getQuantity())));
	}
	
	private void restock() {
		int quantity_to_restock = replenishmentStrategy()[1];
		
		System.out.println("[Retailer] - I am going to restock with qty. " + quantity_to_restock);
		sendOrder(quantity_to_restock);
		
		//actually, we would need to send an order. Incoming orders are processed elsewhere;
		stock += quantity_to_restock;
		current_demand = quantity_to_restock;
	}
	
	private void sendOrder(int quantity) {
		return;
	}
	
	public int getStock() {
		return stock;
	}
	
	public int getBacklogSize() {
		return incomingOrderQueue.size();
	}
	
	public int getLatestDemand() {
		return current_demand;
	}
	
}
