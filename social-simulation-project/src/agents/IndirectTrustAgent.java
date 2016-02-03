package agents;

import java.util.ArrayList;

import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.Sale;
import actors.SupplyChainMember;
import actors.Wholesaler;
import artefacts.trust.Trust;

public class IndirectTrustAgent {
	//designpattern: Singleton
	//Lists with actors
	//have to be in context
	private ArrayList<Manufacturer> manufacturerList;
	private ArrayList<Distributor> distributorList;
	private ArrayList<Wholesaler> wholesalerList;
	private ArrayList<Retailer> retailerList;
	private  ArrayList<Customer> customerList;
	
	private static final IndirectTrustAgent indirectTrustAgent= new IndirectTrustAgent();
	private IndirectTrustAgent(){
		
	}
	public static void setLists(ArrayList<Manufacturer> manufacturerList,ArrayList<Distributor> distributorList,ArrayList<Wholesaler> wholesalerList,
			ArrayList<Retailer> retailerList, ArrayList<Customer> customerList){
		//listen geben
		indirectTrustAgent.setManufacturerList(manufacturerList);
		indirectTrustAgent.setDistributorList(distributorList);
		indirectTrustAgent.setRetailerList(retailerList);
		indirectTrustAgent.setWholesalerList(wholesalerList);
		indirectTrustAgent.setCustomerList(customerList);
	}
	//get the trustAgent and deliveryAgent which should be checked
	public static ArrayList<Trust> getTrustValue(TrustAgent trustAgent, DeliveryAgent deliveryAgent){
		//looking for the parent of the trustagent
		SupplyChainMember parent= trustAgent.getSupplyChainMember();
		ArrayList<Trust> trust_liste= new ArrayList<Trust>();
		//differnetiates from which class the 
		if(indirectTrustAgent.getCustomerList()!=null&&indirectTrustAgent.getCustomerList().get(0).getClass().equals(parent.getClass())){
			for (Customer customer : indirectTrustAgent.getCustomerList()){
				if(customer.getTrustAgent()!=trustAgent){
					trust_liste.add(customer.getTrustAgent().getTrustObject(deliveryAgent));
				}
			}
		}
		if(indirectTrustAgent.getDistributorList()!=null&&indirectTrustAgent.getDistributorList().get(0).getClass().equals(parent.getClass())){
			for (Distributor distributor : indirectTrustAgent.getDistributorList()){
				if(distributor.getTrustAgent()!=trustAgent){
					trust_liste.add(distributor.getTrustAgent().getTrustObject(deliveryAgent));
				}
			}
		}
		if(indirectTrustAgent.getRetailerList()!=null&&indirectTrustAgent.getRetailerList().get(0).getClass().equals(parent.getClass())){
			for (Retailer retailer : indirectTrustAgent.getRetailerList()){
				if(retailer.getTrustAgent()!=trustAgent){
					trust_liste.add(retailer.getTrustAgent().getTrustObject(deliveryAgent));
				}
			}
		}
		if(indirectTrustAgent.getCustomerList()!=null&&indirectTrustAgent.getWholesalerList().get(0).getClass().equals(parent.getClass())){
			for (Wholesaler wholesaler : indirectTrustAgent.getWholesalerList()){
				if(wholesaler.getTrustAgent()!=trustAgent){
					trust_liste.add(wholesaler.getTrustAgent().getTrustObject(deliveryAgent));
				}
			}
		}
		return trust_liste;
	}
	
	public static IndirectTrustAgent getIndirectTrustAgent(){
		return indirectTrustAgent;
	}
	
	//Getter and Setter
	public ArrayList<Manufacturer> getManufacturerList() {
		return manufacturerList;
	}
	public void setManufacturerList(ArrayList<Manufacturer> manufacturerList) {
		this.manufacturerList = manufacturerList;
	}
	public ArrayList<Distributor> getDistributorList() {
		return distributorList;
	}
	public void setDistributorList(ArrayList<Distributor> distributorList) {
		this.distributorList = distributorList;
	}
	public ArrayList<Wholesaler> getWholesalerList() {
		return wholesalerList;
	}
	public void setWholesalerList(ArrayList<Wholesaler> wholesalerList) {
		this.wholesalerList = wholesalerList;
	}
	public ArrayList<Retailer> getRetailerList() {
		return retailerList;
	}
	public void setRetailerList(ArrayList<Retailer> retailerList) {
		this.retailerList = retailerList;
	}
	public ArrayList<Customer> getCustomerList() {
		return customerList;
	}
	public void setCustomerList(ArrayList<Customer> customerList) {
		this.customerList = customerList;
	}

	
	
}
