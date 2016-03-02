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
import social_simulation_project.OrderObserver;
import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.Sale;
import actors.Wholesaler;
import artefacts.Order;
import artefacts.Profile;

/*
 * http://repast.sourceforge.net/docs/RepastModelTesting.pdf documentation
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

	/*
	public void setUp() throws Exception 
	{
		Schedule schedule = new Schedule();
		RunEnvironment.init(schedule, null, null, true);
		
		Context<Object> context = new DefaultContext<Object>();	
	}
	*/
	@Before
    public void setUp() throws Exception 
	{
		this.schedule = new Schedule();
		RunEnvironment.init(this.schedule, new DefaultScheduleRunner(), null, false);
		this.context = new DefaultContext<Object>();
		//SimulationBuilder builder = new SimulationBuilder();
		//this.context = builder.build (context);
		RunState.init().setMasterContext(this.context);
    }

	@After
	public void tearDown() throws Exception 
	{
	}
	
	@Test
	public void test() 
	{
		Profile a=new Profile(0.44,0.415,0.145,0.22,0.31,0.31,0.16,0.67,0.53,0.35);
		this.context.add(OrderObserver.giveObserver());
		OrderObserver.giveObserver().setAmount(0);
		
		Manufacturer manufacturer1 = new Manufacturer(10, 0, 100,a);
		ArrayList<Sale> manufacturerList = new ArrayList<Sale>();
		manufacturerList.add(manufacturer1);
		
		Distributor distributor1 = new Distributor(manufacturerList, 15, 11, 50,a);
		
		ArrayList<Sale> distributorList = new ArrayList<Sale>();
		distributorList.add(distributor1);

		Wholesaler wholesaler1 = new Wholesaler(distributorList, 12, 11, 40,a);

		ArrayList<Sale> wholesalerList = new ArrayList<Sale>();
		wholesalerList.add(wholesaler1);	
		
		Retailer retailer1 = new Retailer(wholesalerList, 10, 10, 10,a);
		
		ArrayList<Sale> retailerList = new ArrayList<Sale>();
		retailerList.add(retailer1);
		
		Customer customer1 = new Customer(retailerList, 10, 15,a);
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer1);

		this.context.add(manufacturer1);
		this.context.add(distributor1);
		this.context.add(wholesaler1);
		this.context.add(retailer1);
		this.context.add(customer1);
		
		// simulation is not initialized yet
		assertEquals(-1, this.schedule.getTickCount(), 0);
		
		//------- TICK 1 -------------
		
		// double inventory_beginning = customer1.getCurrent_outgoing_inventory_level();
		
		for (int i = 1; i <= 1; i++) 
		{			
			this.schedule.schedule(customer1);
			this.schedule.schedule(retailer1);
			this.schedule.schedule(wholesaler1);
			this.schedule.schedule(distributor1);
			this.schedule.schedule(manufacturer1);
	    	this.schedule.execute();
		}
    	
		// simulation clock has advanced
    	assertTrue("Simulation clock has not advanced", this.schedule.getTickCount() > -1);
    			
    	// Customer demand is within boundaries (initially: 10, 25)
    	assertTrue("Demand for next tick is out of range (10..25): " + customer1.getNextDemand(), 10 <= customer1.getNextDemand() && customer1.getNextDemand() <= 25);
    	
    	// the next order quantity is larger than 0 and smaller than the customer demand
    	assertTrue("Next order quantity is out of range", customer1.getNextOrderQuantity() >= 0 && customer1.getNextOrderQuantity() <= 25);
  
    	// Let's do a custom order with qty 1. This order must be received in tick 3.
    	Order order = new Order(1, customer1.getOrderAgent());
    	//System.out.println(order.getId());
    	customer1.getOrderAgent().order(customer1.getTrustAgent(), order);
    	// somewhere in the next 500 Ticks I want my order from the beginning be arrived.
    	
    	while(true) 
    	{			
			this.schedule.schedule(customer1);
			this.schedule.schedule(retailer1);
			this.schedule.schedule(wholesaler1);
			this.schedule.schedule(distributor1);
			this.schedule.schedule(manufacturer1);
			
	    	this.schedule.execute();
	    	
	    	if (customer1.getInventoryAgent().getEverReceivedShipments().get(order.toString()) != null) {
	    		// assertEquals("The small order of qty 1 arrives in a later shipment", customer1.getInventoryAgent().getEverReceivedShipments().get(order.getId()), order);
	    		assertEquals("The small order of qty. 1 arrives in tick 3", 3, schedule.getTickCount(), 0);
	    		//System.out.println("Our order that was sent in tick 1 was received as shipment in tick " + schedule.getTickCount());
	    		break;
	    	}
		}
    	    	
		fail("Not yet implemented");
	}
}