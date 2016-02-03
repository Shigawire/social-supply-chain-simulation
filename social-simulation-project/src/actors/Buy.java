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
		for (Sale sailor1 : sailor)
		{
			delivery_agents.add(sailor1.getDeliveryAgent());
		}
		trustAgent = new TrustAgent(delivery_agents, this.dimensionRatings, this);
		this.procurementAgent=new ProcurementAgent(delivery_agents, trustAgent);
		this.orderAgent = new OrderAgent(this, procurementAgent,delivery_agents);	
		
	}
	public TrustAgent getTrustAgent(){
		return trustAgent;
	}
	public ProcurementAgent getProcurementAgent(){
		return procurementAgent;
	}
	public void setLying(){
		lying=true;
	}
}






