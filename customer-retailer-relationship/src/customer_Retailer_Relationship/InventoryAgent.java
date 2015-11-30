package customer_Retailer_Relationship;

public class InventoryAgent {
	private int inventory_level;

	public InventoryAgent() {
		
	}
	
	public void receiveShipment(Shipment shipment) {
		this.inventory_level += shipment.getShipmentAmount();
	}
	
	/*
	 * SETTERS
	 */
	public void setInventoryLevel(int inventory_level) {
		this.inventory_level = inventory_level;
	}
	
	/*
	 * GETTERS
	 */
	
	public int getInventoryLevel() {
		return this.inventory_level;
	}
	
}
