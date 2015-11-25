package simulation_interface;

public interface InventoryAgent {
	//method for checking Inventory
	public void store(double shipment);
	public void remove(double shipment);
	public double checkInventory(double customerDemand);
}
