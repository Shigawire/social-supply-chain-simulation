package social_simulation_project;

import java.util.ArrayList;

/**
* This class is the generalization for all
* supply chain members. 
*
* @author  PS Development Team
* @since   2015-11-30
*/
public abstract class SupplyChainMember 
{
	protected String id;
	protected int current_inventory_level;
	protected OrderAgent orderAgent;
	protected TrustAgent trustAgent;
	protected InventoryAgent inventoryAgent;
	protected ForecastAgent forecastAgent;
	
	/**
	   * This constructor gives every supply chain member a unique
	   * id in a hexadecimal format.
	   * 
	   */
	public SupplyChainMember(int inventory_level) 
	{
		this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
		
		// Create inventory agent with inventoryLevel
		this.inventoryAgent = new InventoryAgent(inventory_level);
		this.forecastAgent = new ForecastAgent();
	}
	
	// Methods every supply chain member must implement
	public abstract void run();
	public abstract void receiveShipments();
	
	/*
	 * GETTERS
	 */
	public int getCurrent_inventory_level()
	{
		return this.inventoryAgent.getInventoryLevel();
	}
	
	/*
	 * SETTERS
	 */
}