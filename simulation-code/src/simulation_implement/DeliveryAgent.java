package simulation_implement;

import simulation_interface.ChainLink_customer;
import simulation_interface.DeliveryAgenti;

public class DeliveryAgent implements DeliveryAgenti{
	//Da ChainLink_between ChainLink_customer erweitert reicht ChainLink_Customer
	//Obacht falls sich da noch was aendern sollte!
	@Override
	public void deliver(double shipment, ChainLink_customer receiver) {
		receiver.receiveShipment(shipment);
	}

}
