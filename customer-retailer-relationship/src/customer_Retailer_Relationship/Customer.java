package customer_Retailer_Relationship;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;

public class Customer extends Echelon{
	private Retailer retailer;
	
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
		return RandomHelper.nextIntFromTo(1,  30);
	}
	
	public void setRetailer(Retailer retailer) {
		this.retailer = retailer;
	}
	
	public void beSupplied(Order order) {
		System.out.println("[Customer] - Yeah, just got delivered with Order.  (Order ID: " + order.getId() + ")");
	}

}
