package actors;

import java.util.HashMap;
import java.util.Map;

import agents.ForecastAgent;
import agents.InventoryAgent;
import agents.OrderAgent;
import agents.TrustAgent;
import artefacts.trust.DimensionType;

/**
* This class is the generalization for all
* supply chain members. 
*
* @author  PS Development Team
* @since   2015-11-30
*/
public abstract class SupplyChainMember 
{
	protected String id;//id for every member
	protected int current_incoming_inventory_level;//inventory with items that has to be used to produce sellable items
	protected int current_outgoing_inventory_level;//inventory with sellable itmes
	protected TrustAgent trustAgent;//Agent for the trust to suppliers
	protected InventoryAgent inventoryAgent;
	protected ForecastAgent forecastAgent;
	protected Map<DimensionType, Double> dimensionRatings;//Map with dimension ratings gives the importance for
														//every dimension of trust for every Supply Chain member based on the profiles
	
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
		
		this.dimensionRatings = new HashMap<DimensionType, Double>();
		
		dimensionRatings.put(DimensionType.RELIABILITY, 0.25);
		dimensionRatings.put(DimensionType.COMPETENCE, 0.25);
		dimensionRatings.put(DimensionType.SHARED_VALUES, 0.25);
		dimensionRatings.put(DimensionType.QUALITY, 0.25);
	}
	
	// Methods every supply chain member must implement
	public abstract void run();//run method what every supply chain member does in the tick
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
	
	public Map<DimensionType, Double> getTrustDimensionRatings() {
		return this.dimensionRatings;
	}
	
	public TrustAgent getTrustAgent() {
		return this.trustAgent;
	}

	public void updateList(OrderAgent orderAgent, int quantity) {
		// is implemendet in the subclasses in every except for customer
		
	}

	public void going2order(OrderAgent orderAgent) {
		// is implemendet in the subclasses in every except for customer
		
	}
	
	public InventoryAgent getInventoryAgent() {
		return this.inventoryAgent;
	}
	
	/*
	 * SETTERS
	 */
}