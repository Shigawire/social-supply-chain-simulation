package artefacts.trust;

import java.util.Map;

import repast.simphony.random.RandomHelper;
import actors.SupplyChainMember;
import artefacts.Order;

public class KPI {
	
	private Order shipment;
	private Trust trust;
	
	// Calculate the average distance of two supply chain Member profiles, i.e. their dimension-ratings in order to simulation shared values.
	private double compareSharedValues(SupplyChainMember s1, SupplyChainMember s2) 
	{
		Map<DimensionType, Double> ratingMap1 = s1.getTrustDimensionRatings();
		Map<DimensionType, Double> ratingMap2 = s2.getTrustDimensionRatings();
				
		// Please have a look at the documentation for the mathematical explanations.
		double x1 = (ratingMap1.get(DimensionType.RELIABILITY) - ratingMap2.get(DimensionType.RELIABILITY));
		double x2 = (ratingMap1.get(DimensionType.COMPETENCE) - ratingMap2.get(DimensionType.COMPETENCE));
		double x3 = (ratingMap1.get(DimensionType.SHARED_VALUES) - ratingMap2.get(DimensionType.SHARED_VALUES));
		double x4 = (ratingMap1.get(DimensionType.QUALITY) - ratingMap2.get(DimensionType.QUALITY));
		
		double squaredSum = (Math.pow(x1, 2) + Math.pow(x2, 2) + Math.pow(x3, 2) + Math.pow(x4, 2));
		
		return (1 - (Math.sqrt(squaredSum) / 2));
	}
	
	/**
	   * This method calculates the reliability of a shipment's supplier
	   * 
	   * @return double.
	   */
	private double calculateReliability(Order shipment) 
	{	
		// fetch the delivery history for all partial shipments of an order.
		Map<Integer, Integer> deliveryHistory = shipment.getPartialDeliveryHistory();
		
		// Check if the shipment is too late, resp. if it differs from the promised delivery date
		double diff = shipment.getExpectedDeliveryDate() - shipment.getReceivedAt();
		
		int start = (int)shipment.getExpectedDeliveryDate();
		boolean found = false;
		int partialDelivery = 0;
		// "how many items were delivered on time"
		// -> on-time KPI
		// Iterate throught the historical order data to check how many items are partially delivered by the promised date.
		while (!found && start >= shipment.getOrderedAt()) 
		{
			if (deliveryHistory.get(start) != null) {
				partialDelivery = deliveryHistory.get(start);
				found = true;
			}
			start = start - 1;
		}
		
		if (diff >= 0) {
			// shipment is on time
			return 1.0;
		} else {
			// return the on-time KPI
			return ((double)partialDelivery / (double)shipment.getQuantity());
		}
	}
	
	public KPI(Order shipment, Trust trust)
	{
		this.shipment = shipment;
		this.trust = trust;
	}
	
	// Return the KPI between 0..1 for a specific trust dimension
	public double getKPIForDimension(DimensionType dimensionType) 
	{	
		double kpiValue = 0;
		// switch through all available dimensions
		switch (dimensionType) 
		{
			case RELIABILITY:
				double reliability = calculateReliability(shipment);
				
				kpiValue = reliability;
				break;

			case QUALITY:
				double shipmentQuality = shipment.getShipmentQuality();
							
				kpiValue = shipmentQuality;
				break;
				
			case SHARED_VALUES:
				double sharedValues = compareSharedValues(shipment.getOrderAgent().getParent(), shipment.getDeliveryAgent().getParent());
				
				kpiValue = sharedValues;
				break;
				
			case COMPETENCE:
				//get current competence Value..
				double competenceValue = trust.getCurrentCompetenceValue();
				//check if the last trust update was positive or negative..
				boolean direction = trust.getHistoricalTrustAppraisal();
				
				//and update the competence KPI accordingly.
				if (direction) {
					trust.setCurrentCompetenceValue(competenceValue + 0.1);
				} else {
					trust.setCurrentCompetenceValue(competenceValue - 0.1);
				}
				
				// return the new competence value
				kpiValue = trust.getCurrentCompetenceValue();
			
				break;
		}
		return kpiValue;
	}
}