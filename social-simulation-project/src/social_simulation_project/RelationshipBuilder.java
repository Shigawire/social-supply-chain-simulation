package social_Simulation_Project;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;

public class RelationshipBuilder implements ContextBuilder<Object> {
	
	@Override
	public Context<Object> build(Context<Object> context) {
		
		List<Echelon[]> echelon = new ArrayList<Echelon[]>();
		List<Retailer> echelonList["retailer"] = new ArrayList<Retailer>();
		
		
		context.setId("Customer Retailer Relationship");
		Customer fabian = new Customer();
		Retailer jakob = new Retailer();
		
		echelonList["retailer"].add(jakob);
		
		fabian.setRetailer(jakob);
		
		context.add(fabian);
		context.add(jakob);
		
		return context;
	}
}
