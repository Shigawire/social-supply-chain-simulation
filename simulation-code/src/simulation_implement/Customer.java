package simulation_implement;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import simulation_interface.ChainLink_customer;


public class Customer implements Echelon{
	CInventoryAgent inventoryAgent;
	OrderAgent orderAgent;
	TrustAgent trustAgent;
	ForecastAgent forecastAgent;
	Retailer meinRetailer;
	double demand;
	public Customer(Retailer retailer, double realDemand,Retailer erster, Retailer zweiter){
		meinRetailer=retailer;
		inventoryAgent=new CInventoryAgent();
		orderAgent= new OrderAgent();
		trustAgent=new TrustAgent(erster, zweiter);
		forecastAgent=new ForecastAgent();
		demand=realDemand;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		demand=forecastAgent.calculateDemand();
		demand=inventoryAgent.checkInventory(demand);
		meinRetailer=(Retailer) trustAgent.chooseSailor();
		orderAgent.order(this,meinRetailer, demand);
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
	public void receiveShipment(double shipment) {
		// recieving stoesst weitere Prozess an
		inventoryAgent.store(shipment);
		trustAgent.updateTrust(RandomHelper.nextIntFromTo(4, 20));
		
	}

}
