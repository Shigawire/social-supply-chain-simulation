package simulation_interface;
//interface for the simpliest chain link(e.g. customer)s
public interface ChainLink_customer extends ChainLink{
	//jeder Customer muss Ware empfangen k�nnen
	public void receiveShipment(double shipment);
}
