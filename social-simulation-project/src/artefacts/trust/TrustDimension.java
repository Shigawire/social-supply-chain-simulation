package artefacts.trust;

abstract public class TrustDimension 
{
	private double dimensionRating; // how important is this trust dimension to the specific agent?
	private double trustValue;
	
	// private DimensionType dimension_type;
	
	public TrustDimension(double dimension_rating, double starting_value) 
	{
		this.trustValue = starting_value;
		this.dimensionRating = dimension_rating;
	}
	
	public double getDimensionRating() 
	{
		return this.dimensionRating;
	}
	
	public double getTrustValue() 
	{
		return this.trustValue;
	}
	
	public void updateDimension(double trust_value) 
	{
		this.trustValue+= trust_value;
	}
}