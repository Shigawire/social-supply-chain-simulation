package customer_Retailer_Relationship;

public class Customer extends SupplyChainMember {
	private int demand_amount;
	private int next_demand;
	private int current_inventory_level;
	private int order_quantity;
	
	public OrderAgent orderAgent;
	
	public Customer() {
		inventoryAgent = new InventoryAgent();
		orderAgent = new OrderAgent(this);
	}
	
	public void run() {
		//TODO
		//1. processShipments()
		this.processShipments();
		//2. updateTrust()
		//3. consume()
		this.consume();
		//4. calculateDemand()
		next_demand = this.forecastAgent.calculateDemand();
		//5. order()
		this.order();
	}
	
	public void consume() {
		//TODO temporär, muss noch implementiert werden
		demand_amount = 10;//forecastAgent.getNextDemand();
		current_inventory_level = inventoryAgent.getInventoryLevel();
		if (demand_amount > current_inventory_level) {
			//TODO strafkosten/reaktion
			//Inventory ist geringer als Nachfrage
			inventoryAgent.setInventoryLevel(0);
		} else {
			inventoryAgent.setInventoryLevel(current_inventory_level - demand_amount);
		}
	}
	
	
	public void order() {
		order_quantity = next_demand - inventoryAgent.getInventoryLevel();//
		//TODO replenishment policy
		if (order_quantity <= 0) return;
		
		Order order = new Order(order_quantity, this.orderAgent);
		//Choose retailer
		orderAgent.order(this.trustAgent, order);
	}
	
	private void processShipments() {
		this.orderAgent.processShipments(this.inventoryAgent);
	}

}