package agents;

import artefacts.Order;

/**
* This class represents an inventory agent. They are 
* responsible for inventory (all supply chain members).
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class InventoryAgent 
{
	private int inventory_level;

	public InventoryAgent(int inventory_level) 
	{
		this.inventory_level = inventory_level;
	}
	
	/**
	   * This method receives goods.
	   * 
	   * @param ?
	   * @return Nothing.
	   */
	public void store(Order shipment) 
	{
		//System.out.println("[Inventory Agent] setting inventory quantitiy from "+ this.inventory_level + " to level+" +shipment.getQuantity());
		this.inventory_level += shipment.getQuantity();
	}
	
	/**
	   * This method reduces the inventory
	   * of a supply chain member
	   * 
	   * @return Nothing.
	   */
	public void reduceInventoryLevel(int reduction)
	{
		this.inventory_level -= reduction;
	}
	
	/*
	 * GETTERS
	 */
	public int getInventoryLevel() 
	{
		return this.inventory_level;
	}
	
	/*
	 * SETTERS
	 */
	public void setInventoryLevel(int inventory_level) 
	{
		this.inventory_level = inventory_level;
	}
}