import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import simulation_implement.Customer;
import simulation_implement.Retailer;


public class SimulationBuilder implements ContextBuilder<Object> {
	
	@Override
	public Context<Object> build(Context<Object> context) {
		
		List<Echelon[]> echelons = new ArrayList<Echelon[]>();
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
