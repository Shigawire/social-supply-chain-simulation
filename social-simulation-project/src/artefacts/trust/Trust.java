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
	
	private double tValue = 0.5;
	private double oldtValue = 0.5;
	
	private double currentCompetenceValue = 0.5;
	
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
		return this.tValue;
	}
	
	public void setUnifiedTrustValue(double tValue) 
	{
		this.oldtValue = this.tValue;
		this.tValue = tValue;
	}
	
	//Returns true if the last trust update was positive, negative else.
	public boolean getHistoricalTrustAppraisal() {
		if (this.oldtValue > this.tValue) {
			return false;
			//negative
			//System.out.println("Trust update is negative [from "+ _oldtValue + " to " + _newtValue +  "]");
		} else {
			return true;
			//System.out.println("Trust update is positive [from "+ _oldtValue + " to " + _newtValue +  "]");
		}
	}
	
	public double getCurrentCompetenceValue() {
		return this.currentCompetenceValue;
	}
	
	public void setCurrentCompetenceValue(double competence) {
		if (competence > 1) competence = 1;
		if (competence < 0) competence = 0;
		this.currentCompetenceValue = Math.ceil(competence, 1);
	}
	
	public TrustDimension getDimension(DimensionType type) 
	{
		return dimensionMapping.get(type);
	}
}