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
	protected TrustAgent trustAgent;
	protected InventoryAgent inventoryAgent;
	protected ForecastAgent forecastAgent;
	
	/**
	   * This constructor gives every supply chain member a unique
	   * id in a hexadecimal format.
	   * 
	   */
	public SupplyChainMember() 
	{
		this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
	}
	
	/*
	 * GETTERS
	 */
	
	/*
	 * SETTERS
	 */
}