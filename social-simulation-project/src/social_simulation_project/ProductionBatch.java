package social_simulation_project;

/**
* This class represents a production batch.
* It is used by the manufacturer for production.
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class ProductionBatch 
{
	private int total_production_time;
	private int time_in_production;
	private int quantity;
	
	public ProductionBatch(int total_production_time, int quantity) 
	{
		this.total_production_time = total_production_time;
		this.quantity = quantity;
		this.time_in_production = 0;
	}
	
	public void incrementTimeInProduction()
	{
		this.time_in_production++;
	}
	
	/*
	 * GETTERS
	 */
	public int getTimeInProduction() 
	{
		return this.time_in_production;
	}
	
	public int getQuantity() 
	{
		return this.quantity;
	}
	
	/*
	 * SETTERS
	 */
}