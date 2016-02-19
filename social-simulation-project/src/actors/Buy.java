package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
import agents.TrustAgent;
import artefacts.trust.Trust;
//class for all those who buy
public abstract class Buy extends SupplyChainMember
{
	protected OrderAgent orderAgent;
	protected ProcurementAgent procurementAgent;
	protected TrustAgent trustAgent;
	//boolean if he is a "lying agent"
	protected boolean lying=false;
	protected ArrayList<Sale> sailor_list;//list with possible salers
	private ArrayList<DeliveryAgent> delivery_agents;//list of the delivery agents of the salers
	public Buy( ArrayList<Sale> sailor,int incoming_inventory_level, int outgoing_inventory_level) 
	{	
		super(incoming_inventory_level, outgoing_inventory_level);
		delivery_agents=new ArrayList<DeliveryAgent>();
		//a list will be filled with all the delivery Agents of the sellers
		//because that is the contact where this buyer will order
		for (Sale sailor1 : sailor)
		{
			delivery_agents.add(sailor1.getDeliveryAgent());
		}
		//must be in this order, because they need each other to be initialised!
		this.trustAgent = new TrustAgent(delivery_agents, this.dimensionRatings, this);
		this.procurementAgent=new ProcurementAgent(delivery_agents, trustAgent);
		this.orderAgent = new OrderAgent(this, procurementAgent,delivery_agents);	
		
	}
	//getters
	public TrustAgent getTrustAgent(){
		return trustAgent;
	}
	public ProcurementAgent getProcurementAgent(){
		return procurementAgent;
	}
	//setters
	public void setLying(){
		lying=true;
	}
}






