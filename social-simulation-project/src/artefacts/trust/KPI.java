package artefacts.trust;

import artefacts.Order;
import artefacts.trust.DimensionType;

public class KPI {
	
	private Order shipment;
	
	public KPI(Order shipment){
		this.shipment = shipment;
	}
	
	public double getKPIForDimension(DimensionType dimensionType) {
		
		double kpiValue = 0;
		switch (dimensionType) {
		case RELIABILITY:
			
			double runtime = shipment.getReceivedAt() - shipment.getOrderedAt();
			
			double delay = shipment.getReceivedAt() - shipment.getExpectedDeliveryDate();
			
			if (delay <= 0) {
				kpiValue = 0;
			}
			
			break;
		}
		
		return kpiValue;
	}

}
