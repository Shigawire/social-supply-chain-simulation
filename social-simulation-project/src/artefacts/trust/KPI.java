package artefacts.trust;

import java.util.Map;

import repast.simphony.random.RandomHelper;
import actors.SupplyChainMember;
import artefacts.Order;

public class KPI {
	
	private Order shipment;
	private Trust trust;
	
	private double compareSharedValues(SupplyChainMember s1, SupplyChainMember s2) {
		Map<DimensionType, Double> ratingMap1 = s1.getTrustDimensionRatings();
		Map<DimensionType, Double> ratingMap2 = s2.getTrustDimensionRatings();
				
		double x1 = (ratingMap1.get(DimensionType.RELIABILITY) - ratingMap2.get(DimensionType.RELIABILITY));
		double x2 = (ratingMap1.get(DimensionType.COMPETENCE) - ratingMap2.get(DimensionType.COMPETENCE));
		double x3 = (ratingMap1.get(DimensionType.SHARED_VALUES) - ratingMap2.get(DimensionType.SHARED_VALUES));
		double x4 = (ratingMap1.get(DimensionType.QUALITY) - ratingMap2.get(DimensionType.QUALITY));
		
				
		double squared_sum = (Math.pow(x1, 2) + Math.pow(x2, 2) + Math.pow(x3, 2) + Math.pow(x4, 2));
		
		return (1 - (Math.sqrt(squared_sum) / 2));
	}
	
	private double calculateReliability(Order shipment) {
		//return RandomHelper.nextDoubleFromTo(0.95, 1);
		Map<Integer, Integer> deliveryHistory = shipment.getPartialDeliveryHistory();
		
		//System.out.println("Full delivery was expected until: " + shipment.getExpectedDeliveryDate());
		//System.out.println("Shipment arrived at: " + shipment.getReceivedAt());
		double diff = shipment.getExpectedDeliveryDate() - shipment.getReceivedAt();
		
		//System.out.println("That's a difference of: " + diff);
		
		
		/*for (Map.Entry<Integer, Integer> entry : deliveryHistory.entrySet()) {
		    System.out.println("Total Delivery in Tick " + entry.getKey()+": "+entry.getValue() + " items");
		}
		*/
		
		int start = (int)shipment.getExpectedDeliveryDate();
		boolean found = false;
		int partialDelivery = 0;
		// Find the point in time where the order was basically fulfilled in time, resp. "how many items were delivered on time"
		while (!found && start >= shipment.getOrderedAt()) {
			if (deliveryHistory.get(start) != null) {
				partialDelivery = deliveryHistory.get(start);
				found = true;
			}
			start = start - 1;
		}
		
		//System.out.println("Shipment fulfilled: " + partialDelivery + " of " + shipment.getQuantity() + " (Quote: " + (double)partialDelivery/(double)shipment.getQuantity() + ")");
		
		if (diff >= 0) {
			//shipment is on time
			return 1.0;
		} else {
			return ((double)partialDelivery/(double)shipment.getQuantity());
		}
		
	}
	
	public KPI(Order shipment, Trust trust){
		this.shipment = shipment;
		this.trust = trust;
	}
	
	public double getKPIForDimension(DimensionType dimensionType) {
		
		double kpiValue = 0;
		switch (dimensionType) {
		case RELIABILITY:
			double reliability = calculateReliability(shipment);
			
			kpiValue = reliability;
			
			break;

		case QUALITY:
			double shipment_quality = shipment.getShipmentQuality();
						
			kpiValue = shipment_quality;
			break;
			
		case SHARED_VALUES:
			double shared_values = compareSharedValues(shipment.getOrderAgent().getParent(), shipment.getDeliveryAgent().getParent());
			
			kpiValue = shared_values;
			break;
			
		case COMPETENCE:
			double competence_value = trust.getCurrentCompetenceValue();
			boolean direction = trust.getHistoricalTrustAppraisal();
			
			if (direction) {
				trust.setCurrentCompetenceValue(competence_value + 0.1);
			} else {
				trust.setCurrentCompetenceValue(competence_value - 0.1);
			}
			
			//competence_member
			kpiValue = competence_value;
		
			
			break;
		}
		return kpiValue;
	}

}
