package simulation_implement;

import simulation_interface.ChainLink_between;
import simulation_interface.OrderAgent;

public class COrderAgent implements OrderAgent{

	@Override
	public void order(ChainLink_between sailor, double demand) {
		sailor.receiveOrder(demand);
	}

}
