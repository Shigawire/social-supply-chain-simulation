package actors;

import java.util.ArrayList;

import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProcurementAgent;
import agents.TrustAgent;

public abstract class Buy extends SupplyChainMember
{
	protected OrderAgent orderAgent;
	protected ProcurementAgent procurementAgent;
	protected TrustAgent trustAgent;
	protected ArrayList<Sale> sailor_list;
	private ArrayList<DeliveryAgent> delivery_agents;
	public Buy( ArrayList<Sale> sailor,int inventory_level) 
	{	
		super(inventory_level);
		for (Sale sailor1 : sailor)
		{
			delivery_agents.add(sailor1.getDeliveryAgent());
		}
		trustAgent = new TrustAgent(delivery_agents,this);
		
	}
	public TrustAgent getTrustAgent(){
		return trustAgent;
	}
}