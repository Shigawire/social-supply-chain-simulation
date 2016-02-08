package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	private int desired_inventory_level;
	private boolean lying;
	private int a_inventory_level;
	private int b_inventory_level;
	//wozu?
	private Map<String, Order> everReceivedShipments = new HashMap<String, Order>();

	public InventoryAgent(int incoming_inventory_level, int outgoing_inventory_level) 
	{
		this.outgoing_inventory_level = outgoing_inventory_level;
		this.incoming_inventory_level = incoming_inventory_level;
		this.a_inventory_level = 0;
		this.b_inventory_level = 0;
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
		if(lying){
			if(desired_inventory_level<outgoing_inventory_level){
				shipment.setCancelled();
				shipment.getDeliveryAgent().getParent().returning(shipment.getPartDelivery());
				shipment.partDelivery(shipment.getUnfullfilledQuantity());
				shipment.setProcessed(true);
			}
		}
		this.everReceivedShipments.put(shipment.getId(), shipment);
		
		this.incoming_inventory_level += shipment.getPartDelivery()*(1-shipment.getfailurePercentage());
	}
	//method that sets the desired level
	public void desiredLevel(boolean lying,int desired){
		this.lying=lying;
		desired_inventory_level=desired;
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
	public void reduceAInventoryLevel(int reduction)
	{
		this.a_inventory_level -= reduction;
	}
	public void reduceBInventoryLevel(int reduction)
	{
		this.b_inventory_level -= reduction;
	}
	public void increaseIncomingInventoryLevel (int addition){
		this.incoming_inventory_level += addition;
	}
	public void increaseOutgoingInventoryLevel (int addition){
		this.outgoing_inventory_level += addition;
	}
	public void increaseAInventoryLevel (int addition){
		this.a_inventory_level += addition;
	}
	public void increaseBInventoryLevel (int addition){
		this.b_inventory_level += addition;
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
	public int getAInventoryLevel()
	{
		return this.a_inventory_level;
	}
	public int getBInventoryLevel()
	{
		return this.b_inventory_level;
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
	public void SetAInventoryLevel(int inventory_level)
	{
		this.a_inventory_level=inventory_level;
	}
	public void SetBInventoryLevel(int inventory_level)
	{
		this.b_inventory_level=inventory_level;
	}
	
	public Map<String, Order> getEverReceivedShipments() {
		return this.everReceivedShipments;
	}
}