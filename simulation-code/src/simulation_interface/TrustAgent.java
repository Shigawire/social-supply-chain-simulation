package simulation_interface;

public interface TrustAgent {
	//method for choose where to buy from
	public ChainLink_simple chooseSailor();
	public void updateTrust(int someValue);
	
}
