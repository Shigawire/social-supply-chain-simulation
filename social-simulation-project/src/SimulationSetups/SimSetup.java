package SimulationSetups;

import java.util.ArrayList;
import java.util.Collection;

import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.SellingActor;
import actors.SupplyChainMember;
import actors.Wholesaler;

public abstract class SimSetup 
{
	protected ArrayList<SellingActor> manufacturerList;
	protected ArrayList<SellingActor> distributorList;
	protected ArrayList<SellingActor> wholesalerList;
	protected ArrayList<SellingActor> retailerList;
	protected ArrayList<Customer> customerList;
	protected boolean trustIntegrated;
	protected boolean indirectTrustIntegrated;
	protected boolean informationSharingIntegrated;
	
	public SimSetup()
	{
		manufacturerList = new ArrayList<SellingActor>();
		distributorList = new ArrayList<SellingActor>();
		wholesalerList = new ArrayList<SellingActor>();
		retailerList = new ArrayList<SellingActor>();
		customerList = new ArrayList<Customer>();
	}

	/*
	 * GETTERS
	 */
	public ArrayList<Customer> getCustomerList()
	{
		return this.customerList;
	}
	
	public ArrayList<SellingActor> getManufacturerList()
	{
		return this.manufacturerList;
	}
	
	public ArrayList<SellingActor> getDistributorList()
	{
		return this.distributorList;
	}
	
	public ArrayList<SellingActor> getWholesalerList()
	{
		return this.wholesalerList;
	}
	
	public ArrayList<SellingActor> getRetailerList()
	{
		return this.retailerList;
	}
	
	public boolean getTrustIntegrated()
	{
		return this.trustIntegrated;
	}
	
	public boolean getIndirectTrustIntegrated()
	{
		return this.indirectTrustIntegrated;
	}
	
	public boolean getInformationSharingIntegrated()
	{
		return this.informationSharingIntegrated;
	}
}