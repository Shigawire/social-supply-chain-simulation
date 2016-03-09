package artefacts.trust;

abstract public class TrustDimension 
{
	private double dimensionRating; // how important is this trust dimension to the specific agent?
	
	// Just the parent class for each dimension...
	public TrustDimension(double dimensionRating) 
	{
		this.dimensionRating = dimensionRating;
	}

	
	/*
	 * GETTERS
	 */
	public double getDimensionRating() 
	{
		return this.dimensionRating;
	}
	
}