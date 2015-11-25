package simulation_implement;

import simulation_interface.ChainLink;
import simulation_interface.ChainLink_between;
import simulation_interface.ChainLink_customer;

public class Retailer implements ChainLink_between{
	
	private RInventoryAgent inventoryAgent;
	private OrderAgent orderAgent;
	private TrustAgent trustAgent;
	private ForecastAgent forecastAgent;
	private DeliveryAgent deliveryAgent;
	private ChainLink_customer meinKunde;
	private double demand =0;
	private double shortage=0;
	//muss natuerlich eigentlich auch noch seinen sailor also wer ihn beliefert bekommen!
	public Retailer(double realDemand){
		inventoryAgent= new RInventoryAgent();
		orderAgent= new OrderAgent();
		trustAgent= new TrustAgent(null,null);//erstmall null, null muss bei der Kette verändert werden also ähnlich wie beim Customer
		forecastAgent=new ForecastAgent();
		deliveryAgent=new DeliveryAgent();
		demand=realDemand;
	}
	
	//Was macht der Agent jeden Tick?
	public void step(){
		shortage=inventoryAgent.checkInventory(demand);
		
		if(shortage==0){
			deliveryAgent.deliver(demand, meinKunde);
		}
		//der teil erstmal nicht da noch kein weiteres kettenglied
		inventoryAgent.store(demand+shortage);
		/*demand=forecastAgent.calculateDemand();
		demand=inventoryAgent.checkInventory(demand);
		meinRetailer=(Retailer) trustAgent.chooseSailor();
		orderAgent.order(meinRetailer, demand);*/
	}
	@Override
	public double getDemand() {
		// TODO Auto-generated method stub
		return demand;
	}

	@Override
	public void setDemand(double newDemand) {
		demand=newDemand;
		
	}

	@Override
	public void receiveOrder(ChainLink_customer chain,double realDemand) {
		//welcher Kunde mit welcher Betsellung wahrscheinlich am besten in eine Liste
		meinKunde= chain;
		demand=realDemand;
	}
	@Override
	public void receiveShipment(double shipment) {
		// siehe Customer sobald weitere Chain
		
	}

}
