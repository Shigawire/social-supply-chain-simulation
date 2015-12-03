package social_simulation_project;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;

/**
* This class is the starting point for the repast
* simulation.
*
* @author  PS Development Team
* @since   03-12-2015
*/
public class SimulationBuilder implements ContextBuilder<Object> 
{	
	@Override
	public Context<Object> build(Context<Object> context) 
	{	
		context.setId("Social Simulation Project");
		
		Retailer retailer1 = new Retailer(10, 100);
		Retailer retailer2 = new Retailer(5, 100);
		
		ArrayList<Retailer> retailerList = new ArrayList();
		retailerList.add(retailer1);
//		retailerList.add(retailer2);
		
		Customer customer1 = new Customer(retailerList, 50);
		
		context.add(retailer1);
//		context.add(retailer2);
		context.add(customer1);
		
		return context;
	}
}