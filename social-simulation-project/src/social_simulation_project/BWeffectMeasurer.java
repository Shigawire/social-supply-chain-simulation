package social_simulation_project;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;
import agents.DeliveryAgent;
import artefacts.Order;
import actors.Customer;
import actors.Distributor;
import actors.Retailer;
import actors.Wholesaler;

public class BWeffectMeasurer 
{
	// Dummys for Comparison of classes only
	Customer c;
	Retailer r;
	Wholesaler w;
	Distributor d;
	// Singleton;
	private static BWeffectMeasurer measurer = new BWeffectMeasurer();
	// list in which for every for every tier the whole value of orders this tier made is saved
	// + one integer to save the sum of one tick
	private ArrayList<Integer> customer = new ArrayList<Integer>();
	private int tickCustomer = 0;
	private ArrayList<Integer> retailer = new ArrayList<Integer>();
	private int tickRetailer = 0;
	private ArrayList<Integer> wholesaler = new ArrayList<Integer>();
	private int tickWholesaler = 0;
	private ArrayList<Integer> distributor = new ArrayList<Integer>();
	private int tickDistributor = 0;
	private ArrayList<Integer> manufacturer = new ArrayList<Integer>();
	private int tickManufacturer = 0;
	//attributes to save the BWEValues in and put them into an excel-sheet
	private double customerValue;
	private double retailerValue;
	private double wholesalerValue;
	private double distributorValue;
	private double manufacturerValue;
	
	
	private BWeffectMeasurer()
	{
	}
	
	//update the per tick order counter for every tier (will be used by update
	public void updateCustomer(int order_amount)
	{
		measurer.setTickCustomer(measurer.getTickCustomer() + order_amount);
	}
	
	public void updateRetailer(int order_amount)
	{
		measurer.setTickRetailer(measurer.getTickRetailer() + order_amount);
	}
	
	public void updateWholesaler(int order_amount)
	{
		measurer.setTickWholesaler(measurer.getTickWholesaler() + order_amount);
	}
	
	public void updateDistributor(int order_amount)
	{
		measurer.setTickDistributor(measurer.getTickDistributor() + order_amount);
	}
	//special update for manufacturer as he does not order
	public void updateManufacturer(int produceQuantity) {
		measurer.setTickManufacturer(measurer.getTickManufacturer()+ produceQuantity);
			
	}	
	
	

	// enter the orders every whole tier made in every step to the list
	@ScheduledMethod(start = 2, interval = 1, priority = 6)
	public void run()
	{
		// append value of customer for the last tick
		if (measurer.getCustomer().size() < 104) {
			measurer.getCustomer().add(measurer.getTickCustomer());
		} else {
			measurer.getCustomer().remove(0);
			measurer.getCustomer().add(measurer.getTickCustomer());
		}
		
		// append value of Retailer for the last tick
		if (measurer.getRetailer().size() < 104) {
			measurer.getRetailer().add(measurer.getTickRetailer());
		} else {
			measurer.getRetailer().remove(0);
			measurer.getRetailer().add(measurer.getTickRetailer());
		}
		
		// append value of Wholesaler for the last tick
		if (measurer.getWholesaler().size() < 104) {
			measurer.getWholesaler().add(measurer.getTickWholesaler());
		} else {
			measurer.getWholesaler().remove(0);
			measurer.getWholesaler().add(measurer.getTickWholesaler());
		}
		
		// append value of Distributor for the last tick
		if (measurer.getDistributor().size() < 104) {
			measurer.getDistributor().add(measurer.getTickDistributor());
		} else {
			measurer.getDistributor().remove(0);
			measurer.getDistributor().add(measurer.getTickDistributor());
		}
		if (measurer.getManufacturer().size() < 104) {
			measurer.getManufacturer().add(measurer.getTickManufacturer());
		} else {
			measurer.getManufacturer().remove(0);
			measurer.getManufacturer().add(measurer.getTickManufacturer());
		}
		
		// set the counters for the next tick =0
		measurer.setTickCustomer(0);
		measurer.setTickRetailer(0);
		measurer.setTickWholesaler(0);
		measurer.setTickDistributor(0);
		measurer.setTickManufacturer(0);
		
		// System.out.println();
		// compute the variances
		 double customerVar = variance(measurer.getCustomer());
		 double retailerVar = variance(measurer.getRetailer());
		 double wholesalerVar = variance(measurer.getWholesaler());
		 double distributorVar = variance(measurer.getDistributor());
		 double manufacturerVar = variance(measurer.getManufacturer());
		 //bullwhip meaurements:
		 retailerValue = retailerVar/customerVar;
		 //System.out.println(retailerVar/customerVar);
		 wholesalerValue = wholesalerVar/customerVar;
		 //System.out.println(wholesalerVar/customerVar);
		 distributorValue = distributorVar/customerVar;
		 //System.out.println(distributorVar/customerVar);
		 manufacturerValue = manufacturerVar/customerVar;
		 //System.out.println(manufacturerVar/customerVar);
	}
	
