package ticks_implement;

import ticks_interface.ChainLink_between;

public class Retailer implements ChainLink_between{
	
	private RInventoryAgent inventoryAgent;
	private ROrderAgent orderAgent;
	private RTrustAgent trustAgent;
	private RForecastAgent forecastAgent;
	private RDeliveryAgent deliveryAgent;
	private double demand =0;
	public Retailer(RInventoryAgent a,ROrderAgent b, RTrustAgent c,RForecastAgent d, RDeliveryAgent e, double realDemand){
		inventoryAgent=a;
		orderAgent=b;
		trustAgent=c;
		forecastAgent=d;
		deliveryAgent=e;
		demand=realDemand;
	}
	public void step(){
		
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
	public void receiveOrder(double realDemand) {
		demand=realDemand;
	}
	@Override
	public void receiveShipment(double shipment) {
		// TODO Auto-generated method stub
		
	}

}
