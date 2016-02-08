package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import repast.simphony.random.RandomHelper;
import repast.simphony.systemdynamics.translator.InformationManagers;
import actors.SupplyChainMember;
import artefacts.Order;
import artefacts.trust.CompetenceDimension;
import artefacts.trust.DimensionType;
import artefacts.trust.KPI;
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
	private SupplyChainMember supplyChainMember;
	private ArrayList<DeliveryAgent> delivery_agents;
	
	private Map<DeliveryAgent, Trust> trustStorage = new HashMap<DeliveryAgent, Trust>();
	
	// When do we classify a shipment as overdue?
	private double ShipmentRuntimeOverdueThreshold = 2;
	
	//private double tValue = 0.5;
	private double learningRate = 0.6;
	

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
	
	/*public void inspectNewArrivals(OrderAgent orderAgent) 
	{
		ArrayList<Order> shipments = orderAgent.getReceivedShipments();

		for (Order shipment : shipments) 
		{
			inspectShipment(orderAgent, shipment);
		}
	}
	*/
	
	//Jedes Shipment wird einzeln untersucht, daraufhin wird der Trust-Wert der spezifischen Dimension eines bestimmten orderAgent geändert
	public void inspectShipment(OrderAgent orderAgent, Order shipment) 
	{		
		DimensionType[] dimensions = {DimensionType.RELIABILITY, DimensionType.COMPETENCE, DimensionType.QUALITY, DimensionType.SHARED_VALUES};
		
		//reliability
		//is the shipment overdue?
		int runtime = (shipment.getReceivedAt() - shipment.getOrderedAt());
		//runtime is at least 2 weeks: ordered at 1, processed at 2, delivered at 3 (when trust <0.6 see OrderAgent)
		
		shipment.setShipmentQuality(1-shipment.getfailurePercentage());
		
		//Map<TrustDimension, Double> orderFulfillments = new HashMap<TrustDimension, Double>();
		
		Trust trust = trustStorage.get(shipment.getDeliveryAgent());
		
		KPI Kpi = new KPI(shipment, this.trustStorage.get(shipment.getDeliveryAgent()));
		
		double summedDimensionValues = 0;
		
		for (DimensionType dimensionType : dimensions) {
			//System.out.println("---------Dimension " + dimensionType + " ---------");
			
			Map<DimensionType, Double> dimensionRating = supplyChainMember.getTrustDimensionRatings();
			
			double rating = dimensionRating.get(dimensionType);
			
			//System.out.println("Dimension rating: " + rating);
			
			double kpiValue = Kpi.getKPIForDimension(dimensionType);
			
			//System.out.println("Dimension kpiValue: " + kpiValue);
			
			double updatedDimensionValue = (kpiValue - rating) * rating;
			
			//System.out.println("updatedDimensionValue: " + updatedDimensionValue);
			
			summedDimensionValues+=updatedDimensionValue;
			//orderFulfillments.put(trust.getDimension(DimensionType.RELIABILITY), kpiValue);
			
			
		}
		
//		System.out.println("Summed up values: " + summedDimensionValues);
		
//		System.out.println("Old trust Value is :" + trust.getUnifiedTrustValue());
		
		
		double _oldtValue = trust.getUnifiedTrustValue();
		
		double _tValue = _oldtValue * (1 + summedDimensionValues);
		//this.tValue = ((1- this.learningRate) * trust.getUnifiedTrustValue()) + (this.learningRate * _tValue);
		
		//System.out.println("_tValue: " + _tValue);
		
		//System.out.println("tValue: " + tValue);
		
		double _newtValue = ((1- this.learningRate) * trust.getUnifiedTrustValue()) + (this.learningRate * _tValue);

		trust.setUnifiedTrustValue(_newtValue);
		
		//determine if trust update was positive or negative, necessary for competence dimension
		
		
		if (_oldtValue > _newtValue) {
			//negative
			System.out.println("Trust update is negative [from "+ _oldtValue + " to " + _newtValue +  "]");
		} else {
			System.out.println("Trust update is positive [from "+ _oldtValue + " to " + _newtValue +  "]");
		}
		
		
	}
	
	
	/*
	 * GETTERS
	 */
	public double getTrustValue(DeliveryAgent delivery_agent) 
	{	
		
		//indirect trust
		int runs =0;
		double trustvalue=0; 
		for (Trust trust:IndirectTrustAgent.getTrustValue(this, delivery_agent)){
			trustvalue+=trust.getUnifiedTrustValue();
			runs++;
		}
		if(runs>0){
			trustvalue=trustvalue/runs;
			return (this.trustStorage.get(delivery_agent).getUnifiedTrustValue()*0.5+trustvalue*0.5);		}
		else{
			//whole trust value
			return this.trustStorage.get(delivery_agent).getUnifiedTrustValue();
		}
	} 
	
	/*public double getTrustValue(DeliveryAgent delivery_agent,int i) 
	{
		//hier muss der trust wert zurueclgegeben werden.
		return(1);
	} 
	*/
	
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