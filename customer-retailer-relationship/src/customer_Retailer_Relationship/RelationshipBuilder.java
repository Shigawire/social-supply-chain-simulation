package customer_Retailer_Relationship;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;

public class RelationshipBuilder implements ContextBuilder<Object> {
	
	@Override
	public Context<Object> build(Context<Object> context) {
		context.setId("Customer Retailer Relationship");
		Customer fabian = new Customer();
		Retailer jakob = new Retailer();
		fabian.setRetailer(jakob);
		
		context.add(fabian);
		context.add(jakob);
		
		return context;
	}
}
