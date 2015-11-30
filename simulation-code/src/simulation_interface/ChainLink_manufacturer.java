package simulation_interface;

public interface ChainLink_manufacturer extends ChainLink{
	//der Manufacturer muss order bekommen können
	public void receiveOrder(double realDemand);
}
