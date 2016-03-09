package artefacts.trust;

import java.util.HashMap;
import java.util.Map;

public class Trust 
{
	
	//trust Value - initialized with 0.5
	private double tValue = 0.5;
	
	//historical trust Value
	private double oldtValue = 0.5;
	
	//The current competence value between two supply chain members
	private double currentCompetenceValue = 0.5;
	
	//Map the dimension types to a specific trust dimension
	private Map<DimensionType, TrustDimension> dimensionMapping = new HashMap<DimensionType, TrustDimension>();
	
	// Create a Trust Object the encapsulates the four dimensions
	public Trust(ReliabilityDimension reliability, 
			     CompetenceDimension competence, 
			     QualityDimension quality, 
			     SharedValuesDimension sharedValues)
	{
		
		// Map all dimensions to their type.
		dimensionMapping.put(DimensionType.RELIABILITY, reliability);
		dimensionMapping.put(DimensionType.COMPETENCE, competence);
		dimensionMapping.put(DimensionType.QUALITY, quality);
		dimensionMapping.put(DimensionType.SHARED_VALUES, sharedValues);
	}
	
	/*
	 * GETTERS
	 */
	public double getUnifiedTrustValue() 
	{
		return this.tValue;
	}
	
	// Returns true if the last trust update was positive, negative else.
	public boolean getHistoricalTrustAppraisal() 
	{
		if (this.oldtValue > this.tValue) {
			return false;
			// negative
		} else {
			return true;
		}
	}
	
	public double getCurrentCompetenceValue() 
	{
		return this.currentCompetenceValue;
	}
	
	public TrustDimension getDimension(DimensionType type) 
	{
		return dimensionMapping.get(type);
	}
		
	/*
	 * SETTERS
	 */
	public void setUnifiedTrustValue(double tValue) 
	{
		this.oldtValue = this.tValue;
		this.tValue = tValue;
	}
	
	// write the new competence value.. 
		// Basically this value depicts the competence between two supply chain members. 
	public void setCurrentCompetenceValue(double competence) 
	{
		// Ensure the competence value is never below 0 or above 1.
		if (competence > 1) competence = 1;
		if (competence < 0) competence = 0;
		this.currentCompetenceValue = competence;
	}
}