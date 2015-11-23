package customer_Retailer_Relationship;

import repast.simphony.engine.environment.RunEnvironment;

public class Order {
	private int quantity;
	private int ordered_at;
	private int backlog;
	private String id;
	private Echelon orderer;
	
	public Order(int quantity, Echelon orderer) {
		this.quantity = quantity;
		//generate an ID, so it is easier to track the Order through the system :)
		this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
		this.ordered_at = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		this.orderer = orderer;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public boolean finished() {
		return (this.backlog > 0);
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getOrderDate() {
		return this.ordered_at;
	}
	
	public Echelon getOrderer() {
		return this.orderer;
	}
}