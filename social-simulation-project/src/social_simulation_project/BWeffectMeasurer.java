package social_simulation_project;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;
import artefacts.Order;
import actors.Customer;
import actors.Distributor;
import actors.Retailer;
import actors.Wholesaler;

public class BWeffectMeasurer 
{
	// Dummys for Comparison of classes only
	Customer customer;
	Retailer retailer;
	Wholesaler wholesaler;
	Distributor distributor;
	
	// Singleton;
	private static BWeffectMeasurer measurer = new BWeffectMeasurer();
	// list in which for every tier the whole value of orders this tier made is saved
	// + one integer to save the sum of one tick
	private ArrayList<Integer> customerList = new ArrayList<Integer>();
	private int tickCustomer = 0;
	private ArrayList<Integer> retailerList = new ArrayList<Integer>();
	private int tickRetailer = 0;
	private ArrayList<Integer> wholesalerList = new ArrayList<Integer>();
	private int tickWholesaler = 0;
	private ArrayList<Integer> distributorList = new ArrayList<Integer>();
	private int tickDistributor = 0;
	private ArrayList<Integer> manufacturerList = new ArrayList<Integer>();
	private int tickManufacturer = 0;
	
	// attributes to save the BWEValues in and put them into an excel-sheet
	private double retailerValue;
	private double wholesalerValue;
	private double distributorValue;
	private double manufacturerValue;
	
	private BWeffectMeasurer()
	{
		
	}
	
	// update the per tick order counter for every tier (will be used by update
	public void updateCustomer(int orderAmount)
	{
		measurer.setTickCustomer(measurer.getTickCustomer() + orderAmount);
	}
	
	public void updateRetailer(int orderAmount)
	{
		measurer.setTickRetailer(measurer.getTickRetailer() + orderAmount);
	}
	
	public void updateWholesaler(int orderAmount)
	{
		measurer.setTickWholesaler(measurer.getTickWholesaler() + orderAmount);
	}
	
	public void updateDistributor(int orderAmount)
	{
		measurer.setTickDistributor(measurer.getTickDistributor() + orderAmount);
	}
	
	// special update for manufacturer as he does not order
	public void updateManufacturer(int produceQuantity) 
	{
		measurer.setTickManufacturer(measurer.getTickManufacturer() + produceQuantity);		
	}	
	
	// enter the orders every whole tier made in every step to the list
	@ScheduledMethod(start = 2, interval = 1, priority = 6)
	public void run()
	{
		// append value of customer for the last tick
		if (measurer.getCustomerList().size() < 52) {
			measurer.getCustomerList().add(measurer.getTickCustomer());
		} else {
			measurer.getCustomerList().remove(0);
			measurer.getCustomerList().add(measurer.getTickCustomer());
		}
		
		// append value of Retailer for the last tick
		if (measurer.getRetailerList().size() < 52) {
			measurer.getRetailerList().add(measurer.getTickRetailer());
		} else {
			measurer.getRetailerList().remove(0);
			measurer.getRetailerList().add(measurer.getTickRetailer());
		}
		
		// append value of Wholesaler for the last tick
		if (measurer.getWholesalerList().size() < 52) {
			measurer.getWholesalerList().add(measurer.getTickWholesaler());
		} else {
			measurer.getWholesalerList().remove(0);
			measurer.getWholesalerList().add(measurer.getTickWholesaler());
		}
		
		// append value of Distributor for the last tick
		if (measurer.getDistributorList().size() < 52) {
			measurer.getDistributorList().add(measurer.getTickDistributor());
		} else {
			measurer.getDistributorList().remove(0);
			measurer.getDistributorList().add(measurer.getTickDistributor());
		}
		
		if (measurer.getManufacturerList().size() < 52) {
			measurer.getManufacturerList().add(measurer.getTickManufacturer());
		} else {
			measurer.getManufacturerList().remove(0);
			measurer.getManufacturerList().add(measurer.getTickManufacturer());
		}
		
		// set the counters for the next tick = 0
		measurer.setTickCustomer(0);
		measurer.setTickRetailer(0);
		measurer.setTickWholesaler(0);
		measurer.setTickDistributor(0);
		measurer.setTickManufacturer(0);
		
		// compute the variances
		 double customerVar = variance(measurer.getCustomerList());
		 double retailerVar = variance(measurer.getRetailerList());
		 double wholesalerVar = variance(measurer.getWholesalerList());
		 double distributorVar = variance(measurer.getDistributorList());
		 double manufacturerVar = variance(measurer.getManufacturerList());
		 // bullwhip meaurements:
		 retailerValue = retailerVar / customerVar;
		 //System.out.println(retailerVar/customerVar);
		 wholesalerValue = wholesalerVar / customerVar;
		 //System.out.println(wholesalerVar/customerVar);
		 distributorValue = distributorVar / customerVar;
		 //System.out.println(distributorVar/customerVar);
		 manufacturerValue = manufacturerVar / customerVar;
		 //System.out.println(manufacturerVar/customerVar);
	}
	
