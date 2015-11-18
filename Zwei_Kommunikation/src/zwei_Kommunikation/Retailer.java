package zwei_Kommunikation;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Retailer {
	private int bestellung;
	private Customer meinKunde;
	private int lager=30;
	private int untergrenze=20;
	private int bestellungsAnzahl=1;
	public Retailer(){
	}
	public void bestellung(int bestellung, Customer cus) {
		meinKunde = cus;
		this.bestellung=bestellung;
	}
	@ScheduledMethod(start=2,interval=1,priority=1)
	public void pruefeAuftraege(){
		
		if(bestellung!=0){
			untergrenze= (((untergrenze/2*bestellungsAnzahl)+bestellung)/bestellungsAnzahl)*2;
			bestellungsAnzahl++;
			if(lager>=bestellung){
					lager=lager-bestellung;
					auftragAusfuehren();
			}
				
		}
		
		
		//System.out.println(untergrenze);
	}
	public void auftragAusfuehren(){
		meinKunde.liefern(bestellung);
		bestellung=0;
	}
	@ScheduledMethod(start=2,interval=1,priority=2)
	public void lagerAuffüllen(){
		if(untergrenze>=lager){
			lager=lager+untergrenze;
		}
	}
	public int getLager(){
		return lager;
	}
	
}
