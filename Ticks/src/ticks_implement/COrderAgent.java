package ticks_implement;

import ticks_interface.ChainLink_between;
import ticks_interface.OrderAgent;

public class COrderAgent implements OrderAgent{

	@Override
	public void order(ChainLink_between sailor, double demand) {
		sailor.receiveOrder(demand);
	}

}