	// computes the variance for list of integers
	public double variance(ArrayList<Integer> toCompute)
	{
		double mid = 0;
		double variance = 0;
		double partComputation = 0;
		for (Integer part : toCompute)
		{
			mid += part;
		}

		// System.out.println("mid"+mid);
		mid = mid / toCompute.size();
		for (Integer part : toCompute)
		{
			partComputation = part - mid;
			variance += partComputation * partComputation;
		}
		// System.out.println("variance" + variance);
		return variance;
	}
	
	// update the with new order
	public void update(Order order) 
	{
		System.out.println(customer.getClass());
		System.out.println(order.getOrderAgent().getParent().getClass().equals("a"));
		
		if (order.getOrderAgent().getParent().getClass().equals(customer.getClass())) {
			measurer.updateCustomer(order.getQuantity());
		}
		
		else if (order.getOrderAgent().getParent().getClass().equals(retailer.getClass())) {
			measurer.updateRetailer(order.getQuantity());			
		}
		
		else if (order.getOrderAgent().getParent().getClass().equals(wholesaler.getClass())) {
			measurer.updateWholesaler(order.getQuantity());			
		}
		
		else if (order.getOrderAgent().getParent().getClass().equals(distributor.getClass())) {
			measurer.updateDistributor(order.getQuantity());			
		}
	}
	
	/*
	 * GETTERS
	 */
	public int getTickCustomer() 
	{
		return tickCustomer;
	}
	
	public int getTickRetailer() 
	{
		return tickRetailer;
	}
	
	public int getTickWholesaler() 
	{
		return tickWholesaler;
	}
	
	public int getTickDistributor() 
	{
		return tickDistributor;
	}

	private int getTickManufacturer() 
	{
		return tickManufacturer;
	}
	
	public static BWeffectMeasurer getMeasurer()
	{
		return measurer;
	}
	
	public ArrayList<Integer> getCustomerList() 
	{
		return customerList;
	}
	
	public ArrayList<Integer> getRetailerList() 
	{
		return retailerList;
	}

	public ArrayList<Integer> getWholesalerList() 
	{
		return wholesalerList;
	}
	
	public ArrayList<Integer> getDistributorList() 
	{
		return distributorList;
	}
	
	private ArrayList<Integer> getManufacturerList() 
	{
		return manufacturerList;
	}
	
	public double getRetailerBWEValue()
	{
		return retailerValue;
	}
	
	public double getWholesalerBWEValue()
	{
		return wholesalerValue;
	}
	
	public double getDistributorBWEValue()
	{
		return distributorValue;
	}
	
	public double getManufacturerBWEValue()
	{
		return manufacturerValue;
	}
	
	/*
	 * SETTERS
	 */
	public void setTickCustomer(int tickCustomer) 
	{
		this.tickCustomer = tickCustomer;
	}
	
	public void setTickRetailer(int tickRetailer) 
	{
		this.tickRetailer = tickRetailer;
	}
	
	public void setTickWholesaler(int tickWholesaler) 
	{
		this.tickWholesaler = tickWholesaler;
	}
	
	public void setTickDistributor(int tickDistributor) 
	{
		this.tickDistributor = tickDistributor;
	}
	
	private void setTickManufacturer(int tickManufacturer) 
	{
		this.tickManufacturer=tickManufacturer;
	}
	
	public void setTheDummys(Customer customer, Retailer retailer, Wholesaler wholesaler, Distributor distributor)
	{
		this.customer = customer;
		this.retailer = retailer;
		this.wholesaler = wholesaler;
		this.distributor = distributor;
	}
}