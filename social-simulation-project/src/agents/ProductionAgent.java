package agents;

import java.util.ArrayList;

import cern.jet.random.engine.MersenneTwister;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import artefacts.ProductionBatch;

public class ProductionAgent 
{
	private int machine_number;
	private int machine_productivity;
	private int production_quantity;
	private int lead_time;
	private InventoryAgent inventoryAgent;
	private ArrayList<ProductionBatch> Production;
	private ArrayList<ProductionBatch> toProduce;
	
	public ProductionAgent(int lead_time, int machines, InventoryAgent inventoryAgent)
	{
		this.inventoryAgent=inventoryAgent;
		machine_number = machines;
		machine_productivity = 50;
		toProduce = new ArrayList<ProductionBatch>();
		Production = new ArrayList<ProductionBatch>();
		this.lead_time = lead_time;
	}
	// 1. figures out how many machines to run, which defines the production quantity
	// 2. if enough rawmaterial is in the inventory, the production starts with 10% loss in 5% of the cases
	// 3. else the rawmaterial gets ordered
	public void produce(int nextDemand)
	{ 
		//1.
		if(nextDemand % machine_productivity==0){
			if (nextDemand/machine_productivity<=machine_number)
			{
			production_quantity = (nextDemand/machine_productivity)*machine_productivity;
			}
			else production_quantity = machine_number*machine_productivity;
		}
		else{
			if (nextDemand/machine_productivity<machine_number)
			{
			production_quantity = ((nextDemand/machine_productivity)+1)*machine_productivity;
			}
			else production_quantity = machine_number*machine_productivity;

		}
		//2.
		if (inventoryAgent.getAInventoryLevel()>=production_quantity*2 && inventoryAgent.getBInventoryLevel()>=production_quantity) 
		{
//			int dem = RandomHelper.nextIntFromTo(1, 20);
//			if (dem==1)production_quantity=production_quantity*90/100;
			ProductionBatch new_production_order = new ProductionBatch(this.lead_time, production_quantity);
			Production.add(new_production_order);
			inventoryAgent.reduceAInventoryLevel(production_quantity*2);
			inventoryAgent.reduceBInventoryLevel(production_quantity);
		}
		//3.
		else{
			if (inventoryAgent.getAInventoryLevel()<production_quantity*2)
			{	
				deliverRawMaterialA(production_quantity*2);
			}
			if (inventoryAgent.getBInventoryLevel()<production_quantity)
			{
				deliverRawMaterialB(production_quantity);
			}
		}
	}
	
	//production method for Retailer and Distributor
	public void label()
	{
		production_quantity = this.inventoryAgent.getIncomingInventoryLevel();
		ProductionBatch new_production_order = new ProductionBatch(this.lead_time, this.inventoryAgent.getIncomingInventoryLevel());
		Production.add(new_production_order);
		inventoryAgent.reduceIncomingInventoryLevel(production_quantity);
	}
	//production method for Wholesaler
	public void process()
	{
		production_quantity = this.inventoryAgent.getIncomingInventoryLevel()/2;
		ProductionBatch new_production_order = new ProductionBatch(this.lead_time, this.inventoryAgent.getIncomingInventoryLevel());
		Production.add(new_production_order);
		inventoryAgent.reduceIncomingInventoryLevel(production_quantity*2);
	}
	// put finally produced products into outgoing inventory
	public void harvest ()
	{
		for (ProductionBatch current_batch : Production) 
		{
			current_batch.incrementTimeInProduction();
			if (current_batch.getTimeInProduction() >= this.lead_time) 
			{
				//This batch is ready to be added to inventory
				this.inventoryAgent.increaseOutgoingInventoryLevel(current_batch.getQuantity());
				//Production.remove(current_batch);
			} 
			else
			{
				toProduce.add(current_batch);
			}
		}
		
		Production.clear();
		Production.addAll(toProduce);
		toProduce.clear();
	}
	public void deliverRawMaterialA(int amount){
		RandomHelper.createNormal(97, 2);
		int var = RandomHelper.getNormal().nextInt();
		amount= amount*var/100;
		inventoryAgent.increaseAInventoryLevel(amount);
	}
	public void deliverRawMaterialB(int amount){
		RandomHelper.createNormal(97, 2);
		int var = RandomHelper.getNormal().nextInt();
		amount= amount*var/100;
		inventoryAgent.increaseBInventoryLevel(amount);
	}
}