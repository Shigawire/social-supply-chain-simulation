package actors;

import java.util.HashMap;
import java.util.Map;

import agents.ForecastAgent;
import agents.InventoryAgent;
import agents.OrderAgent;
import agents.TrustAgent;
import artefacts.Profile;
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
	protected Profile myProfile; // give the profile of the actor (--> 1=A,2=B,3=C)
	protected String id; // id for every member
	protected int currentIncomingInventoryLevel;
	// inventory with items that has to be used to produce sellable items
	// respectively for the customer the inventory of items he consumes
	protected int currentOutgoingInventoryLevel; // inventory with produced and therfore now sellable itmes
	protected TrustAgent trustAgent; // Agent for the trust to suppliers
	protected InventoryAgent inventoryAgent;
	protected ForecastAgent forecastAgent;
	
	// Map with dimension ratings gives the importance for every dimension
	// of trust for every Supply Chain member based on the profiles
	protected Map<DimensionType, Double> dimensionRatings;
														
	/**
	   * This constructor gives every supply chain member a unique
	   * id in a hexadecimal format.
	   * 
	   */
	public SupplyChainMember(int incomingInventoryLevel, int outgoingInventoryLevel,Profile p) 
	{
		this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
		this.myProfile=p;
		// Create inventory agent with inventoryLevels
		this.inventoryAgent = new InventoryAgent(incomingInventoryLevel, outgoingInventoryLevel);
		this.forecastAgent = new ForecastAgent();
		
		this.dimensionRatings = new HashMap<DimensionType, Double>();
		
		dimensionRatings.put(DimensionType.RELIABILITY, myProfile.getReliabilityRelevance());
		dimensionRatings.put(DimensionType.COMPETENCE, myProfile.getCompetenceRelevance());
		dimensionRatings.put(DimensionType.SHARED_VALUES, myProfile.getSharedValuesRelevance());
		dimensionRatings.put(DimensionType.QUALITY, myProfile.getQualityRelevance());
	}
	
	// Methods every supply chain member must implement
	public abstract void run(); // run method what every supply chain member does in the tick
	public abstract void receiveShipments();
	
	// updates the list of buyers needed for information sharing
	// is implemented in every subclass except for customer
	public void updateList(OrderAgent orderAgent, int quantity)
	{
		
	}
	
	// is called by the buyer, if he trust this actor and will not order at this actor
	// is implemented in every subclass except for customer
	public void going2order(OrderAgent orderAgent) 
	{
		
	}
	
	// used if an actor returns deliveries/part deliveries he got	
	public void returning(int partDelivery) 
	{
		inventoryAgent.increaseOutgoingInventoryLevel(partDelivery);
	}

	public Object getProcurementAgent() 
	{
		// implemented for relevant actors in Buy
		return null;
	}
	public void setProfile(Profile p){
		myProfile=p;
	}
	
	/*
	 * GETTERS
	 */
	public int getCurrentIncomingInventoryLevel()
	{
		return this.inventoryAgent.getIncomingInventoryLevel();
	}
	
	public int getCurrentOutgoingInventoryLevel()
	{
		return this.inventoryAgent.getOutgoingInventoryLevel();
	}
	
	public Map<DimensionType, Double> getTrustDimensionRatings() 
	{
		return this.dimensionRatings;
	}
	
	public TrustAgent getTrustAgent() 
	{
		return this.trustAgent;
	}
	
	public InventoryAgent getInventoryAgent()
	{
		return this.inventoryAgent;
	}
	public Profile getProfile(){
		return myProfile;
	}
}