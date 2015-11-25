package simulation_interface;
//interface for a normal chain link, which lays between two others
public interface ChainLink_between extends ChainLink_customer{
	//Jeder der inmitten der Kette sitzt muss sowohl Ware empfangen koennen, sowie Order bekommen
	public void receiveOrder(ChainLink_customer chain,double realDemand);
}
