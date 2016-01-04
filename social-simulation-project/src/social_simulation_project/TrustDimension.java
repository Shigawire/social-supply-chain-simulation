package social_simulation_project;

enum DimensionType {
	RELIABILITY, COMPETENCE, QUALITY, SHARED_VALUES
};

abstract public class TrustDimension {
	
	private double dimension_rating; //how important is this trust dimension to the specific agent?
	private double trust_value;
	
	//private DimensionType dimension_type;
	
	public TrustDimension(double dimension_rating, double starting_value) {
		this.trust_value = starting_value;
		this.dimension_rating = dimension_rating;
	}
	
	public double getDimensionRating() {
		return this.dimension_rating;
	}
	
	public double getTrustValue() {
		return this.trust_value;
	}
	
	public void updateDimension(double trust_value) {
		this.trust_value+= trust_value;
	}

}
