package tests;

import static org.junit.Assert.assertEquals;
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
import social_simulation_project.SimulationBuilder;
import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.Sale;
import actors.Wholesaler;

/*
 * http://repast.sourceforge.net/docs/RepastModelTesting.pdf documentation
 */

public class OrderTest {

	private Context<Object> context;
	private Schedule schedule;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/*
	public void setUp() throws Exception {
		Schedule schedule = new Schedule();
		RunEnvironment.init(schedule, null, null, true);
		
		Context<Object> context = new DefaultContext<Object>();	
	}
	*/
	@Before
    public void setUp() throws Exception {
		
		this.schedule = new Schedule();
		RunEnvironment.init(this.schedule, new DefaultScheduleRunner(), null, false);
		this.context = new DefaultContext<Object>();
		//SimulationBuilder builder = new SimulationBuilder();
		//this.context = builder.build (context);
		RunState.init().setMasterContext ( this.context );
    }

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test() {
		
		this.context.add(OrderObserver.giveObserver());
		
		OrderObserver.giveObserver().setAmount(0);
		
		Manufacturer manufacturer1 = new Manufacturer(10, 0,100);
		ArrayList<Sale> manufacturerList = new ArrayList<Sale>();
		manufacturerList.add(manufacturer1);
		

		Distributor distributor1 = new Distributor(manufacturerList, 15,11, 50);
		
		ArrayList<Sale> distributorList = new ArrayList<Sale>();
		distributorList.add(distributor1);

		Wholesaler wholesaler1 = new Wholesaler(distributorList, 12, 11,40);

		ArrayList<Sale> wholesalerList = new ArrayList<Sale>();
		wholesalerList.add(wholesaler1);	
		
		Retailer retailer1 = new Retailer(wholesalerList, 10, 10,10);
		
		ArrayList<Sale> retailerList = new ArrayList<Sale>();
		retailerList.add(retailer1);
		
		Customer customer1 = new Customer(retailerList, 10,15);
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer1);

		this.context.add(manufacturer1);
		this.context.add(distributor1);
		this.context.add(wholesaler1);
		this.context.add(retailer1);
		this.context.add(customer1);
		
		//simulation is not initialized yet
		assertEquals(-1, this.schedule.getTickCount(), 0);
		
		//------- TICK 1 -------------
		
		//double inventory_beginning = customer1.getCurrent_outgoing_inventory_level();
		
		for (int i=0; i<=1; i++) {			
			this.schedule.schedule(customer1);
			this.schedule.schedule(retailer1);
			this.schedule.schedule(wholesaler1);
	    	this.schedule.execute();
	    	System.out.println("================Demand at tick " + this.schedule.getTickCount()+ ": " + customer1.getNextDemand());
		}
    	
    	
    	assertEquals(1, this.schedule.getTickCount(), 0);
    	
    	
   
		
		fail("Not yet implemented");		

	}

}