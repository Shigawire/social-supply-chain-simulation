package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import actors.SupplyChainMember;
import artefacts.Order;
import artefacts.trust.CompetenceDimension;
import artefacts.trust.DimensionType;
import artefacts.trust.KPI;
import artefacts.trust.QualityDimension;
import artefacts.trust.ReliabilityDimension;
import artefacts.trust.SharedValuesDimension;
import artefacts.trust.Trust;
import artefacts.trust.TrustDimension;


/**
* This class represents a trust agent. 
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class TrustAgent 
{
	private SupplyChainMember supplyChainMember;
	private ArrayList<DeliveryAgent> delivery_agents;
	
	private Map<DeliveryAgent, Trust> trustStorage = new HashMap<DeliveryAgent, Trust>();
	
	// When do we classify a shipment as overdue?
	private double ShipmentRuntimeOverdueThreshold = 2;
	

	public TrustAgent(ArrayList<DeliveryAgent> delivery_agents, Map<DimensionType, Double> dimensionRatings, SupplyChainMember supplyChainMember)

	{
		this.delivery_agents = delivery_agents;
		this.supplyChainMember = supplyChainMember;
		for (DeliveryAgent delivery_agent : this.delivery_agents) 
		{
			// Alle TrustDimensions starten mit 25% Wichtigkeit und 50% Initialwert
			
			ReliabilityDimension reliability = new ReliabilityDimension(dimensionRatings.get(DimensionType.RELIABILITY), 0.5);
			CompetenceDimension competence = new CompetenceDimension(dimensionRatings.get(DimensionType.COMPETENCE), 0.5);
			QualityDimension quality = new QualityDimension(dimensionRatings.get(DimensionType.QUALITY), 0.5);
			SharedValuesDimension shared_values = new SharedValuesDimension(dimensionRatings.get(DimensionType.SHARED_VALUES), 0.5);
			
			Trust trust = new Trust(reliability, competence, quality, shared_values);
			
			trustStorage.put(delivery_agent, trust);
		}
	}

	private void updateTrustDimensionValue(DeliveryAgent delivery_agent, DimensionType type, double value) 
	{
		this.trustStorage.get(delivery_agent).getDimension(type).updateDimension(value);
	}
	
	public void inspectNewArrivals(OrderAgent orderAgent) 
	{
		ArrayList<Order> shipments = orderAgent.getReceivedShipments();

		for (Order shipment : shipments) 
		{
			inspectShipment(orderAgent, shipment);
		}
	}
	
	//Jedes Shipment wird einzeln untersucht, daraufhin wird der Trust-Wert der spezifischen Dimension eines bestimmten orderAgent geändert
	private void inspectShipment(OrderAgent orderAgent, Order shipment) 
	{
		//reliability
		//is the shipment overdue?
		int runtime = (shipment.getReceivedAt() - shipment.getOrderedAt());
		//runtime is at least 2 weeks: ordered at 1, processed at 2, delivered at 3
						
		DimensionType[] dimensions = {DimensionType.RELIABILITY, DimensionType.COMPETENCE, DimensionType.QUALITY, DimensionType.SHARED_VALUES};
		
		Map<TrustDimension, Double> orderFulfillments = new HashMap<TrustDimension, Double>();
		
		Trust trust = trustStorage.get(shipment.getDeliveryAgent());
		
		KPI Kpi = new KPI(shipment);
		
		for (DimensionType dimensionType : dimensions) {
		
			double kpiValue = Kpi.getKPIForDimension(dimensionType);
			
			orderFulfillments.put(trust.getDimension(DimensionType.RELIABILITY), kpiValue);
		
		}
		
				
		// recalculate reliability:
		
		
	}
	
	
	/*
	 * GETTERS
	 */
	public double getTrustValue(DeliveryAgent delivery_agent) 
	{
		//hier muss der trust wert zurueclgegeben werden.
		return this.trustStorage.get(delivery_agent).getUnifiedTrustValue();
	} 
	public Trust getTrustObject(DeliveryAgent delivery_agent){
		return this.trustStorage.get(delivery_agent);
	}
	public DeliveryAgent getCheapestSupplier() 
	{
		DeliveryAgent cheapestSupplier = delivery_agents.get(0);
		
		for (int i = 0; i < delivery_agents.size() - 1; i++)
		{
			if (cheapestSupplier.getPrice() > delivery_agents.get(i).getPrice())
			{
				cheapestSupplier = delivery_agents.get(i);
			}
		}
		return cheapestSupplier;
	}

	public SupplyChainMember getSupplyChainMember() {
		// TODO Auto-generated method stub
		return supplyChainMember;
	}
	
	public Trust getTrustAbout(DeliveryAgent deliveryAgent) {
		return trustStorage.get(deliveryAgent);
	}
	
	/* 
	 * SETTERS
	 */
}