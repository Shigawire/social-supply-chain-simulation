package zwei_Kommunikation;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;

public class Customer {
	private Retailer meinRetailer;
	private int lager=0;
	private int order=10;
	int untergrenze=20;
	int verbrauch=5;
	int woche=1;
	public Customer(){
	}
	@ScheduledMethod(start=2,interval=1)
	public void bestellungAbgeben(){
		if(lager<untergrenze){
			meinRetailer.bestellung(order,this);
		}
	}

	public void liefern(int bestellung) {
		lager=lager+bestellung;
		
		
	}
	@ScheduledMethod(start=1,interval=1)
	public void verbrauch(){
		verbrauch = RandomHelper.nextIntFromTo(4, 20);
		if(verbrauch<lager){
			lager=lager-verbrauch;
		}
		else{
			lager=0;
			untergrenze=untergrenze+2;
			order++;
		}
		
	}
	
	
	public Retailer getMeinRetailer() {
		return meinRetailer;
	}
	public void setMeinRetailer(Retailer meinRetailer) {
		this.meinRetailer = meinRetailer;
	}
	public int getLager(){
		return lager;
	}
	public void setLager(int l){
		lager=l;
	}
}
