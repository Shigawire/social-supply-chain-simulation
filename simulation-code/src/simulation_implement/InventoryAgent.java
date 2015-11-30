package simulation_implement;

import simulation_interface.InventoryAgenti;

public abstract class InventoryAgent implements InventoryAgenti{
	double inventory=0;
	@Override
	public double checkInventory(double demandAtMoment) {
		if(2*demandAtMoment>inventory){
			return (2*demandAtMoment-inventory);
		}
		else{
			return 0;
		}
	}

	@Override
	public void store(double shipment) {
		inventory=shipment+inventory;
		
	}

	@Override
	public void remove(double shipment) {
		inventory=inventory-shipment;
		//jetzt muesste eigentlich hier bestellt werden
		
	}

	

}
