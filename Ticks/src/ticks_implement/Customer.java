package ticks_implement;

import repast.simphony.engine.schedule.ScheduledMethod;
import ticks_interface.ChainLink_simple;


public class Customer implements ChainLink_simple{
	CInventoryAgent inventoryAgent;
	COrderAgent orderAgent;
	CTrustAgent trustAgent;
	CForecastAgent forecastAgent;
	Retailer meinRetailer;
	double demand;
	public Customer(CInventoryAgent a,COrderAgent b, CTrustAgent c,CForecastAgent d,double realDemand){
		inventoryAgent=a;
		orderAgent=b;
		trustAgent=c;
		forecastAgent=d;
		demand=realDemand;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		demand=forecastAgent.calculateDemand();
		demand=inventoryAgent.checkInventory(demand);
		meinRetailer=(Retailer) trustAgent.chooseSailor();
		orderAgent.order(meinRetailer, demand);
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
		// TODO Auto-generated method stub
		
	}

}
