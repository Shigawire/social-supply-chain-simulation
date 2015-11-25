package simulation_implement;

import simulation_interface.ChainLink_simple;
import simulation_interface.DeliveryAgent;

public class DeliveryAgent implements DeliveryAgent{

	@Override
	public void deliver(double shipment, ChainLink_simple receiver) {
		receiver.receiveShipment(shipment);
	}

}
