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
		
		/*
		 * 
		 * D I S T R I B U T O R S
		 * 
		 */
		
		// For the beginning create 2 distributors
		// First parameter is price
		// Second parameter is initial inventory level
		Distributor distributor1 = new Distributor(50, 500);
		Distributor distributor2 = new Distributor(60, 1700);
		
		// Create a list of distributors and add every 
		// distributor to that list (for now only 1)
		ArrayList<Distributor> distributorList = new ArrayList<Distributor>();
		distributorList.add(distributor1);
//		distributorList.add(distributor2);
		
		// --------------------------------------------------------------------------------
		
		/*
		 * 
		 * W H O L E S A L E R S
		 * 
		 */
		
		// For the beginning create 2 wholesalers
		// First parameter is price
		// Second parameter is initial inventory level
		Wholesaler wholesaler1 = new Wholesaler(distributorList, 20, 400);
		Wholesaler wholesaler2 = new Wholesaler(distributorList, 15, 300);
		
		// Create a list of wholesalers and add every 
		// wholesaler to that list (for now only 1)
		ArrayList<Wholesaler> wholesalerList = new ArrayList<Wholesaler>();
		wholesalerList.add(wholesaler1);	
//		wholesalerList.add(wholesaler2);	
		
		// --------------------------------------------------------------------------------
		
		/*
		 * 
		 * R E T A I L E R S
		 * 
		 */
		
		// For the beginning create 2 retailers
		// First parameter is price
		// Second parameter is initial inventory level
		Retailer retailer1 = new Retailer(wholesalerList, 10, 100);
		Retailer retailer2 = new Retailer(wholesalerList, 5, 100);
		
		// Create a list of distributors and add every 
		// distributor to that list (for now only 1)
		ArrayList<Retailer> retailerList = new ArrayList<Retailer>();
		retailerList.add(retailer1);
//		retailerList.add(retailer2);
		
		// --------------------------------------------------------------------------------
		
		/*
		 * 
		 * C U S T O M E R
		 * 
		 */
		
		// We have only one customer in our supply chain
		// Maybe the customer class could be designed as 
		// a singleton class
		Customer customer1 = new Customer(retailerList, 50);
		
		// --------------------------------------------------------------------------------
		
		// Add everything to the simulation's context
		
		context.add(distributor1);
//		context.add(distributor2);
		
		context.add(wholesaler1);
//		context.add(wholesaler2);
		
		context.add(retailer1);
//		context.add(retailer2);
		context.add(customer1);
		
		return context;
	}
}