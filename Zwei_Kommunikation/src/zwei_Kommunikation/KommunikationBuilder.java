package zwei_Kommunikation;


import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;


public class KommunikationBuilder implements ContextBuilder<Object>{

	@Override
	public Context<Object> build(Context<Object> context) {
				
		context.setId("Zwei_Kommunikation");
		Customer cus= new Customer();
		Retailer neuer= new Retailer();
		cus.setMeinRetailer(neuer);
		context.add(cus);
		context.add(neuer);
		
		RunEnvironment.getInstance().setScheduleTickDelay(100);
				
		return context;
	}

}
