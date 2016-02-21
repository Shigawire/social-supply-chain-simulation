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
	private int incomingInventoryLevel;
	private int outgoingInventoryLevel;
	private int desiredInventoryLevel;
	private boolean lying;
	private int aInventoryLevel;
	private int bInventoryLevel;
	// wozu?
	private Map<String, Order> everReceivedShipments = new HashMap<String, Order>();

	public InventoryAgent(int incomingInventoryLevel, int outgoingInventoryLevel) 
	{
		this.outgoingInventoryLevel = outgoingInventoryLevel;
		this.incomingInventoryLevel = incomingInventoryLevel;
		this.aInventoryLevel = 0;
		this.bInventoryLevel = 0;
	}
	
	/**
	   * This method receives goods.
	   * 
	   * @param ?
	   * @return Nothing.
	   */
	// store a received shipment
	public void store(Order shipment) 
	{
		// when he is a lying agent it is possible that he will cancel the order based on his desired inventory level
		// and send this partdelivery back
		if (lying) {
			if (desiredInventoryLevel < outgoingInventoryLevel) {
				shipment.setCancelled();
				shipment.getDeliveryAgent().getParent().returning(shipment.getPartDelivery());
				shipment.partDelivery(shipment.getUnfullfilledQuantity());
				shipment.setProcessed(true);
			}
		}
		this.everReceivedShipments.put(shipment.getId(), shipment);
		
		this.incomingInventoryLevel += shipment.getPartDelivery() * (1 - shipment.getfailurePercentage());
	}
	
	// method that sets the desired level
	public void desiredLevel(boolean lying, int desired) 
	{
		this.lying = lying;
		desiredInventoryLevel = desired;
	}
	
	/**
	   * This method reduces the incoming inventory
	   * of a supply chain member
	   * 
	   * @return Nothing.
	   */
	public void reduceIncomingInventoryLevel(int reduction)
	{
		this.incomingInventoryLevel -= reduction;
	}
	
	public void reduceOutgoingInventoryLevel(int reduction)
	{
		this.outgoingInventoryLevel -= reduction;
	}
	
	public void reduceAInventoryLevel(int reduction)
	{
		this.aInventoryLevel -= reduction;
	}
	
	public void reduceBInventoryLevel(int reduction)
	{
		this.bInventoryLevel -= reduction;
	}
	
	public void increaseIncomingInventoryLevel (int addition)
	{
		this.incomingInventoryLevel += addition;
	}
	
	public void increaseOutgoingInventoryLevel (int addition)
	{
		this.outgoingInventoryLevel += addition;
	}
	
	public void increaseAInventoryLevel (int addition)
	{
		this.aInventoryLevel += addition;
	}
	
	public void increaseBInventoryLevel (int addition)
	{
		this.bInventoryLevel += addition;
	}
	
	/*
	 * GETTERS
	 */
	public int getIncomingInventoryLevel() 
	{
		return this.incomingInventoryLevel;
	}
	
	public int getOutgoingInventoryLevel() 
	{
		return this.outgoingInventoryLevel;
	}
	
	public int getAInventoryLevel()
	{
		return this.aInventoryLevel;
	}
	
	public int getBInventoryLevel()
	{
		return this.bInventoryLevel;
	}
	
	public Map<String, Order> getEverReceivedShipments() 
	{
		return this.everReceivedShipments;
	}
	
	/*
	 * SETTERS
	 */
	public void setIncomingInventoryLevel(int inventory_level) 
	{
		this.incomingInventoryLevel = inventory_level;
	}
	
	public void setOutgoingInventoryLevel(int inventory_level) 
	{
		this.outgoingInventoryLevel = inventory_level;
	}
	
	public void SetAInventoryLevel(int inventory_level)
	{
		this.aInventoryLevel = inventory_level;
	}
	
	public void SetBInventoryLevel(int inventory_level)
	{
		this.bInventoryLevel = inventory_level;
	}
}