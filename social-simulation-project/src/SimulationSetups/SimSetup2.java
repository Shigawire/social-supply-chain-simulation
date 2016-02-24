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
		informationSharingIntegrated = false;
		manufacturerList.add(new Manufacturer(0, 0, 200));
		manufacturerList.add(new Manufacturer(0, 0, 150));
		distributorList.add(new Distributor(manufacturerList, 0, 0, 55));
		distributorList.add(new Distributor(manufacturerList, 0, 0, 50));
		distributorList.add(new Distributor(manufacturerList, 0, 0, 56));
		wholesalerList.add(new Wholesaler(distributorList, 0, 0, 40));
		retailerList.add(new Retailer(wholesalerList, 0, 0, 10));
		retailerList.add(new Retailer(wholesalerList, 0, 0, 11));
		retailerList.add(new Retailer(wholesalerList, 0, 0, 10));
		customerList.add(new Customer(retailerList, 0, 0));
	}
}