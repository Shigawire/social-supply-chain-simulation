package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import artefacts.Inventory;
import artefacts.Order;

/**
* This class represents an inventory agent. This one is 
* responsible for inventory handling (all supply chain members).
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class InventoryAgent 
{
	/**
	 * This inventory holds the intermediate production goods that are going to be used during the 
	 * "manufacturing"/"assembly" process, respectively for consumption in the case of the customer.
	 */
	protected Inventory incomingInventory;
	
	/**
	 * This inventory holds the outgoing and manufactured elements - basically those that are ready to be sold and shipped.
	 */
	protected Inventory outgoingInventory;
	
	/**
	 * These two inventory are solely used by the Manufacturer in order to simulate the manufacturing process of an item
	 * that requires two different elements
	 */
	protected Inventory aInventory;
	protected Inventory bInventory;
	
	
	//defines the desired inventory level - usually computed by the forecast agent and then stored for further processing
	private int desiredInventoryLevel;
	
	private boolean lying;
	

	/**
	 * This variably helps us keeping track of all shipments that were received once in a while.
	 * We are going to use this variable in order to determine a shipment was on time.
	 */
	private Map<String, Order> everReceivedShipments = new HashMap<String, Order>();

	public InventoryAgent(int incomingInventoryLevel, int outgoingInventoryLevel) 
	{
		this.incomingInventory = new Inventory(incomingInventoryLevel);
		
		this.outgoingInventory = new Inventory(outgoingInventoryLevel);
		
		
		//initialize manufacturing inventory Levels
		this.aInventory = new Inventory(0);
		this.bInventory = new Inventory(0);
	}
	
	
	// handle and store a received shipment
	public void store(Order shipment) 
	{
		/**
		 *  if it is a lying agent it is possible that he will cancel the order based on his desired inventory level
		 *  and return the shipment or partial delivery
		 */
		if (this.lying) {
			
			//cancel an order and return at least a part of the shipment
			if (this.desiredInventoryLevel < this.outgoingInventory.getLevel()) {
				shipment.setCancelled();
				
				//TODO can this be simplified?
				shipment.getDeliveryAgent().getParent().returning(shipment.getPartDelivery());
				
				//TODO what's that?
				shipment.partDelivery(shipment.getUnfullfilledQuantity());
				
				//finally, set the shipment as fulfilled.
				shipment.setProcessed(true);
			}
		}
		
		//add this shipment to our infinite history list
		this.everReceivedShipments.put(shipment.toString(), shipment);
		
		//calculate how many items survived the deep abyss of the DHL delivery truck.
		int undamagedQuantity = (int)(shipment.getPartDelivery() * (1 - shipment.getfailurePercentage()));
		
		//update the incoming inventory accordingly
		this.incomingInventory.setLevel(this.incomingInventory.getLevel() + undamagedQuantity);
	}
	
	//set the desired inventory level and determine if the agent will change the lying behaviour for future shipments.
	public void desiredLevel(boolean lying, int desired) 
	{
		this.lying = lying;
		desiredInventoryLevel = desired;
	}
	
	/**
	   * This method reduces the incoming inventory
	 */
	
	public void reduceIncomingInventoryLevel(int reduction)
	{
		this.incomingInventory.setLevel(this.incomingInventory.getLevel() - reduction);
	}
	
	public void reduceOutgoingInventoryLevel(int reduction)
	{
		this.outgoingInventory.setLevel(this.outgoingInventory.getLevel() - reduction);
	}
	
	public void reduceAInventoryLevel(int reduction)
	{
		this.aInventory.setLevel(this.aInventory.getLevel() - reduction);
	}
	
	public void reduceBInventoryLevel(int reduction)
	{
		this.bInventory.setLevel(this.bInventory.getLevel() - reduction);
	}
	
	public void increaseIncomingInventoryLevel (int addition)
	{
		this.incomingInventory.setLevel(this.incomingInventory.getLevel() + addition);
	}
	
	public void increaseOutgoingInventoryLevel (int addition)
	{
		this.outgoingInventory.setLevel(this.outgoingInventory.getLevel() + addition);
	}
	
	public void increaseAInventoryLevel (int addition)
	{
		this.aInventory.setLevel(this.aInventory.getLevel() + addition);
	}
	
	public void increaseBInventoryLevel (int addition)
	{
		this.bInventory.setLevel(this.bInventory.getLevel() + addition);
	}
	
	/*
	 * GETTERS
	 */
	public int getIncomingInventoryLevel() 
	{
		return this.incomingInventory.getLevel();
	}
	
	public int getOutgoingInventoryLevel() 
	{
		return this.outgoingInventory.getLevel();
	}
	
	public int getAInventoryLevel()
	{
		return this.aInventory.getLevel();
	}
	
	public int getBInventoryLevel()
	{
		return this.bInventory.getLevel();
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
		this.incomingInventory.setLevel(inventory_level);
	}
	
	public void setOutgoingInventoryLevel(int inventory_level) 
	{
		this.outgoingInventory.setLevel(inventory_level);
	}
	
	public void SetAInventoryLevel(int inventory_level)
	{
		this.aInventory.setLevel(inventory_level);
	}
	
	public void SetBInventoryLevel(int inventory_level)
	{
		this.bInventory.setLevel(inventory_level);
	}
}