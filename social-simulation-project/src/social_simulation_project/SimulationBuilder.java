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
		RunEnvironment.getInstance().endAt(109);
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
		for (Sale manufacturer : setup.getManufacturerList())
		{
			context.add(manufacturer);
		}
		
		ArrayList<Manufacturer> manufacturerListIndirectTrust = new ArrayList<Manufacturer>();
		manufacturerListIndirectTrust.addAll((Collection<? extends Manufacturer>) setup.getManufacturerList());
		IndirectTrustAgent.getIndirectTrustAgent().setManufacturerList(manufacturerListIndirectTrust);
		
		for (Sale distributor : setup.getDistributorList())
		{
			context.add(distributor);
		}
		
		ArrayList<Distributor> distributorListIndirectTrust = new ArrayList<Distributor>();
		distributorListIndirectTrust.addAll((Collection<? extends Distributor>) setup.getDistributorList());
		IndirectTrustAgent.getIndirectTrustAgent().setDistributorList(distributorListIndirectTrust);
		
		for (Sale wholesaler : setup.getWholesalerList())
		{
			context.add(wholesaler);
		}
		
		ArrayList<Wholesaler> wholesalerListIndirectTrust = new ArrayList<Wholesaler>();
		wholesalerListIndirectTrust.addAll((Collection<? extends Wholesaler>) setup.getWholesalerList());	
		IndirectTrustAgent.getIndirectTrustAgent().setWholesalerList(wholesalerListIndirectTrust);
		
		for (Sale retailer : setup.getRetailerList())
		{
			context.add(retailer);
		}
		
		ArrayList<Retailer> retailerListIndirectTrust = new ArrayList<Retailer>();
		retailerListIndirectTrust.addAll((Collection<? extends Retailer>) setup.getRetailerList());
		IndirectTrustAgent.getIndirectTrustAgent().setRetailerList(retailerListIndirectTrust);
		
		for (Customer customer : setup.getCustomerList())
		{
			context.add(customer);
		}
		
		IndirectTrustAgent.getIndirectTrustAgent().setCustomerList(setup.getCustomerList());
		BWeffectMeasurer.getMeasurer().setTheDummys(setup.getCustomerList().get(0),
													retailerListIndirectTrust.get(0), 
													wholesalerListIndirectTrust.get(0), 
													distributorListIndirectTrust.get(0));
		context.add(BWeffectMeasurer.getMeasurer());
		return context;
	}
}