package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
import agents.TrustAgent;
import artefacts.Profile;
import artefacts.trust.Trust;


/**
 * This is an abstract class, providing the base for every Actor in the Supply Chain 
 * who is supposed to be a consumer/buying actor only.
 */

public abstract class BuyingActor extends SupplyChainMember
{
	//A list of necessary Agents for the BuyingActor
	protected OrderAgent orderAgent;
	protected ProcurementAgent procurementAgent;
	protected TrustAgent trustAgent;
	
	//Should this actor be lying? Initially, no actor is lying until it is told to be.
	protected boolean isLying = false;
	
	/**
	 * A list with possible upstream business partners.
	 * I.e. this is a list with potential "sellers" at which the BuyingActor may satisfy his own demand.
	 */
	protected ArrayList<SellingActor> sellerList;
	
	private ArrayList<DeliveryAgent> deliveryAgents; // list of the delivery agents of the sellers
	
	public BuyingActor(ArrayList<SellingActor> sellerList, int incomingInventoryLevel, int outgoingInventoryLevel, Profile profile) 
	{	
		super(incomingInventoryLevel, outgoingInventoryLevel, profile);
		deliveryAgents = new ArrayList<DeliveryAgent>();
		// a list will be filled with all the delivery Agents of the sellers
		// because that is the contact where this buyer will order
		for (SellingActor singleSeller : sellerList)
		{
			deliveryAgents.add(singleSeller.getDeliveryAgent());
		}
		
		// must be in this order, because they need each other to be initialised!
		this.trustAgent = new TrustAgent(deliveryAgents, this.dimensionRatings, this);
		this.procurementAgent = new ProcurementAgent(deliveryAgents, trustAgent, profile);
		this.orderAgent = new OrderAgent(this, procurementAgent, deliveryAgents);	
		
	}
	
	/*
	 * GETTERS
	 */
	public TrustAgent getTrustAgent()
	{
		return trustAgent;
	}
	
	public ProcurementAgent getProcurementAgent()
	{
		return procurementAgent;
	}
	
	/*
	 * SETTERS
	 */
	
	public void setLying()
	{
		isLying = true;
	}
}