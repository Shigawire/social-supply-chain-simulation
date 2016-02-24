package artefacts.trust;

abstract public class TrustDimension 
{
	private double dimensionRating; // how important is this trust dimension to the specific agent?
	private double trustValue;
	
	// Just the parent class for each dimension...
	public TrustDimension(double dimensionRating, double startingValue) 
	{
		this.trustValue = startingValue;
		this.dimensionRating = dimensionRating;
	}
	
	public void updateDimension(double trustValue) 
	{
		this.trustValue += trustValue;
	}
	
	/*
	 * GETTERS
	 */
	public double getDimensionRating() 
	{
		return this.dimensionRating;
	}
	
	public double getTrustValue() 
	{
		return this.trustValue;
	}
}