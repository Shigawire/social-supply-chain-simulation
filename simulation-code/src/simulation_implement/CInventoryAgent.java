package simulation_implement;

public class CInventoryAgent extends InventoryAgent{
	public double inventory=10;
	@Override
	public double checkInventory(double demandAtMoment) {
		//Frage ob das vielleicht eher in den Forecaster gehoert! jetzt erstmal Loesung 
		//eine Klasse inventoryAgent fuer Retailer und fuer Customer
		if(2*demandAtMoment>inventory){
			return (2*demandAtMoment-inventory);
		}
		else{
			return 0;
		}
	}
}
