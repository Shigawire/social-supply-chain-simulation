package agents;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.random.RandomHelper;
import social_simulation_project.BWeffectMeasurer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.SupplyChainMember;
import actors.Wholesaler;
import artefacts.ProductionBatch;

public class ProductionAgent
{
	
	private int machineQuantity;
	
	private int machineProductivity;
	
	private int productionQuantity;
	private ArrayList<Integer> waitingForProduction;
	private int leadTime;
	private InventoryAgent inventoryAgent;
	private ArrayList<ProductionBatch> production;
	
	private SupplyChainMember parent;
	
	public ProductionAgent(int leadTime, int machineQuantity, InventoryAgent inventoryAgent, SupplyChainMember parent)
	{
		this.parent = parent;
		this.inventoryAgent=inventoryAgent;
		
		//how many manchines does the production Agent have?
		this.machineQuantity = machineQuantity;
		
		//productivity of each machine
		this.machineProductivity = 35;
		
		//This List holds all ProductionBatches that the manufacturer should process
		this.production = new ArrayList<ProductionBatch>();
		
		
		this.waitingForProduction = new ArrayList<Integer>();
		this.leadTime = leadTime;
	}
	
	// 1. figures out how many machines to run, which defines the production quantity
	// 2. if enough rawmaterial is in the inventory, the production starts
	// 3. the rawmaterial gets ordered
	
	public void produce(int... nextDemand)
	{ 
		//Depending on the parent class, the production class is a bit different.
		if (this.parent instanceof Retailer || this.parent instanceof Distributor) {
			productionQuantity = this.inventoryAgent.getIncomingInventoryLevel();
			ProductionBatch new_production_order = new ProductionBatch(this.leadTime, this.inventoryAgent.getIncomingInventoryLevel());
			production.add(new_production_order);
			inventoryAgent.reduceIncomingInventoryLevel(productionQuantity);
			
		//The Wholesaler is assembling two incoming elements and returns a single element 
		} else if (this.parent instanceof Wholesaler) {
			productionQuantity = this.inventoryAgent.getIncomingInventoryLevel() / 2;
			ProductionBatch new_production_order = new ProductionBatch(this.leadTime, this.inventoryAgent.getIncomingInventoryLevel());
			production.add(new_production_order);
			inventoryAgent.reduceIncomingInventoryLevel(productionQuantity * 2);
			
		} else if (this.parent instanceof Manufacturer)  {
			
				//if nothing has to be produced stop this method
				if(nextDemand[0]==0 && waitingForProduction.isEmpty()) return;
				//if something from past period is left, produce that and put what should get produced in this tick
				//at the end of the waiting queue
				if (!waitingForProduction.isEmpty()){
					productionQuantity=productionQuantityForThisAmount(waitingForProduction.get(0));
					waitingForProduction.remove(0);
					if(nextDemand[0]!=0)
					{
					waitingForProduction.add(nextDemand[0]);
					}
				}
				else{
					if (nextDemand[0]!=0) productionQuantity=productionQuantityForThisAmount(nextDemand[0]);
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
	}
	
	
	// transfer items from the incoming to the outgoing inventory - consider this a "harvesting" process
	public void transferInventories()
	{
		List<ProductionBatch> toProduce = new ArrayList<ProductionBatch>();

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
		
		//Repast is not working nicely with modified ArrayLists - somewhere a pointer gets lost. Thus, clear the Array and re-add necessary items.
		production.clear();
		production.addAll(toProduce);
		toProduce.clear();
	}
	//calculates how much will be produced at given demand, because only whole machines can be turned on. 
	//Thus, only multiples of machineProductivity can be produced
	public int productionQuantityForThisAmount(int amount){
		int i;
		if (amount==0) return 0;
		if (amount % machineProductivity == 0) {
			if (amount/machineProductivity <= machineQuantity) {
				i = (amount / machineProductivity) * machineProductivity;
			}
			else i = machineQuantity * machineProductivity;
		} else {
			if (amount / machineProductivity < machineQuantity) {
				i = ((amount / machineProductivity) + 1) * machineProductivity;
			}
			else i = machineQuantity * machineProductivity;
		}
		BWeffectMeasurer.getMeasurer().updateManufacturer(i);
		return i;
	}
	
	//calculates how much Rawmaterial the manufacturer has to order to produce what he could not produce yet in next tick
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