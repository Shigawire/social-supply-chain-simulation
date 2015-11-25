package ticks_implement;

import ticks_interface.ChainLink_simple;
import ticks_interface.DeliveryAgent;

public class RDeliveryAgent implements DeliveryAgent{

	@Override
	public void deliver(double shipment, ChainLink_simple receiver) {
		receiver.receiveShipment(shipment);
	}

}
