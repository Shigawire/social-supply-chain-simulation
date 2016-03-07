package social_simulation_project;

//import java.util.ArrayList;

//import artefacts.Order;

// class that observes all open orders
public class OrderObserver 
{
	private static OrderObserver observer = new OrderObserver();
	private int amount = 0;
//	private ArrayList<Integer> orderHistory= new ArrayList<Integer>();	

	private OrderObserver()
	{
//		observer.amount=0;
	}
	
	// add amount of an open order
	public void addAmount(int adding)
	{
		observer.amount += adding;
	}
	
	// sub amount of a part delivery
	public void subAmount(int sub)
	{
		observer.amount -= sub;
	}
	
	public static OrderObserver giveObserver()
	{	
		return observer;
	}
	
	/*
	 * GETTERS
	 */
	public int getAmount()
	{
		return observer.amount;
	}
	
	/*
	 * SETTERS
	 */
	public void setAmount(int i) 
	{
		observer.amount = i;
	}
}