package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.DefaultScheduleRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.Schedule;
import social_simulation_project.BWeffectMeasurer;
import social_simulation_project.OrderObserver;
import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.SellingActor;
import actors.Wholesaler;
import artefacts.Order;
import artefacts.Profile;

/*
 * http://repast.sourceforge.net/docs/RepastModelTesting.pdf documentation
 * This Test will check if the order structures are correct
 */

public class OrderTest 
{

	private Context<Object> context;
	private Schedule schedule;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
	}

	@Before
    public void setUp() throws Exception 
	{
		this.schedule = new Schedule();
		RunEnvironment.init(this.schedule, new DefaultScheduleRunner(), null, false);
		this.context = new DefaultContext<Object>();
		RunState.init().setMasterContext(this.context);
    }

	@After
	public void tearDown() throws Exception 
	{
	}
	
	@Test
	public void test() 
	{
		//This is a new simulation setup, similar to the Simulation Builder
		Profile profile = new Profile(0.44, 0.415, 0.145, 0.22, 0.31, 0.31, 0.16, 0.67, 0.53, 0.35);
		
		this.context.add(OrderObserver.giveObserver());
		OrderObserver.giveObserver().setAmount(0);
		
		Manufacturer manufacturer1 = new Manufacturer(10, 0, 100, profile);
		ArrayList<SellingActor> manufacturerList = new ArrayList<SellingActor>();
		manufacturerList.add(manufacturer1);
		
		Distributor distributor1 = new Distributor(manufacturerList, 15, 11, 50, profile);
		
		ArrayList<SellingActor> distributorList = new ArrayList<SellingActor>();
		distributorList.add(distributor1);

		Wholesaler wholesaler1 = new Wholesaler(distributorList, 12, 11, 40, profile);

		ArrayList<SellingActor> wholesalerList = new ArrayList<SellingActor>();
		wholesalerList.add(wholesaler1);	
		
		//Give the retailer an initial inventory of 200 - this ensures there is enough stock to fulfill any upcoming demand instantly.
		Retailer retailer1 = new Retailer(wholesalerList, 200, 10, 10,  profile);
		
		ArrayList<SellingActor> retailerList = new ArrayList<SellingActor>();
		retailerList.add(retailer1);
		
		Customer customer1 = new Customer(retailerList, 10, 15, profile);
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer1);
		
		
		//initialize the Bullwhip Effect Measurer. Not used during the tests - but required for the simulation to advance.
		BWeffectMeasurer.getMeasurer().setTheDummys(customer1,
				retailer1, 
				wholesaler1, 
				distributor1);
		
		this.context.add(manufacturer1);
		this.context.add(distributor1);
		this.context.add(wholesaler1);
		this.context.add(retailer1);
		this.context.add(customer1);
		
		// simulation is not initialized yet
		assertEquals(-1, this.schedule.getTickCount(), 0);
		
		//------- TICK 1 -------------
				
		for (int i = 1; i <= 1; i++) 
		{			
			this.schedule.schedule(customer1);
			this.schedule.schedule(retailer1);
			this.schedule.schedule(wholesaler1);
			this.schedule.schedule(distributor1);
			this.schedule.schedule(manufacturer1);
			
			//advance the simulation by one tick
	    	this.schedule.execute();
		}
    	
		// simulation clock has advanced
    	assertTrue("Simulation clock has not advanced", this.schedule.getTickCount() > -1);
    			

    	//Testing if the customer demand is between 8 and 12 (according to the forecastAgent's getCustomerDemand Method it's probably not out of these bounds)
    	assertTrue("Demand for next tick is out of range (9..11): " + customer1.getNextDemand(), (8 <= customer1.getNextDemand()) && (customer1.getNextDemand() <= 12));
    	
    	// the next order quantity is larger than 0 and smaller or equal to the customer demand
    	assertTrue("Next order quantity is out of range", customer1.getNextOrderQuantity() >= 0 && customer1.getNextOrderQuantity() <= customer1.getNextDemand());
  
    	// Let's do a custom order with qty 1. This order must be received in tick 3.
    	Order order = new Order(1, customer1.getOrderAgent());
    	customer1.getOrderAgent().order(customer1.getTrustAgent(), order);
    	
    	// somewhere in the next Ticks I want my order from to be arrived.

    	while(true) 
    	{			
			this.schedule.schedule(customer1);
			this.schedule.schedule(retailer1);
			this.schedule.schedule(wholesaler1);
			this.schedule.schedule(distributor1);
			this.schedule.schedule(manufacturer1);
			
	    	this.schedule.execute();
	    	
	    	if (customer1.getInventoryAgent().getEverReceivedShipments().get(order.toString()) != null) {
	    		assertEquals("The small order of qty. 1 arrives in tick 3", 3, schedule.getTickCount(), 0);
	    		break;
	    	}
		}
	}
}