	private ArrayList<Integer> getManufacturer() {
		// TODO Auto-generated method stub
		return manufacturer;
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
		// System.out.println("variance"+variance);
		return variance;
	}
	
	// update the with new order
	public void update(Order order) 
	{
		if (order.getOrderAgent().getParent().getClass().equals(c.getClass())) {
			measurer.updateCustomer(order.getQuantity());
		}
		
		else if (order.getOrderAgent().getParent().getClass().equals(r.getClass())) {
			measurer.updateRetailer(order.getQuantity());			
		}
		
		else if (order.getOrderAgent().getParent().getClass().equals(w.getClass())) {
			measurer.updateWholesaler(order.getQuantity());			
		}
		
		else if (order.getOrderAgent().getParent().getClass().equals(d.getClass())) {
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

	private int getTickManufacturer() {
		// TODO Auto-generated method stub
		return tickManufacturer;
	}
	public int getTickWholesaler() 
	{
		return tickWholesaler;
	}

	public int getTickRetailer() 
	{
		return tickRetailer;
	}
	
	public static BWeffectMeasurer getMeasurer()
	{
		return measurer;
	}
	
	public int getTickDistributor() 
	{
		return tickDistributor;
	}
	
	public ArrayList<Integer> getCustomer() 
	{
		return customer;
	}
	
	public ArrayList<Integer> getRetailer() 
	{
		return retailer;
	}

	public ArrayList<Integer> getWholesaler() 
	{
		return wholesaler;
	}
	
	public ArrayList<Integer> getDistributor() 
	{
		return distributor;
	}
	
	public double getRetailerBWEValue(){
		return retailerValue;
	}
	public double getWholesalerBWEValue(){
		return wholesalerValue;
	}
	public double getDistributorBWEValue(){
		return distributorValue;
	}
	public double getManufacturerBWEValue(){
		return manufacturerValue;
	}
	
	
	/*
	 * SETTERS
	 */
	public void setTickRetailer(int tickRetailer) 
	{
		this.tickRetailer = tickRetailer;
	}
	private void setTickManufacturer(int tickManufacturer) {
		this.tickManufacturer=tickManufacturer;
		
	}
	public void setTickCustomer(int tickCustomer) 
	{
		this.tickCustomer = tickCustomer;
	}

	public void setTickWholesaler(int tickWholesaler) 
	{
		this.tickWholesaler = tickWholesaler;
	}

	public void setTickDistributor(int tickDistributor) 
	{
		this.tickDistributor = tickDistributor;
	}

	public void setCustomer(ArrayList<Integer> customer) 
	{
		this.customer = customer;
	}

	public void setRetailer(ArrayList<Integer> retailer) 
	{
		this.retailer = retailer;
	}

	public void setWholesaler(ArrayList<Integer> wholesaler) 
	{
		this.wholesaler = wholesaler;
	}

	public void setDistributor(ArrayList<Integer> distributor) 
	{
		this.distributor = distributor;
	}
	
	public void setTheDummys(Customer c, Retailer r, Wholesaler w, Distributor d)
	{
		this.c = c;
		this.r = r;
		this.w = w;
		this.d = d;
	}

	
}