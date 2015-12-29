package social_simulation_project;

public class OrderObserver {
	private static OrderObserver observer= new OrderObserver();
	private int amount=0;
	private OrderObserver(){
		
	}
	public void addAmount(int adding){
		amount+= adding;
	}
	public void subAmount(int sub){
		amount-= sub;
	}
	public int getAmount(){
		return amount;
	}
	public static OrderObserver giveObserver(){
		return observer;
	}
}
