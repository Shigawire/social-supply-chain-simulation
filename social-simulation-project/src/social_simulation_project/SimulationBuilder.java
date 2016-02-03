package social_simulation_project;

import java.util.ArrayList;

import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.Sale;
import actors.Wholesaler;
import agents.IndirectTrustAgent;
import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;

/**
* This class is the starting point for the repast
* simulation.
*
* @author  PS Development Team
* @since   2015-12-03
*/
public class SimulationBuilder implements ContextBuilder<Object> 
{	
	@Override
	public Context<Object> build(Context<Object> context) 
	{	
		context.setId("Social Simulation Project");
		context.add(OrderObserver.giveObserver());
		OrderObserver.giveObserver().setAmount(0);
		RunEnvironment.getInstance().setScheduleTickDelay(20);
		
		Manufacturer manufacturer1 = new Manufacturer(10, 200,200);
		ArrayList<Sale> manufacturerList = new ArrayList<Sale>();
		manufacturerList.add(manufacturer1);
		//for the indirect trust a special List is needed
		ArrayList<Manufacturer> manufacturerList_indirectTrust = new ArrayList<Manufacturer>();
		manufacturerList_indirectTrust.add(manufacturer1);
		IndirectTrustAgent.getIndirectTrustAgent().setManufacturerList(manufacturerList_indirectTrust);
		/*
		 * 
		 * D I S T R I B U T O R S
		 * 
		 */
		
		// For the beginning create 2 distributors
		// First parameter is price
		// Second parameter is initial inventory level
		Distributor distributor1 = new Distributor(manufacturerList, 100,110, 50);
		//Distributor distributor2 = new Distributor(manufacturerList, 16,11, 55);
		
		// Create a list of distributors and add every 
		// distributor to that list (for now only 1)
		ArrayList<Sale> distributorList = new ArrayList<Sale>();
		distributorList.add(distributor1);
		distributor1.setLying();
		//distributorList.add(distributor2);

		//for the indirect trust a special List is needed
		ArrayList<Distributor> distributorList_indirectTrust = new ArrayList<Distributor>();
		distributorList_indirectTrust.add(distributor1);
		IndirectTrustAgent.getIndirectTrustAgent().setDistributorList(distributorList_indirectTrust);
		// --------------------------------------------------------------------------------
		
		/*
		 * 
		 * W H O L E S A L E R S
		 * 
		 */
		
		// For the beginning create 2 wholesalers
		// First parameter is price
		// Second parameter is initial inventory level
		Wholesaler wholesaler1 = new Wholesaler(distributorList, 120, 110,40);
		//Wholesaler wholesaler2 = new Wholesaler(distributorList, 11, 11,38);
		
		// Create a list of wholesalers and add every 
		// wholesaler to that list (for now only 1)
		ArrayList<Sale> wholesalerList = new ArrayList<Sale>();
		wholesalerList.add(wholesaler1);	
		//wholesalerList.add(wholesaler2);	
		
		//special list for indirect trust
		ArrayList<Wholesaler> wholesalerList_indirectTrust = new ArrayList<Wholesaler>();
		wholesalerList_indirectTrust.add(wholesaler1);	
		IndirectTrustAgent.getIndirectTrustAgent().setWholesalerList(wholesalerList_indirectTrust);
		// --------------------------------------------------------------------------------
		
		/*
		 * 
		 * R E T A I L E R S
		 * 
		 */
		
		// For the beginning create 2 retailers
		// First parameter is price
		// Second parameter is initial inventory level
		Retailer retailer1 = new Retailer(wholesalerList, 100, 100,10);
		//Retailer retailer2 = new Retailer(wholesalerList, 15, 10,11);
		
		// Create a list of Retailers and add every 
		// retailer to that list (for now only 1)
		ArrayList<Sale> retailerList = new ArrayList<Sale>();
		retailerList.add(retailer1);
		//retailerList.add(retailer2);

		//special list for indirect trust
		ArrayList<Retailer> retailerList_indirectTrust = new ArrayList<Retailer>();
		retailerList_indirectTrust.add(retailer1);
		IndirectTrustAgent.getIndirectTrustAgent().setRetailerList(retailerList_indirectTrust);
		// --------------------------------------------------------------------------------
		
		/*
		 * 
		 * C U S T O M E R
		 * 
		 */
		
		// We have only one customer in our supply chain
		Customer customer1 = new Customer(retailerList, 100,150);
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer1);
		IndirectTrustAgent.getIndirectTrustAgent().setCustomerList(customerList);
	
		// --------------------------------------------------------------------------------
		// Add everything to the simulation's context
		
		context.add(IndirectTrustAgent.getIndirectTrustAgent());
		context.add(manufacturer1);
		
		context.add(distributor1);
		//context.add(distributor2);
		
		context.add(wholesaler1);
		//context.add(wholesaler2);
		
		context.add(retailer1);
		//context.add(retailer2);
		context.add(customer1);
		// the observer for openOrders

		return context;
	}
}