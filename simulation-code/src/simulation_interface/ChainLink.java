package simulation_interface;

public interface ChainLink {
	//jedes Kettenglied hat einen gewissen Bedarf
	public double getDemand();
	public void setDemand(double newDemand);
}
