package actors;

import agents.OrderAgent;
import agents.ProcurementAgent;

public abstract class Buy extends SupplyChainMember
{
	protected OrderAgent orderAgent;
	protected ProcurementAgent procurementAgent;
	
	public Buy(int incoming_inventory_level, int outgoing_inventory_level) 
	{
		super(incoming_inventory_level, outgoing_inventory_level);
	}
}