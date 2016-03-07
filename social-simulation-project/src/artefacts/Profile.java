package artefacts;

// class to save profile of the actor
public class Profile 
{
	public Profile(double priceRelevance, double deliveryTimeRelevance,
				   double trustRelevance, double competenceRelevance,
				   double reliabilityRelevance, double qualityRelevance,
				   double sharedValuesRelevance, double orderTickEarlier,
				   double willNotOrder, double indirectTrust)
	{
		this.priceRelevance = priceRelevance;
		this.deliveryTimeRelevance = deliveryTimeRelevance;
		this.trustRelevance = trustRelevance;
		this.competenceRelevance = competenceRelevance;
		this.reliabilityRelevance = reliabilityRelevance;
		this.qualityRelevance = qualityRelevance;
		this.sharedValuesRelevance = sharedValuesRelevance;
		this.orderTickEarlier = orderTickEarlier;
		this.willNotOrder = willNotOrder;
		this.indirectTrust = indirectTrust;
	}
	
	// Procurement
	private double priceRelevance;
	private double deliveryTimeRelevance;
	private double trustRelevance;
	
	// Trust Dimensions
	private double competenceRelevance;
	private double reliabilityRelevance;
	private double qualityRelevance;
	private double sharedValuesRelevance;
	
	// Information sharing
	private double orderTickEarlier;
	private double willNotOrder;
	
	// Indirect trust
	private double indirectTrust;
	
	/*
	 * GETTERS
	 */
	public double getPriceRelevance() 
	{
		return priceRelevance;
	}
	
	public double getDeliveryTimeRelevance() 
	{
		return deliveryTimeRelevance;
	}
	
	public double getTrustRelevance() 
	{
		return trustRelevance;
	}
	
	public double getCompetenceRelevance()
	{
		return competenceRelevance;
	}
	
	public double getReliabilityRelevance() 
	{
		return reliabilityRelevance;
	}
	
	public double getQualityRelevance() 
	{
		return qualityRelevance;
	}
	
	public double getSharedValuesRelevance() 
	{
		return sharedValuesRelevance;
	}
	
	public double getOrderTickEarlier() 
	{
		return orderTickEarlier;
	}
	
	public double getWillNotOrder() 
	{
		return willNotOrder;
	}
	
	public double getIndirectTrust() 
	{
		return indirectTrust;
	}
	
	/*
	 * SETTERS
	 */
	public void setPriceRelevance(double priceRelevance)
	{
		this.priceRelevance = priceRelevance;
	}
	
	public void setDeliveryTimeRelevance(double deliveryTimeRelevance) 
	{
		this.deliveryTimeRelevance = deliveryTimeRelevance;
	}
	
	public void setTrustRelevance(double trustRelevance) 
	{
		this.trustRelevance = trustRelevance;
	}
	
	public void setCompetenceRelevance(double competenceRelevance) 
	{
		this.competenceRelevance = competenceRelevance;
	}
	
	public void setReliabilityRelevance(double reliabilityRelevance) 
	{
		this.reliabilityRelevance = reliabilityRelevance;
	}
	
	public void setQualityRelevance(double qualityRelevance) 
	{
		this.qualityRelevance = qualityRelevance;
	}
	
	public void setSharedValuesRelevance(double sharedValuesRelevance) 
	{
		this.sharedValuesRelevance = sharedValuesRelevance;
	}
	
	public void setOrderTickEarlier(double orderTickEarlier) 
	{
		this.orderTickEarlier = orderTickEarlier;
	}
	
	public void setWillNotOrder(double willNotOrder) 
	{
		this.willNotOrder = willNotOrder;
	}
	
	public void setIndirectTrust(double indirectTrust)
	{
		this.indirectTrust = indirectTrust;
	}
}