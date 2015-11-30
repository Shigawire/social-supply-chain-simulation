package simulation_implement;

import simulation_interface.ChainLink;
import simulation_interface.ChainLink_between;
import simulation_interface.ChainLink_customer;
import simulation_interface.OrderAgenti;

public class OrderAgent implements OrderAgenti{
	// muss noch geändert werden sodass auch auch Manufacturer möglicher sailor
	//eventuell mit Dekorierer
	@Override
	public void order(ChainLink_customer ich,ChainLink_between sailor, double demand) {
		sailor.receiveOrder(ich,demand);
	}

}
