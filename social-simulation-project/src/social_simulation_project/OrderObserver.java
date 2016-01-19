package social_simulation_project;

import java.util.ArrayList;

import artefacts.Order;

public class OrderObserver 
{
	private static OrderObserver observer = new OrderObserver();
	private int amount = 0;
	private ArrayList<Integer> orderHistory= new ArrayList<Integer>();	

	private OrderObserver()
	{
		amount=0;
	}
	
	public void addAmount(int adding)
	{
		orderHistory.add(adding);
		amount+= adding;
		
	}
	
	public void subAmount(int sub)
	{
		Integer weg=sub;
		if(orderHistory.contains(weg)){
			orderHistory.remove(weg);
		}
		else{
			System.out.println("Fick Dich");
		}

		amount-= sub;
	}
	
	public int getAmount()
	{
		return amount;
	}
	
	public static OrderObserver giveObserver()
	{	
		return observer;
	}

	public void setAmount(int i) {
		amount=0;
		// TODO Auto-generated method stub
		
	}
}