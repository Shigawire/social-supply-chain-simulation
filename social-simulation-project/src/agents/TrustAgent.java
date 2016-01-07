package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import artefacts.Order;
import artefacts.trust.CompetenceDimension;
import artefacts.trust.DimensionType;
import artefacts.trust.QualityDimension;
import artefacts.trust.ReliabilityDimension;
import artefacts.trust.SharedValuesDimension;
import artefacts.trust.Trust;


/**
* This class represents a trust agent. 
*
* @author  PS Development Team
* @since   2015-11-30
*/
public class TrustAgent 
{
	private ArrayList<DeliveryAgent> delivery_agents;
	
	private Map<DeliveryAgent, Trust> trustStorage = new HashMap<DeliveryAgent, Trust>();
	
	// When do we classify a shipment as overdue?
	private double ShipmentRuntimeOverdueThreshold = 2;
	
	public TrustAgent(ArrayList<DeliveryAgent> delivery_agents)
	{
		this.delivery_agents = delivery_agents;
		for (DeliveryAgent delivery_agent : this.delivery_agents) 
		{
			// Alle TrustDimensions starten mit 90% Wichtigkeit und 50% Initialwert
			
			ReliabilityDimension reliability = new ReliabilityDimension(0.9, 0.5);
			CompetenceDimension competence = new CompetenceDimension(0.9, 0.5);
			QualityDimension quality = new QualityDimension(0.9, 0.5);
			SharedValuesDimension shared_values = new SharedValuesDimension(0.9, 0.5);
			
			Trust trust = new Trust(reliability, competence, quality, shared_values);
			
			trustStorage.put(delivery_agent, trust);
		}
	}

	private void updateTrust(DeliveryAgent delivery_agent, DimensionType type, double value) 
	{
		this.trustStorage.get(delivery_agent).getDimension(type).updateDimension(value);
	}
	
	public void inspectNewArrivals(ArrayList<Order> shipments) 
	{
		for (Order shipment : shipments) 
		{
			inspectShipment(shipment);
		}
	}
	
	private void inspectShipment(Order shipment) 
	{
		//is the shipment overdue?
		int runtime = (shipment.getReceivedAt() - shipment.getOrderedAt());
		double reliabilityImpression = (runtime > this.ShipmentRuntimeOverdueThreshold) ? -0.1 : 0.1;
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
	
	/* 
	 * SETTERS
	 */
}