package SimulationSetups;

import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.Wholesaler;
import artefacts.Profile;

public class SimSetup2 extends SimSetup 
{
	public SimSetup2 ()
	{
		
		super();
		Profile a=new Profile(0.44,0.415,0.145,0.22,0.31,0.31,0.16,0.67,0.53,0.35);
		trustIntegrated = true;
		indirectTrustIntegrated = true;
		informationSharingIntegrated = false;
		manufacturerList.add(new Manufacturer(25, 50, 200,a));
		//manufacturerList.add(new Manufacturer(30, 30, 150));
		distributorList.add(new Distributor(manufacturerList, 20, 40, 55,a));
		//distributorList.add(new Distributor(manufacturerList, 30, 30, 50));
		//distributorList.add(new Distributor(manufacturerList, 30, 30, 56));
		wholesalerList.add(new Wholesaler(distributorList, 15, 30, 40,a));
		retailerList.add(new Retailer(wholesalerList, 10, 20, 10,a));
		//retailerList.add(new Retailer(wholesalerList, 30, 30, 11));
		//retailerList.add(new Retailer(wholesalerList, 30, 30, 10,a));
		customerList.add(new Customer(retailerList, 0, 0,a));
	}
}