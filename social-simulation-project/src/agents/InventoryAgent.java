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
	private int incoming_inventory_level;
	private int outgoing_inventory_level;

	public InventoryAgent(int incoming_inventory_level, int outgoing_inventory_level) 
	{
		this.outgoing_inventory_level = outgoing_inventory_level;
		this.incoming_inventory_level = incoming_inventory_level;
	}
	
	/**
	   * This method receives goods.
	   * 
	   * @param ?
	   * @return Nothing.
	   */
	//store a received shipment
	public void store(Order shipment) 
	{
		//System.out.println("[Inventory Agent] setting inventory quantitiy from "+ this.incoming_inventory_level + " to level+" +shipment.getQuantity());
		this.incoming_inventory_level += shipment.getPartDelivery();
	}
	
	/**
	   * This method reduces the incoming inventory
	   * of a supply chain member
	   * 
	   * @return Nothing.
	   */
	public void reduceIncomingInventoryLevel(int reduction)
	{
		this.incoming_inventory_level -= reduction;
	}
	public void reduceOutgoingInventoryLevel(int reduction)
	{
		this.outgoing_inventory_level -= reduction;
	}
	public void increaseIncomingInventoryLevel (int addition){
		this.incoming_inventory_level += addition;
	}
	public void increaseOutgoingInventoryLevel (int addition){
		this.outgoing_inventory_level += addition;
	}
	
	/*
	 * GETTERS
	 */
	public int getIncomingInventoryLevel() 
	{
		return this.incoming_inventory_level;
	}
	public int getOutgoingInventoryLevel() 
	{
		return this.outgoing_inventory_level;
	}
	
	/*
	 * SETTERS
	 */
	public void setIncomingInventoryLevel(int inventory_level) 
	{
		this.incoming_inventory_level = inventory_level;
	}
	public void setOutgoingInventoryLevel(int inventory_level) 
	{
		this.outgoing_inventory_level = inventory_level;
	}
}