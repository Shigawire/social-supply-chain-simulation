package agents;

import java.util.ArrayList;

import repast.simphony.random.RandomHelper;
import social_simulation_project.OrderObserver;
import actors.SellingActor;
import actors.SupplyChainMember;
import artefacts.Order;

/**
* This class represents a delivery agent. They are 
* responsible for delivery (only retailers, wholesalers,
* distributors and the manufacturer). 
* Delivery agents communicate with
* - the user's inventory agent
* - the orderers's order agent

*/
public class DeliveryAgent 
{
	// price for the offered goods
	private int price;
	
	private int currentOutgoingInventoryLevel;
	
	// list for all received orders
	// this list is filled at the beginning of each tick and subsequently processed - basically this is the "inbox".
	private ArrayList<Order> receivedOrders; 
	
	// all orders ever received - not the same as receivedOrders!
	private ArrayList<Order> everReceivedOrders; 
	
	// list to transfer open orders
	private ArrayList<Order> openOrders; 
	
	 // gives the shortage of the last tick, will be used for the forecast
	private int shortage = 0;
	
	// to which SupplyChainMember it belongs
	private SupplyChainMember parent; 
	
	//the mean for a failing shipment
	private double failureMean;
	
	//the deviation for the failing shipment
	private double failureDeviation;
	
	//How many items are necessary for a shipment? 
	//Just if the overall order quantity is larger - obviously
	//Basically we just don't want to ship out a single item if there's partial delivery outstanding
	private int minimumShipmentQuantity = 8;
	
	public DeliveryAgent(int price, SupplyChainMember parent, int mean, int deviation) 
	{
		this.receivedOrders = new ArrayList<Order>();
		this.everReceivedOrders = new ArrayList<Order>();
		this.openOrders = new ArrayList<Order>();
		this.price = price;
		this.parent = parent;
		this.failureMean = mean;
		this.failureDeviation = deviation;
	}
	
	/**
	   * This method receives orders.
	   * 
	   * @param order Order of the orderer with details about amount etc.
	   */
	public void receiveOrder(Order order) 
	{
		//assign myself to the received order as the "handling" delivery agent
		order.setDeliveryAgent(this);
		
		//push the order to the receivedOrders List
		receivedOrders.add(order);
		
		//and put at record in our eternal history
		everReceivedOrders.add(order);
		
		//push the order to the supply chain members client list
		parent.updateClientList(order.getOrderAgent(), order.getQuantity());
	}
	
	/**
	   * This method receives goods at the beginning of each tick
	   * 
	   * @param inventoryAgent inventory agent used to get information 
	   * 	    about the inventory level of the current deliverer
	   */
	public void deliver(InventoryAgent inventoryAgent)
	{
		currentOutgoingInventoryLevel = inventoryAgent.getOutgoingInventoryLevel();
		shortage = 0;
		for (Order order : receivedOrders) 
		{	
			// if the order is already processed, it will just disapper (when set e.g. by the
			// inventory Agent after cancellation
			if (order.getProcessed()) {
				
			}
			// if the needed rest quantity of the order is higher then the inventory and the need is bigger than the mimimumShipmentQuantity (8) (he will not deliver just 7 goods)
			
			else if (order.getUnfullfilledQuantity() > currentOutgoingInventoryLevel && currentOutgoingInventoryLevel > minimumShipmentQuantity) {
				// if the needed rest quantity of the order is higher then the inventory
				// part of the order will be delivered
				
				order.fulfill(currentOutgoingInventoryLevel);
				
				//calculate a failure percentage for the specific order - i.e. how many items are DOA
				order.setfailurePercentage((failureMean + RandomHelper.nextDoubleFromTo(+failureDeviation, -failureDeviation))/100);
				
				openOrders.add(order);
				
				// shortage will be increased by the higher need
				shortage =+ order.getUnfullfilledQuantity();
				
				// what was delivered will be taken out of the inventory
				inventoryAgent.reduceOutgoingInventoryLevel(currentOutgoingInventoryLevel);
				
				//push the order as shipment to the inbox of the customers order Agent
				order.getOrderAgent().receiveShipment(order,this);
				
				//set the inventory to 0 as we could not fulfill the order entire.
				currentOutgoingInventoryLevel = 0;
			}
			// if <= MimumShipmentQuantity (8) no delivery will be made
			else if(order.getUnfullfilledQuantity() > currentOutgoingInventoryLevel) {
				shortage =+ order.getUnfullfilledQuantity();
				openOrders.add(order);
			}
			// if the order can be completely fullfilled, it will be
			else {
				
				// just a buffer of the unfullfilled
				int buffer = order.getUnfullfilledQuantity();
				
				//mark the order as fully processed
				order.setProcessed(true);
				
				//fulfill the order completely
				order.fulfill(buffer);
				
				// sub the amount because the order is not open anymore
				order.getOrderAgent().receiveShipment(order,this);
				
				//reduce outgoing inventory as the order has been shipped
				inventoryAgent.reduceOutgoingInventoryLevel(buffer);
				
				currentOutgoingInventoryLevel = inventoryAgent.getOutgoingInventoryLevel();
			}
		}
		// the received list must be deleted completely and filled with the openOrder list
		// otherwise RePast has a problem
		receivedOrders.clear();
		receivedOrders.addAll(openOrders);
		openOrders.clear();
	}


	/*
	 * GETTERS
	 */
	public int getPrice() 
	{	
		return this.price;
	}

	public int getShortage() 
	{
		// gives half of the shortage
		return (shortage / 2);
	}
	
	public ArrayList<Order> getAllOrders()
	{	
		return this.everReceivedOrders;
	}
	
	public double getExpectedDeliveryTime() 
	{
		return 2;
	}
	
	public SupplyChainMember getParent() 
	{
		return this.parent;
	}

	/*
	 * SETTERS
	 */
	public void setShortage(int i) 
	{
		shortage = 0;	
	}
}