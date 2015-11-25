package customer_Retailer_Relationship;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;

public class Customer extends Echelon{
	private Retailer retailer;
	private int current_demand;
	
	@ScheduledMethod(start=1,interval=1)
	public void run() {
		this.sendOrder(newDemand());
	}
	
	private void sendOrder(int quantity) {
		Order order = new Order(quantity, this);
		System.out.println("[Customer] - Going to order some shit, yeah! (Order ID: " + order.getId() + ")");

		retailer.receiveOrder(order);
	}
	
	private int newDemand() {
		current_demand = RandomHelper.nextIntFromTo(1,  30);
		return current_demand;
	}
	
	public void setRetailer(Retailer retailer) {
		this.retailer = retailer;
	}
	
	public void beSupplied(Order order) {
		System.out.println("[Customer] - Yeah, just got delivered with Order.  (Order ID: " + order.getId() + ")");
	}
	
	public int getLatestDemand() {
		return current_demand;
	}

}
