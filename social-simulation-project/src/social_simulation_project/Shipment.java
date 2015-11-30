package social_Simulation_Project;

import repast.simphony.engine.environment.RunEnvironment;

public class Shipment {
	private int quantity;
	private int shipped_at;
	private String id;
	private SupplyChainMember shipper;
	
	public Shipment(int quantity, SupplyChainMember shipper) {
		this.quantity = quantity;
		//generate an ID, so it is easier to track the Order through the system :)
		this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
		this.shipped_at = (int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		this.shipper = shipper;
	}

}
