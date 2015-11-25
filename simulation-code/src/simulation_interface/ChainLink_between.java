package simulation_interface;
//interface for a normal chain link, which lays between two others
public interface ChainLink_between extends ChainLink_simple{
	public void receiveOrder(double realDemand);
}
