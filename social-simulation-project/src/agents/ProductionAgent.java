package agents;

import java.util.ArrayList;

import net.sf.jasperreports.engine.xml.JRPenFactory.Left;
import cern.jet.random.engine.MersenneTwister;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import social_simulation_project.BWeffectMeasurer;
import artefacts.ProductionBatch;

public class ProductionAgent 
{
	private int machineNumber;
	private int machineProductivity;
	private int productionQuantity;
	private ArrayList<Integer> waitingForProduction;
	private int leadTime;
	private InventoryAgent inventoryAgent;
	private ArrayList<ProductionBatch> production;
	private ArrayList<ProductionBatch> toProduce;
	
	public ProductionAgent(int leadTime, int machines, InventoryAgent inventoryAgent)
	{
		this.inventoryAgent=inventoryAgent;
		machineNumber = machines;
		machineProductivity = 35;
		toProduce = new ArrayList<ProductionBatch>();
		production = new ArrayList<ProductionBatch>();
		waitingForProduction = new ArrayList<Integer>();
		this.leadTime = leadTime;
	}
	// 1. figures out how many machines to run, which defines the production quantity
	// 2. if enough rawmaterial is in the inventory, the production starts
	// 3. the rawmaterial gets ordered
	public void produce(int nextDemand)
	{ 
		
		// 1.		
		{
			//if nothing has to be produced stop this method
			if(nextDemand==0 && waitingForProduction.isEmpty()) return;
			//if something from past period is left, produce that and put what should get produced in this tick
			//at the end of the waiting queue
			if (!waitingForProduction.isEmpty()){
				productionQuantity=productionQuantityForThisAmount(waitingForProduction.get(0));
				waitingForProduction.remove(0);
				if(nextDemand!=0)
				{
				waitingForProduction.add(nextDemand);
				}
			}
			else{
				if (nextDemand!=0) productionQuantity=productionQuantityForThisAmount(nextDemand);
			}
		}
		
		// 2.
		if (inventoryAgent.getAInventoryLevel() >= productionQuantity * 2 && inventoryAgent.getBInventoryLevel() >= productionQuantity)  {
			ProductionBatch new_production_order = new ProductionBatch(this.leadTime, productionQuantity);
			production.add(new_production_order);
			inventoryAgent.reduceAInventoryLevel(productionQuantity * 2);
			inventoryAgent.reduceBInventoryLevel(productionQuantity);
		}
		//3.
		deliverRawMaterialA(whatRawMaterialIsNeeded()*2);
		deliverRawMaterialB(whatRawMaterialIsNeeded());			
	}
	
	// production method for Retailer and Distributor
	public void produce1()
	{
		productionQuantity = this.inventoryAgent.getIncomingInventoryLevel();
		ProductionBatch new_production_order = new ProductionBatch(this.leadTime, this.inventoryAgent.getIncomingInventoryLevel());
		production.add(new_production_order);
		inventoryAgent.reduceIncomingInventoryLevel(productionQuantity);
	}
	
	// production method for Wholesaler, produces products out of 2 materials from incoming_inventory
	public void produce2()
	{
		//productionQuantity = this.inventoryAgent.getIncomingInventoryLevel() / 2;
		productionQuantity = this.inventoryAgent.getIncomingInventoryLevel();
		ProductionBatch new_production_order = new ProductionBatch(this.leadTime, this.inventoryAgent.getIncomingInventoryLevel());
		production.add(new_production_order);
		//inventoryAgent.reduceIncomingInventoryLevel(productionQuantity * 2);
		inventoryAgent.reduceIncomingInventoryLevel(productionQuantity );
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
	//calculates how much will be produced at given amount, because only whole machines can be turned on
	public int productionQuantityForThisAmount(int amount){
		int i;
		if (amount==0) return 0;
		if (amount % machineProductivity == 0) {
			if (amount/machineProductivity <= machineNumber) {
				i = (amount / machineProductivity) * machineProductivity;
			}
			else i = machineNumber * machineProductivity;
		} else {
			if (amount / machineProductivity < machineNumber) {
				i = ((amount / machineProductivity) + 1) * machineProductivity;
			}
			else i = machineNumber * machineProductivity;
		}
		BWeffectMeasurer.getMeasurer().updateManufacturer(i);
		return i;
	}
	
	//calculates how much Rawmaterial the manufacturer has to order to produce what he could not produce yet in nect tick
	public int whatRawMaterialIsNeeded(){
		int amount=0;
		for(int i :waitingForProduction)
		{
			amount+=i;
		}
		return productionQuantityForThisAmount(amount)+productionQuantity;
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