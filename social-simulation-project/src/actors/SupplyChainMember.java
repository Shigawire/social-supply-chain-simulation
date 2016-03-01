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
	// give the profile of the actor (--> 1=A,2=B,3=C)
	protected Profile ActorProfile;
	
	//the Actor's Agents
	protected TrustAgent trustAgent;
	protected InventoryAgent inventoryAgent;
	protected ForecastAgent forecastAgent;
	
	
	// Map with dimension ratings gives the importance for every dimension
	// of trust for every Supply Chain member based on the profiles
	/**
	 * This variable maps the trust-dimensions (reliability, competence, shared values, quality) to the agent-specific ratings.
	 */
	
	protected Map<DimensionType, Double> dimensionRatings;
														
	//The constructor is usually called from a sub-class with the super() method.
	public SupplyChainMember(int incomingInventoryLevel, int outgoingInventoryLevel, Profile profile) 
	{
		//assign a specific profile to this actor. Read more about these profiles in the documentation.
		this.ActorProfile = profile;

		//create and assign necessary agents to this member.
		
		//initialize the inventory agent with the pre-defined inventory levels as set in the simulation builders
		this.inventoryAgent = new InventoryAgent(incomingInventoryLevel, outgoingInventoryLevel);
		this.forecastAgent = new ForecastAgent();
		
		//fill the dimension-rating map with specific values
		
		this.dimensionRatings = new HashMap<DimensionType, Double>();
		
		dimensionRatings.put(DimensionType.RELIABILITY, profile.getReliabilityRelevance());
		dimensionRatings.put(DimensionType.COMPETENCE, profile.getCompetenceRelevance());
		dimensionRatings.put(DimensionType.SHARED_VALUES, profile.getSharedValuesRelevance());
		dimensionRatings.put(DimensionType.QUALITY, profile.getQualityRelevance());
	}
	
	//Methods every supply chain member must implement
	
	//run method what every supply chain actor does in the tick
	public abstract void run(); 
	
	//This method requires every supply chain actor to receive and process their incoming shipments. 
	//Imagine it's like a postbox...
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
	public void setProfile(Profile profile){
		ActorProfile = profile;
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
		return ActorProfile;
	}
}