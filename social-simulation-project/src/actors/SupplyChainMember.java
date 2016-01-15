package actors;

import agents.ForecastAgent;
import agents.InventoryAgent;
import agents.TrustAgent;

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
	protected int current_incoming_inventory_level;
	protected int current_outgoing_inventory_level;
	protected TrustAgent trustAgent;
	protected InventoryAgent inventoryAgent;
	protected ForecastAgent forecastAgent;
	
	/**
	   * This constructor gives every supply chain member a unique
	   * id in a hexadecimal format.
	   * 
	   */
	public SupplyChainMember(int incoming_inventory_level, int outgoing_inventory_level) 
	{
		this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
		
		// Create inventory agent with inventoryLevels
		this.inventoryAgent = new InventoryAgent(incoming_inventory_level,outgoing_inventory_level);
		this.forecastAgent = new ForecastAgent();
	}
	
	// Methods every supply chain member must implement
	public abstract void run();
	public abstract void receiveShipments();
	
	/*
	 * GETTERS
	 */
	public int getCurrent_incoming_inventory_level()
	{
		return this.inventoryAgent.getIncomingInventoryLevel();
	}
	public int getCurrent_outgoing_inventory_level()
	{
		return this.inventoryAgent.getOutgoingInventoryLevel();
	}
	/*
	 * SETTERS
	 */
}