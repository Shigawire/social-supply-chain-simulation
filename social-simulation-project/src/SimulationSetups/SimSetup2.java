package SimulationSetups;

import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.Wholesaler;

public class SimSetup2 extends SimSetup 
{
	public SimSetup2 ()
	{
		super();
		trustIntegrated = true;
		indirectTrustIntegrated = true;
		informationSharingIntegrated = true;
		manufacturerList.add(new Manufacturer(10, 10, 200));
		manufacturerList.add(new Manufacturer(10, 10, 150));
		distributorList.add(new Distributor(manufacturerList, 10, 10, 55));
		distributorList.add(new Distributor(manufacturerList, 10, 10, 50));
		//distributorList.add(new Distributor(manufacturerList, 10, 10, 56));
		wholesalerList.add(new Wholesaler(distributorList, 10, 10, 40));
		retailerList.add(new Retailer(wholesalerList, 10, 10, 10));
		//retailerList.add(new Retailer(wholesalerList, 10, 10, 11));
		retailerList.add(new Retailer(wholesalerList, 10, 10, 10));
		customerList.add(new Customer(retailerList, 0, 0));
	}
}