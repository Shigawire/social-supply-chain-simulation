package artefacts.trust;

import java.util.Map;

import repast.simphony.random.RandomHelper;
import actors.SupplyChainMember;
import artefacts.Order;

public class KPI {
	
	private Order shipment;
	
	private double compareSharedValues(SupplyChainMember s1, SupplyChainMember s2) {
		Map<DimensionType, Double> ratingMap1 = s1.getTrustDimensionRatings();
		Map<DimensionType, Double> ratingMap2 = s2.getTrustDimensionRatings();
		
		double x1 = (ratingMap1.get(DimensionType.RELIABILITY) - ratingMap2.get(DimensionType.RELIABILITY));
		double x2 = (ratingMap1.get(DimensionType.COMPETENCE) - ratingMap2.get(DimensionType.COMPETENCE));
		double x3 = (ratingMap1.get(DimensionType.SHARED_VALUES) - ratingMap2.get(DimensionType.SHARED_VALUES));
		double x4 = (ratingMap1.get(DimensionType.QUALITY) - ratingMap2.get(DimensionType.QUALITY));
		
		double squared_sum = (Math.pow(x1, 2) + Math.pow(x2, 2) + Math.pow(x3, 2) + Math.pow(x4, 2));
		
		return (Math.sqrt(squared_sum) / 2);
	}
	
	private double calculateReliability(Order shipment) {
		return RandomHelper.nextDoubleFromTo(0.95, 1);
	}
	
	public KPI(Order shipment){
		this.shipment = shipment;
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
			double competence_value = 0.96;
			kpiValue = competence_value;
			
			break;
		}
		return kpiValue;
	}

}
