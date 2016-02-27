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

// class for all those who buy
public abstract class Buy extends SupplyChainMember
{
	protected OrderAgent orderAgent;
	protected ProcurementAgent procurementAgent;
	protected TrustAgent trustAgent;
	protected boolean lying = false; // boolean if he is a "lying agent"
	protected ArrayList<Sale> sailorList; // list with possible salers
	private ArrayList<DeliveryAgent> deliveryAgents; // list of the delivery agents of the salers
	
	public Buy(ArrayList<Sale> sailor, int incomingInventoryLevel, int outgoingInventoryLevel, Profile p) 
	{	
		super(incomingInventoryLevel, outgoingInventoryLevel, p);
		deliveryAgents = new ArrayList<DeliveryAgent>();
		// a list will be filled with all the delivery Agents of the sellers
		// because that is the contact where this buyer will order
		for (Sale sailor1 : sailor)
		{
			deliveryAgents.add(sailor1.getDeliveryAgent());
		}
		// must be in this order, because they need each other to be initialised!
		this.trustAgent = new TrustAgent(deliveryAgents, this.dimensionRatings, this);
		this.procurementAgent = new ProcurementAgent(deliveryAgents, trustAgent,myProfile);
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
		lying = true;
	}
}