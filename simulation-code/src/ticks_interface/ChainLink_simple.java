package ticks_interface;
//interface for the simpliest chain link(e.g. customer)s
public interface ChainLink_simple {
	public double getDemand();
	public void setDemand(double newDemand);
	public void receiveShipment(double shipment);
}
