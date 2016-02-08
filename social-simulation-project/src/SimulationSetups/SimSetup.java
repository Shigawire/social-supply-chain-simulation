package SimulationSetups;

import java.util.ArrayList;
import java.util.Collection;

import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.Sale;
import actors.SupplyChainMember;
import actors.Wholesaler;

public abstract class SimSetup {
	protected ArrayList<Sale> manufacturerList;
	protected ArrayList<Sale> distributorList;
	protected ArrayList<Sale> wholesalerList;
	protected ArrayList<Sale> retailerList;
	protected ArrayList<Customer> customerList;
	
	
	public SimSetup(){
		manufacturerList = new ArrayList<Sale>();
		distributorList = new ArrayList<Sale>();
		wholesalerList = new ArrayList<Sale>();
		retailerList = new ArrayList<Sale>();
		customerList = new ArrayList<Customer>();
	}


	public ArrayList<Customer> getCustomerList(){
		return this.customerList;
	}
	public ArrayList<Sale> getManufacturerList(){
		return this.manufacturerList;
	}
	public ArrayList<Sale> getDistributorList(){
		return this.distributorList;
	}
	public ArrayList<Sale> getWholesalerList(){
		return this.wholesalerList;
	}
	public ArrayList<Sale> getRetailerList(){
		return this.retailerList;
	}


}
