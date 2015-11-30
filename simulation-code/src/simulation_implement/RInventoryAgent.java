package simulation_implement;

public class RInventoryAgent extends InventoryAgent{
	private double inventory=20;


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
