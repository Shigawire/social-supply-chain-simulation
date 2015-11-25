package ticks_implement;

import ticks_interface.InventoryAgent;

public class RInventoryAgent implements InventoryAgent{
	private double inventory=20;
	@Override
	public void store(double shipment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(double shipment) {
		inventory=inventory-shipment;
		//jetzt müsste eigentlich hier bestellt werden
	}

	@Override
	public double checkInventory(double customerDemand) {
		//schauen ob auslieferbar
		if(customerDemand>inventory){
			return (customerDemand-inventory);
		}
		else{
			return 0;
		}
	}

}
