package simulation_interface;

public interface Echelon {
	//jedes Kettenglied hat einen gewissen Bedarf
	public double getDemand();
	public void setDemand(double demand);
	public void receiveOrder(Echelon orderingEchelon, double demand);
	public void sendOrder(Echelon toEchelon, double demand);
}