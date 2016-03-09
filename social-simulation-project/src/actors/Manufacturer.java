package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import repast.simphony.engine.schedule.ScheduledMethod;
import SimulationSetups.TrustSetter;
import agents.DeliveryAgent;
import agents.OrderAgent;
import agents.ProductionAgent;
import artefacts.ProductionBatch;
import artefacts.Profile;

/**
* This class represents the manufacturer. The manufacturer
* does not order, but produces. He has no trust agent (?).
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class Manufacturer extends SupplyChainMember implements SellingActor
{
	private int subtractionByTrust = 0; // for the subtraction from the order caused by knowing he will not order at me

	private int nextDemand;

	//how many machines are available for production
	private int machineQuantity;

	private DeliveryAgent deliveryAgent;
	private ProductionAgent productionAgent;

	private Map<OrderAgent, Integer> buyer = new HashMap<OrderAgent, Integer>();

	private int lastOrderUpToLevel = -1;

	private int lastDemand = 0;

	//the time needed to for production
	private int leadTime = 2;

	protected int currentOutgoingInventoryLevel;

	private int desiredInventoryLevel;

	private int productionQuantity;

	public Manufacturer(int currentIncomingInventoryLevel, int currentOutgoingInventoryLevel, int price, Profile profile)
	{
		super(currentIncomingInventoryLevel, currentOutgoingInventoryLevel, profile);
		this.deliveryAgent = new DeliveryAgent(price, this, 10, 5);
		this.machineQuantity = 10;
		this.productionAgent = new ProductionAgent(this.leadTime, this.machineQuantity, this.inventoryAgent, this);
	}

	// method for every run, start: start tick, priority: which priority it has in the simulation(higher --> better priority)
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void run()
	{
		//Transfer items from the incoming inventory (production process) to the outgoing inventory
		this.productionAgent.transferInventories();

		// 2. deliver() all previously produced goods that can be delivered to customers
		deliver();

		//calculate the demand for the next production phase
		calculateNextDemand();

		//enqueue new production process
		produce();

	}

	/**
	   * This method receives goods at the beginning of each tick
	   *
	   * @return Nothing.
	   */
	public void receiveShipments()
	{
		//basically never happens as the manufacturer does not order at another supply chain member
	}

	private void calculateNextDemand()
	{
		// ask the forecast Agent about the next demand
		nextDemand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
	}

	public void deliver()
	{
		// delegate delivery of produced goods the delivery Agent
		deliveryAgent.deliver(this.inventoryAgent);
	}

	// if a possible buyer trust this actor enough, but will not order at him, he will tell it
	public void going2order(OrderAgent noOrderer)
	{
		if (buyer.containsKey(noOrderer)) {
			subtractionByTrust += buyer.get(noOrderer);
		}
	}

	// the list of what amount which orderAgent ordered is updated with every order
	public void updateClientList(OrderAgent orderer, int orderAtYou)
	{
		// when the buyer is not already in the map
		if (!buyer.containsKey(orderer)) {
			buyer.put(orderer, orderAtYou);
		}

		// the value is changed by the value he ordered this time
		int newValue = (buyer.get(orderer) + orderAtYou) / 2;
		// because of RePast the new value has to be put int the map this way
		buyer.remove(orderer);
		buyer.put(orderer, newValue);
	}




	  /*
    public void produce()
    {
        // 1. need in the next tick
        // 2. whats about my inventory
        // 3. order difference: +shortage-the value I do not need because of information sharing

        // 1.
        nextDemand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());
        lastOrderUpToLevel = (lastOrderUpToLevel != -1) ? nextDemand : lastOrderUpToLevel;
        int orderUpToLevel = lastOrderUpToLevel + 1*(nextDemand - lastDemand);

        desiredInventoryLevel = orderUpToLevel;
        lastDemand = nextDemand;
        lastOrderUpToLevel = orderUpToLevel;
        // 2.

        currentOutgoingInventoryLevel = this.inventoryAgent.getOutgoingInventoryLevel();

        //if current bigger than desiredlevel return
        if(currentOutgoingInventoryLevel > desiredInventoryLevel){

            return;
        }

        // 3.
        TrustSetter s = TrustSetter.getInstance();




        if(s.getInformationSharingIntegrated()) {
            productionQuantity = nextDemand - currentOutgoingInventoryLevel+ deliveryAgent.getShortage()-subtractionByTrust;
        }
        else
        {
        	productionQuantity = nextDemand - currentOutgoingInventoryLevel+ deliveryAgent.getShortage();
        }



        subtractionByTrust=0;

        // If the inventory level is sufficient for the next demand,
        // do not order
        if (productionQuantity < 0)
        {
            //a order with quantity null has to be made for the process in the orderAgent
            // (realize the order of the last tick)
        	productionQuantity = 0;

        }

            this.productionAgent.produce(productionQuantity);
    }
    */



	public void produce()
	{
		//calculate the desired inventory level (have a look at the documentation for mathematical explanation)
		nextDemand = this.forecastAgent.calculateDemand(this.deliveryAgent.getAllOrders());

		lastOrderUpToLevel = (lastOrderUpToLevel != -1) ? nextDemand : lastOrderUpToLevel;
        int orderUpToLevel = lastOrderUpToLevel + 1*(nextDemand - lastDemand);

        desiredInventoryLevel = orderUpToLevel;
        lastDemand = nextDemand;
        lastOrderUpToLevel = orderUpToLevel;

		currentOutgoingInventoryLevel = this.inventoryAgent.getOutgoingInventoryLevel();

		//if the current outgoing inventory level is larger than the desired inventory level return
		if(currentOutgoingInventoryLevel > orderUpToLevel){

			return;
		}

		//get the global trust instance responsible for information sharing. Let's call it "trust oracle"
		TrustSetter trustOracle = TrustSetter.getInstance();

		int productionQuantity = 0;

		//if information sharing is enabled incorporate it for a more accurate calculation of the production quantity
		if(trustOracle.getInformationSharingIntegrated()) {
			productionQuantity = nextDemand - currentOutgoingInventoryLevel+ deliveryAgent.getShortage()-subtractionByTrust;
		}
		else
		{
			productionQuantity = nextDemand - currentOutgoingInventoryLevel+ deliveryAgent.getShortage();
		}

		//TODO what's that?
		subtractionByTrust = 0;

		// If the inventory level is sufficient for the next demand,
		// do not produce
		if (productionQuantity < 0)
		{
			//a production with quantity null has to kicked off for the correct functioning of the simulation
			productionQuantity = 0;

		}

		this.productionAgent.produce(productionQuantity);
	}




	public DeliveryAgent getDeliveryAgent()
	{
		return this.deliveryAgent;
	}
}
