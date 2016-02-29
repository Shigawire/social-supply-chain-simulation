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
		Profile b=new Profile(0.63,0.225,0.145,0.1,0.455,0.305,0.14,0.7125,0.45,0.2625);
		Profile c=new Profile(0.34,0.34,0.32,0.25,0.31,0.31,0.13,0.625,0.585,0.42);
		trustIntegrated = true;
		indirectTrustIntegrated = true;
		informationSharingIntegrated = true;
		
		manufacturerList.add(new Manufacturer(20, 20, 9,a));
		manufacturerList.add(new Manufacturer(20, 20, 11,a));
		manufacturerList.add(new Manufacturer(20, 20, 10,a));
		
		Distributor d1=new Distributor(manufacturerList, 20, 20, 8,c);
		d1.setLying();
		distributorList.add(new Distributor(manufacturerList, 20, 20, 11,a));
		distributorList.add(new Distributor(manufacturerList, 20, 20, 10,a));
		distributorList.add(new Distributor(manufacturerList, 20, 20, 10,b));
		distributorList.add(d1);
		
		Wholesaler w1=new Wholesaler(distributorList, 15, 15, 10,b);
		w1.setLying();
		wholesalerList.add(new Wholesaler(distributorList,15, 15, 10,a));
		wholesalerList.add(new Wholesaler(distributorList,15, 15, 10,c));
		wholesalerList.add(w1);
		
		
		retailerList.add(new Retailer(wholesalerList, 10, 10, 10,a));
		Retailer r1=new Retailer(wholesalerList, 10, 10, 11,a);
		r1.setLying();
		retailerList.add(r1);
		retailerList.add(new Retailer(wholesalerList, 10, 10, 11,b));
		retailerList.add(new Retailer(wholesalerList, 10, 10, 11,a));
		retailerList.add(new Retailer(wholesalerList, 10, 10, 10,c));
		
		Customer c1=new Customer(retailerList, 0, 0,a);
		Customer c2=new Customer(retailerList, 0, 0,b);
		c2.setLying();
		customerList.add(c1);
		customerList.add(c2);
		customerList.add(new Customer(retailerList, 0, 0,c));
	}
}