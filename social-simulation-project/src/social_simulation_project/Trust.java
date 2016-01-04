package social_simulation_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trust {

	private ArrayList<TrustDimension> trust_dimensions; 
	
	private ReliabilityDimension reliability;
	private CompetenceDimension competence;
	private QualityDimension quality;
	private SharedValuesDimension shared_values;
	
	private Map<DimensionType, TrustDimension> dimension_mapping = new HashMap<DimensionType, TrustDimension>();
	
	public Trust(ReliabilityDimension reliability, CompetenceDimension competence, QualityDimension quality, SharedValuesDimension shared_values) {
		this.reliability = reliability;
		this.competence = competence;
		this.quality = quality;
		this.shared_values = shared_values;
		
		dimension_mapping.put(DimensionType.RELIABILITY, reliability);
		dimension_mapping.put(DimensionType.COMPETENCE, competence);
		dimension_mapping.put(DimensionType.QUALITY, quality);
		dimension_mapping.put(DimensionType.SHARED_VALUES, shared_values);
	}
	
	public double getUnifiedTrustValue() {
		return 0.95;
	}
	
	public TrustDimension getDimension(DimensionType type) {
		return dimension_mapping.get(type);
	}
}
