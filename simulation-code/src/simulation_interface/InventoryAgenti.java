package simulation_interface;

public interface InventoryAgenti {
	//method for checking Inventory
	public void store(double shipment);
	public void remove(double shipment);
	public double checkInventory(double customerDemand);
}
