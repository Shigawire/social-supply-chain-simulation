package social_simulation_project;

import java.util.ArrayList;
import java.util.Collection;

import SimulationSetups.SimSetup;
import SimulationSetups.SimSetup1;
import SimulationSetups.SimSetup2;
import SimulationSetups.TrustSetter;
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
		IndirectTrustAgent.reset();
		context.setId("Social Simulation Project");
		context.add(OrderObserver.giveObserver());
		OrderObserver.giveObserver().setAmount(0);
		RunEnvironment.getInstance().setScheduleTickDelay(20);
		SimSetup setup = new SimSetup2();
		TrustSetter s = TrustSetter.getInstance();
		s.setTrustIntegrated(setup.getTrustIntegrated());
		s.setIndirectTrustIntegrated(setup.getIndirectTrustIntegrated());
		s.setInformationSharingIntegrated(setup.getInformationSharingIntegrated());
		// --------------------------------------------------------------------------------
		// Add everything to the simulation's context
		context.add(IndirectTrustAgent.getIndirectTrustAgent());
		context.add(s);
		for(Sale manufacturer : setup.getManufacturerList()){
			context.add(manufacturer);
		}
		
		ArrayList<Manufacturer> manufacturerList_indirectTrust = new ArrayList<Manufacturer>();
		manufacturerList_indirectTrust.addAll((Collection<? extends Manufacturer>) setup.getManufacturerList());
		IndirectTrustAgent.getIndirectTrustAgent().setManufacturerList(manufacturerList_indirectTrust);
		
		for(Sale distributor : setup.getDistributorList()){
			context.add(distributor);
		}
		
		ArrayList<Distributor> distributorList_indirectTrust = new ArrayList<Distributor>();
		distributorList_indirectTrust.addAll((Collection<? extends Distributor>) setup.getDistributorList());
		IndirectTrustAgent.getIndirectTrustAgent().setDistributorList(distributorList_indirectTrust);
		
		for(Sale wholesaler : setup.getWholesalerList()){
			context.add(wholesaler);
		}
		
		ArrayList<Wholesaler> wholesalerList_indirectTrust = new ArrayList<Wholesaler>();
		wholesalerList_indirectTrust.addAll((Collection<? extends Wholesaler>) setup.getWholesalerList());	
		IndirectTrustAgent.getIndirectTrustAgent().setWholesalerList(wholesalerList_indirectTrust);
		
		for(Sale retailer : setup.getRetailerList()){
			context.add(retailer);
		}
		
		ArrayList<Retailer> retailerList_indirectTrust = new ArrayList<Retailer>();
		retailerList_indirectTrust.addAll((Collection<? extends Retailer>) setup.getRetailerList());
		IndirectTrustAgent.getIndirectTrustAgent().setRetailerList(retailerList_indirectTrust);
		
		for(Customer customer : setup.getCustomerList()){
			context.add(customer);
		}
		
		IndirectTrustAgent.getIndirectTrustAgent().setCustomerList(setup.getCustomerList());
		BWeffectMeasurer.getMeasurer().setTheDummys(setup.getCustomerList().get(0),retailerList_indirectTrust.get(0) , 
				wholesalerList_indirectTrust.get(0), distributorList_indirectTrust.get(0));
		context.add(BWeffectMeasurer.getMeasurer());
		return context;
	}
}