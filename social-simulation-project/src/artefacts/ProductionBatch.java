package artefacts;

/**
* This class represents a production batch.
* It is used by the manufacturer for production.
*
*/
public class ProductionBatch 
{
	private int totalProductionTime;
	private int timeInProduction;
	private int quantity;
	
	public ProductionBatch(int totalProductionTime, int quantity) 
	{
		this.totalProductionTime = totalProductionTime;
		this.quantity = quantity;
		this.timeInProduction = 0;
	}
	
	public void incrementTimeInProduction()
	{
		this.timeInProduction++;
	}
	
	/*
	 * GETTERS
	 */
	public int getTimeInProduction() 
	{
		return this.timeInProduction;
	}
	
	public int getQuantity() 
	{
		return this.quantity;
	}
	
	public int getTotalProductionTime() {
		return this.totalProductionTime;
	}
}