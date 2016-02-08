package social_simulation_project;

import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;
import agents.DeliveryAgent;
import artefacts.Order;
import actors.Customer;
import actors.Distributor;
import actors.Retailer;
import actors.Wholesaler;

public class BWeffectMeasurer {
	
	//Dummys for Comparison only
	Customer c;
	Retailer r;
	Wholesaler w;
	Distributor d;
	//Singleton;
	private static BWeffectMeasurer measurer=new BWeffectMeasurer();
	//list in which for every for every tier the whole value of orders this tier made is saved
	//+ one integer to save the sum of one tick
	private ArrayList<Integer> customer= new ArrayList<Integer>();
	private int tickCustomer=0;
	private ArrayList<Integer> retailer= new ArrayList<Integer>();
	private int tickRetailer=0;
	private ArrayList<Integer> wholesaler= new ArrayList<Integer>();
	private int tickWholesaler=0;
	private ArrayList<Integer> distributor= new ArrayList<Integer>();
	private int tickDistributor=0;
	
	
	private BWeffectMeasurer(){
	}
	//update the per tick order counter for every tier (will be used by update
	public void updateCustomer(int order_amount){
		measurer.setTickCustomer(measurer.getTickCustomer()  + order_amount);
	}
	public void updateRetailer(int order_amount){
		measurer.setTickRetailer(measurer.getTickRetailer()  + order_amount);
	}
	public void updateWholesaler(int order_amount){
		measurer.setTickWholesaler(measurer.getTickWholesaler()  + order_amount);
	}
	public void updateDistributor(int order_amount){
		measurer.setTickDistributor(measurer.getTickDistributor()  + order_amount);
	}
	//enter the orders every tier made in every step to the list
	@ScheduledMethod(start = 2, interval = 1, priority = 6)
	public void run(){
		//append value of customer for the last tick
		if(measurer.getCustomer().size()<30){
			measurer.getCustomer().add(measurer.getTickCustomer());
		}
		else{
			measurer.getCustomer().remove(0);
			measurer.getCustomer().add(measurer.getTickCustomer());
		}
		//append value of Retailer for the last tick
		if(measurer.getRetailer().size()<30){
			measurer.getRetailer().add(measurer.getTickRetailer());
		}
		else{
			measurer.getRetailer().remove(0);
			measurer.getRetailer().add(measurer.getTickRetailer());
		}
		//append value of Wholesaler for the last tick
		if(measurer.getWholesaler().size()<30){
			measurer.getWholesaler().add(measurer.getTickWholesaler());
		}
		else{
			measurer.getWholesaler().remove(0);
			measurer.getWholesaler().add(measurer.getTickWholesaler());
		}
		//append value of Distributor for the last tick
		if(measurer.getDistributor().size()<30){
			measurer.getDistributor().add(measurer.getTickDistributor());
		}
		else{
			measurer.getDistributor().remove(0);
			measurer.getDistributor().add(measurer.getTickDistributor());
		}
		//set the counters for the next tick =0
		measurer.setTickCustomer(0);
		measurer.setTickRetailer(0);
		measurer.setTickWholesaler(0);
		measurer.setTickDistributor(0);
		
		System.out.println();
		//compute the variances
		 double customerVar = variance(measurer.getCustomer());
		 double retailerVar = variance(measurer.getRetailer());
		 double wholesalerVar = variance(measurer.getWholesaler());
		 double distributorVar = variance(measurer.getDistributor());
		 //bullwhip meaurements:
		 System.out.println(retailerVar/customerVar);
		 System.out.println(wholesalerVar/retailerVar);
		 System.out.println(distributorVar/wholesalerVar);
	}
	
	
	
	
	public double variance(ArrayList<Integer> toCompute){
		double mid=0;
		double variance=0;
		double partcomputation=0;
		for(Integer part:toCompute){
			mid+=part;
			
		}

		//System.out.println("mid"+mid);
		mid=mid/toCompute.size();
		for(Integer part:toCompute){
			partcomputation=part-mid;
			variance+=partcomputation*partcomputation;
		}
		//System.out.println("variance"+variance);
		return variance;
		
	}
	//Getters and setters
	public int getTickCustomer() {
		return tickCustomer;
	}

	public void setTickCustomer(int tickCustomer) {
		this.tickCustomer = tickCustomer;
	}

	public int getTickRetailer() {
		return tickRetailer;
	}

	public void setTickRetailer(int tickRetailer) {
		this.tickRetailer = tickRetailer;
	}

	public int getTickWholesaler() {
		return tickWholesaler;
	}

	public void setTickWholesaler(int tickWholesaler) {
		this.tickWholesaler = tickWholesaler;
	}

	public int getTickDistributor() {
		return tickDistributor;
	}

	public void setTickDistributor(int tickDistributor) {
		this.tickDistributor = tickDistributor;
	}


	public ArrayList<Integer> getCustomer() {
		return customer;
	}

	public void setCustomer(ArrayList<Integer> customer) {
		this.customer = customer;
	}

	public ArrayList<Integer> getRetailer() {
		return retailer;
	}

	public void setRetailer(ArrayList<Integer> retailer) {
		this.retailer = retailer;
	}

	public ArrayList<Integer> getWholesaler() {
		return wholesaler;
	}

	public void setWholesaler(ArrayList<Integer> wholesaler) {
		this.wholesaler = wholesaler;
	}

	public ArrayList<Integer> getDistributor() {
		return distributor;
	}

	public void setDistributor(ArrayList<Integer> distributor) {
		this.distributor = distributor;
	}

	
	public static BWeffectMeasurer getMeasurer(){
		return measurer;
	}
	public void setTheDummys(Customer c,Retailer r,Wholesaler w,Distributor d){
		this.c=c;
		this.r=r;
		this.w=w;
		this.d=d;
	}
	//update the with new order
	public void update(Order order) {
		
		if(order.getOrderAgent().getParent().getClass().equals(c.getClass())){
			measurer.updateCustomer(order.getQuantity());
		}
		else if(order.getOrderAgent().getParent().getClass().equals(r.getClass())){
			measurer.updateRetailer(order.getQuantity());			
		}
		else if(order.getOrderAgent().getParent().getClass().equals(w.getClass())){
			measurer.updateWholesaler(order.getQuantity());			
		}
		else if(order.getOrderAgent().getParent().getClass().equals(d.getClass())){
			measurer.updateDistributor(order.getQuantity());			
		}
		
	}
	
}