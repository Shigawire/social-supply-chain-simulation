package agents;

import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import artefacts.ProductionBatch;

public class ProductionAgent 
{
	private int machineNumber;
	private int machineProductivity;
	private int productionQuantity;
	private int leadTime;
	private InventoryAgent inventoryAgent;
	private ArrayList<ProductionBatch> production;
	private ArrayList<ProductionBatch> toProduce;
	
	public ProductionAgent(int leadTime, int machines, InventoryAgent inventoryAgent)
	{
		this.inventoryAgent=inventoryAgent;
		machineNumber = machines;
		machineProductivity = 50;
		toProduce = new ArrayList<ProductionBatch>();
		production = new ArrayList<ProductionBatch>();
		this.leadTime = leadTime;
	}
	// 1. figures out how many machines to run, which defines the production quantity
	// 2. if enough rawmaterial is in the inventory, the production starts with 10% loss in 5% of the cases
	// 3. else the rawmaterial gets ordered
	public void produce(int nextDemand)
	{ 
		// 1.
		if (nextDemand % machineProductivity == 0) {
			if (nextDemand/machineProductivity <= machineNumber) {
				productionQuantity = (nextDemand / machineProductivity) * machineProductivity;
			}
			else productionQuantity = machineNumber * machineProductivity;
		} else {
			if (nextDemand / machineProductivity < machineNumber) {
				productionQuantity = ((nextDemand / machineProductivity) + 1) * machineProductivity;
			}
			else productionQuantity = machineNumber * machineProductivity;

		}
		
		// 2.
		if (inventoryAgent.getAInventoryLevel() >= productionQuantity * 2 && inventoryAgent.getBInventoryLevel() >= productionQuantity)  {
//			int dem = RandomHelper.nextIntFromTo(1, 20);
//			if (dem==1)productionQuantity=productionQuantity*90/100;
			ProductionBatch new_production_order = new ProductionBatch(this.leadTime, productionQuantity);
			production.add(new_production_order);
			inventoryAgent.reduceAInventoryLevel(productionQuantity * 2);
			inventoryAgent.reduceBInventoryLevel(productionQuantity);
		}
		//3.
		else {
			if (inventoryAgent.getAInventoryLevel() < productionQuantity * 2) {	
				deliverRawMaterialA(productionQuantity * 2);
			}
			if (inventoryAgent.getBInventoryLevel()<productionQuantity) {
				deliverRawMaterialB(productionQuantity);
			}
		}
	}
	
	// production method for Retailer and Distributor
	public void label()
	{
		productionQuantity = this.inventoryAgent.getIncomingInventoryLevel();
		ProductionBatch new_production_order = new ProductionBatch(this.leadTime, this.inventoryAgent.getIncomingInventoryLevel());
		production.add(new_production_order);
		inventoryAgent.reduceIncomingInventoryLevel(productionQuantity);
	}
	
	// production method for Wholesaler, produces products out of 2 materials from incoming_inventory
	public void process()
	{
		productionQuantity = this.inventoryAgent.getIncomingInventoryLevel() / 2;
		ProductionBatch new_production_order = new ProductionBatch(this.leadTime, this.inventoryAgent.getIncomingInventoryLevel());
		production.add(new_production_order);
		inventoryAgent.reduceIncomingInventoryLevel(productionQuantity * 2);
	}
	
	// put finally produced products into outgoing inventory
	public void harvest ()
	{
		for (ProductionBatch current_batch : production) 
		{
			current_batch.incrementTimeInProduction();
			if (current_batch.getTimeInProduction() >= this.leadTime) {
				// This batch is ready to be added to inventory
				this.inventoryAgent.increaseOutgoingInventoryLevel(current_batch.getQuantity());
			} else {
				toProduce.add(current_batch);
			}
		}
		
		production.clear();
		production.addAll(toProduce);
		toProduce.clear();
	}

	// delivers RawMaterial A with a failure-percentage
	public void deliverRawMaterialA(int amount)
	{
		RandomHelper.createNormal(97, 2);
		int var = RandomHelper.getNormal().nextInt();
		amount = amount * var / 100;
		inventoryAgent.increaseAInventoryLevel(amount);
	}
	
	//  delivers RawMaterial B with a failure-percentage
	public void deliverRawMaterialB(int amount)
	{
		RandomHelper.createNormal(97, 2);
		int var = RandomHelper.getNormal().nextInt();
		amount = amount * var / 100;
		inventoryAgent.increaseBInventoryLevel(amount);
	}
}