package social_simulation_project;

import agents.OrderAgent;
import agents.ProcurementAgent;

public abstract class Buy extends SupplyChainMember{
	protected OrderAgent orderAgent;
	protected ProcurementAgent procurementAgent;
	public Buy(int inventory_level) {
		super(inventory_level);
		// TODO Auto-generated constructor stub
	}

}
