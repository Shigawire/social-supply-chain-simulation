package artefacts.trust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trust 
{
	private ArrayList<TrustDimension> trustDimensions; 
	
	private ReliabilityDimension reliability;
	private CompetenceDimension competence;
	private QualityDimension quality;
	private SharedValuesDimension shared_values;
	
	private Map<DimensionType, TrustDimension> dimensionMapping = new HashMap<DimensionType, TrustDimension>();
	
	public Trust(ReliabilityDimension reliability, 
			     CompetenceDimension competence, 
			     QualityDimension quality, 
			     SharedValuesDimension shared_values)
	{
		this.reliability = reliability;
		this.competence = competence;
		this.quality = quality;
		this.shared_values = shared_values;
		
		dimensionMapping.put(DimensionType.RELIABILITY, reliability);
		dimensionMapping.put(DimensionType.COMPETENCE, competence);
		dimensionMapping.put(DimensionType.QUALITY, quality);
		dimensionMapping.put(DimensionType.SHARED_VALUES, shared_values);
	}
	
	public double getUnifiedTrustValue() 
	{
		return 0.95;
	}
	
	public TrustDimension getDimension(DimensionType type) 
	{
		return dimensionMapping.get(type);
	}
}