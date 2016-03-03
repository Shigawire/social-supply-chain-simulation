package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import repast.simphony.random.RandomHelper;
import repast.simphony.systemdynamics.translator.InformationManagers;
import SimulationSetups.TrustSetter;
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
	private ArrayList<DeliveryAgent> deliveryAgents;
	
	//Store a specific Trust Object for every Delivery Agent
	private Map<DeliveryAgent, Trust> trustStorage = new HashMap<DeliveryAgent, Trust>();
	
	// When do we classify a shipment as overdue? (After how many ticks)
	private double ShipmentRuntimeOverdueThreshold = 2;

	// Learning rate for the trust assessment
	private double learningRate = 0.6;
	
	public TrustAgent(ArrayList<DeliveryAgent> deliveryAgents, Map<DimensionType, Double> dimensionRatings, SupplyChainMember supplyChainMember)
	{
		this.deliveryAgents = deliveryAgents;
		this.supplyChainMember = supplyChainMember;
		
		// Let's build up a trust Object for every deliveryAgent, resp. every Supply Chain member we might deal with
		for (DeliveryAgent deliveryAgent : this.deliveryAgents) 
		{
			// Initialize Trust Dimensions with the passed in Dimension Ratings...
			ReliabilityDimension reliability = new ReliabilityDimension(dimensionRatings.get(DimensionType.RELIABILITY));
			CompetenceDimension competence = new CompetenceDimension(dimensionRatings.get(DimensionType.COMPETENCE));
			QualityDimension quality = new QualityDimension(dimensionRatings.get(DimensionType.QUALITY));
			SharedValuesDimension sharedValues = new SharedValuesDimension(dimensionRatings.get(DimensionType.SHARED_VALUES));
			
			// create a Trust Object based on the four Dimensions
			Trust trust = new Trust(reliability, competence, quality, sharedValues);
			
			// store the Trust Object
			trustStorage.put(deliveryAgent, trust);
		}
	}

	// Inspect each shipment individually, based on this "inspection" the trust values of the supplier will adapt
	public void inspectShipment(OrderAgent orderAgent, Order shipment) 
	{		
		// We had to introduce some dirty hack to send out zero qty. orders.. Ignore these here.
		// TODO fix if possible?
		if (shipment.getQuantity() == 0) return;
		
		// Never rate shipments that are not fully processed yet. This basically means we would inspect partial shipments. We don't want to do that.
		if (!shipment.getProcessed()) return;

		DimensionType[] dimensions = {DimensionType.RELIABILITY, DimensionType.COMPETENCE, DimensionType.QUALITY, DimensionType.SHARED_VALUES};
		
		// runtime is at least 2 weeks: ordered at 1, processed at 2, delivered at 3 (when trust <0.6 see OrderAgent)
		
		//shipment quality is  1 - the failure percentage - remember that shipment is just an Order object that is called shipment...
		shipment.setShipmentQuality(1 - shipment.getfailurePercentage());
		
		//get the specific trust value for the delivery agent of the shipment
		Trust trust = trustStorage.get(shipment.getDeliveryAgent());
		
		//create a KPI Object for each shipment
		KPI Kpi = new KPI(shipment, this.trustStorage.get(shipment.getDeliveryAgent()));
		
		double summedDimensionValues = 0;
		
		// System.out.println("Shipment runtime: " + runtime);
		
		// Question the KPI Object regarding the specific order performance for each dimension
		for (DimensionType dimensionType : dimensions) 
		{
			//System.out.println("---------Dimension " + dimensionType + " ---------");
			
			//Map<DimensionType, Double> dimensionRating = supplyChainMember.getTrustDimensionRatings();
			
			//double rating = dimensionRating.get(dimensionType);
			
			// System.out.println("Dimension rating: " + rating);
			
			double rating = trust.getDimension(dimensionType).getDimensionRating();
			
			
			double kpiValue = Kpi.getKPIForDimension(dimensionType);
			
			//System.out.println("Dimension kpiValue: " + kpiValue);
			
			double updatedDimensionValue = (kpiValue - rating) * rating;
			
			//System.out.println("updatedDimensionValue: " + updatedDimensionValue);
			
			summedDimensionValues += updatedDimensionValue;
			//orderFulfillments.put(trust.getDimension(DimensionType.RELIABILITY), kpiValue);
			
			
		}
		
		//System.out.println("####################");
		
		//System.out.println("Summed up values: " + summedDimensionValues);
		
		//System.out.println("Old trust Value is :" + trust.getUnifiedTrustValue());
		
		//This is the actual trust calculation. Check the documentation for further details.
		double oldtValue = trust.getUnifiedTrustValue();
		
		double tValue = oldtValue * (1 + summedDimensionValues);
		//this.tValue = ((1- this.learningRate) * trust.getUnifiedTrustValue()) + (this.learningRate * _tValue);
		
		//System.out.println("_tValue: " + _tValue);
		
		//System.out.println("tValue: " + tValue);
		
		double newtValue = ((1 - this.learningRate) * trust.getUnifiedTrustValue()) + (this.learningRate * tValue);

		//if the calculated trust is above 1, stop at 1. Again, for explanation please check the docs.
		if (newtValue > 1) newtValue = 1;
		trust.setUnifiedTrustValue(newtValue);
		
		//determine if trust update was positive or negative, necessary for competence dimension
		
		if (oldtValue > newtValue) {
			//negative
			//System.out.println("Trust update is negative [from "+ _oldtValue + " to " + _newtValue +  "]");
		} else {
			//System.out.println("Trust update is positive [from "+ _oldtValue + " to " + _newtValue +  "]");
		}
		
		
	}
	
	
	/*
	 * GETTERS
	 */
	public double getTrustValue(DeliveryAgent deliveryAgent) 
	{	
		TrustSetter s = TrustSetter.getInstance();
		if (s.getIndirectTrustIndegrated()) {
			//indirect trust
			int runs = 0;
			double trustValue = 0; 
			for (Trust trust : IndirectTrustAgent.getTrustValue(this, deliveryAgent))
			{
				trustValue += trust.getUnifiedTrustValue();
				runs++;
			}
			
			if (runs > 0) {
				trustValue = trustValue / runs;
				return (this.trustStorage.get(deliveryAgent).getUnifiedTrustValue() *(1-supplyChainMember.getProfile().getIndirectTrust()) + trustValue *supplyChainMember.getProfile().getIndirectTrust() );		
			} else {
				// whole trust value
				return this.trustStorage.get(deliveryAgent).getUnifiedTrustValue();
			}
		}
		else return this.trustStorage.get(deliveryAgent).getUnifiedTrustValue();
	} 
	
	
	public Trust getTrustObject(DeliveryAgent deliveryAgent)
	{
		return this.trustStorage.get(deliveryAgent);
	}
		

	public SupplyChainMember getParent() 
	{
		return supplyChainMember;
	}
}