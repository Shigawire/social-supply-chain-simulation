package SimulationSetups;

import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.Wholesaler;

public class Setup2 extends SimSetup {
	
	public Setup2 (){
		super();
		manufacturerList.add(new Manufacturer(10, 200,200));
		manufacturerList.add(new Manufacturer(11, 150,150));
		distributorList.add(new Distributor(manufacturerList, 100,120, 55));
		distributorList.add(new Distributor(manufacturerList, 100,110, 50));
		distributorList.add(new Distributor(manufacturerList, 90,100, 56));
		wholesalerList.add(new Wholesaler(distributorList, 12, 11,40));
		retailerList.add(new Retailer(wholesalerList, 10, 10,10));
		retailerList.add(new Retailer(wholesalerList, 20, 10,11));
		retailerList.add(new Retailer(wholesalerList, 30, 10,10));
		customerList.add(new Customer(retailerList, 10,15));
	}

}
