package ticks_implement;

import ticks_interface.InventoryAgent;

public class CInventoryAgent implements InventoryAgent{
	public double inventory=10;
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
		// TODO Auto-generated method stub
		
	}

	

}
