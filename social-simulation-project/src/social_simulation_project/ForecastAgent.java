package social_simulation_project;

import repast.simphony.random.RandomHelper;

/**
* This class represents the forecast agent. 
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class ForecastAgent 
{
	public int calculateDemand()
	{
		int dem = RandomHelper.nextIntFromTo(2, 25);
		//System.out.println(dem);
		return dem;
		
		//return 10;
	}
	
	/*
	 * GETTERS
	 */
	
	/* 
	 * SETTERS
	 */
}