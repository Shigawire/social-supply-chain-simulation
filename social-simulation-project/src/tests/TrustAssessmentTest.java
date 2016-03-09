package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.DefaultScheduleRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.Schedule;
import social_simulation_project.BWeffectMeasurer;
import actors.Customer;
import actors.Distributor;
import actors.Manufacturer;
import actors.Retailer;
import actors.SellingActor;
import actors.Wholesaler;
import artefacts.Order;
import artefacts.Profile;
import artefacts.trust.CompetenceDimension;
import artefacts.trust.DimensionType;
import artefacts.trust.KPI;
import artefacts.trust.QualityDimension;
import artefacts.trust.ReliabilityDimension;
import artefacts.trust.SharedValuesDimension;
import artefacts.trust.Trust;

public class TrustAssessmentTest 
{
	
	private Context<Object> context;
	private Schedule schedule;
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
    public void setUp() throws Exception 
	{
		this.schedule = new Schedule();
		RunEnvironment.init(this.schedule, new DefaultScheduleRunner(), null, false);
		this.context = new DefaultContext<Object>();
		RunState.init().setMasterContext(this.context);
    }

	@After
	public void tearDown() throws Exception 
	{
	}

	
	@Test
	public void assertTrustUpdate() 
	{
		// In this case we create a customer and retailer - let the simulation run until an order has been made and received
		// and then make sure that the the shipment and trust update match our expectations
		
		// A profile that is used for both supply chain members
		
		double DIMENSION_RATING_RELIABILITY = 0.7;
		double DIMENSION_RATING_QUALITY = 0.2;
		double DIMENSION_RATING_COMPETENCE = 0.05;
		double DIMENSION_RATING_SHARED_VALUES = 0.05;
		
		Profile profile = new Profile(0.44, 0.415, 0.145, 
				DIMENSION_RATING_COMPETENCE, 
				DIMENSION_RATING_RELIABILITY, 
				DIMENSION_RATING_QUALITY, 
				DIMENSION_RATING_SHARED_VALUES,
			0.67, 0.0, 0.0);
		
		// unfortunately, for the entire supply chain to work, we have to initialize ALL members.
		// However, only the customer and the retailer will be advanced in the simulation
		Manufacturer manufacturer1 = new Manufacturer(10, 0, 100, profile);
		ArrayList<SellingActor> manufacturerList = new ArrayList<SellingActor>();
		manufacturerList.add(manufacturer1);
		
		Distributor distributor1 = new Distributor(manufacturerList, 15, 11, 50, profile);
		
		ArrayList<SellingActor> distributorList = new ArrayList<SellingActor>();
		distributorList.add(distributor1);

		Wholesaler wholesaler1 = new Wholesaler(distributorList, 12, 11, 40, profile);

		ArrayList<SellingActor> wholesalerList = new ArrayList<SellingActor>();
		wholesalerList.add(wholesaler1);	
		
		//Give the retailer an initial inventory of 200 - this ensures there is enough stock to fulfill any upcoming demand instantly.
		Retailer retailer1 = new Retailer(wholesalerList, 200, 200, 10,  profile);
		
		ArrayList<SellingActor> retailerList = new ArrayList<SellingActor>();
		retailerList.add(retailer1);
		
		Customer customer1 = new Customer(retailerList, 10, 15, profile);
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer1);
		
		//initialize the Bullwhip Effect Measurer. Not used during the tests - but required for the simulation to advance.
		BWeffectMeasurer.getMeasurer().setTheDummys(
			customer1,
			retailer1, 
			wholesaler1, 
			distributor1);
		
		/**
		 * This test is specifically designed to solely test the trust between the customer and the retailer, nothing more.
		 * 
		 */
		
		//get the trust Object from the customer
		//this object holds the trust dimension regarding the retailer
		
		Trust trust = customer1.getTrustAgent().getTrustObject(retailer1.getDeliveryAgent());
		
		//make sure the initial trust value is 0.5
		assertEquals("Initial Trust Value is not correct.", 0.5, trust.getUnifiedTrustValue(), 0);
		
		/**
		 * Next we make sure the dimension ratings fit, i.e. the profile set up is working correctly with the setup of the trust dimension
		 */
		//get all four trust dimensions and make sure their rating is correct
		
		//----Reliability
		ReliabilityDimension reliability = (ReliabilityDimension)trust.getDimension(DimensionType.RELIABILITY);
		
		assertEquals("The reliability dimension rating is not correct.", DIMENSION_RATING_RELIABILITY, reliability.getDimensionRating(), 0);
		
		//----Quality
		QualityDimension quality = (QualityDimension)trust.getDimension(DimensionType.QUALITY);
		
		assertEquals("The quality dimension rating is not correct.", DIMENSION_RATING_QUALITY, quality.getDimensionRating(), 0);
		

		CompetenceDimension competence = (CompetenceDimension)trust.getDimension(DimensionType.COMPETENCE);
		//----Competence
		assertEquals("The competence dimension rating is not correct.", DIMENSION_RATING_COMPETENCE, competence.getDimensionRating(), 0);
		
		
		SharedValuesDimension sharedValues = (SharedValuesDimension)trust.getDimension(DimensionType.SHARED_VALUES);
		//----Shared Values
		assertEquals("The shared values dimension rating is not correct.", DIMENSION_RATING_SHARED_VALUES, sharedValues.getDimensionRating(), 0);
		
		
		/**
		 * Now we create an order object at the customer's level, and send it to the retailer.
		 * The large available inventory of the retailer makes sure that it will be processed immediately - i.e. that the retailer does not 
		 * have to order himself at upstream members.
		 * 
		 * We want to make sure that the order will be returned immediately, thus creating the best possible trust update.
		 */
		
		/**
		 * We basically hook into the customer's ordering process - we completely disregard any forecast information here.
		 * 
		 * This all happens before the simulation has actually begun.
		 */
		
		//Craft an order about 100 items and assign the customer's order agent
		Order TrustAssessmentOrder = new Order(100, customer1.getOrderAgent());
				
		//set a delivery duration of two ticks
		TrustAssessmentOrder.setExpectedDeliveryDuration(2);
		
		//Submit the order to the retailer
		retailer1.getDeliveryAgent().receiveOrder(TrustAssessmentOrder);
		
		//advance the simulation by two ticks
		for (int i = 1; i <= 2; i++) 
		{			
			this.schedule.schedule(customer1);
			this.schedule.schedule(retailer1);

	    	this.schedule.execute();
		}
		
		//By now the shipment should have been processed by the retailer and returned to the customer as fulfilled shipment
		//This means the customer inventory has incrased by 100 - due to the consumption at the beginning of tick #1 and #2 the 
		//incoming has depleted by 2*10 items - thus we expect the customer to have 90 items in the "incoming" inventory (remember the 10 items from the starting value)
		
		//Due to quality differences - i.e. broken orders on shipment - this value may vary between 88 and 90
		
		assertEquals("The customer inventory has increased by ~90", 90, customer1.getInventoryAgent().getIncomingInventoryLevel(), 2);
		
		//If we reach this state this means that the customer has done a trust assessment of the previously received order.
		//We assume that the trust value has changed - i.e. is not 0.5 anymore.
		assertTrue("Trust Value is not changed.", (trust.getUnifiedTrustValue() != 0.5));
		
		
		//Time to test some KPIs.
		KPI testOrderKPI = new KPI(TrustAssessmentOrder, trust);
		
		double reliabilityKPI = testOrderKPI.getKPIForDimension(DimensionType.RELIABILITY);
		
		//We made sure that the order can be processed instantly, which means that we expect a 100% KPI at the reliability.
		assertEquals("The reliability KPI is not correct.", 1, reliabilityKPI, 0);
		
		
		double qualityKPI = testOrderKPI.getKPIForDimension(DimensionType.QUALITY);
		
		//The shipment Quality KPI is just the shipment quality, so we expect it to be the same.
		assertEquals("The qualityKPI equals the shipment quality", TrustAssessmentOrder.getShipmentQuality(), qualityKPI, 0);
		
		//according to orderObject.setShipmentQuality(...) the quality should be between 98% and 100%
		assertEquals("The qualityKPI KPI is not correct.", 1, qualityKPI, 0.02);
		
		double competenceKPI = trust.getCurrentCompetenceValue();
		
		//expect starting value of 0.5 + 0.1
		assertEquals("The competence KPI is not correct.", 0.6, competenceKPI, 0);
		
		double sharedValuesKPI = testOrderKPI.getKPIForDimension(DimensionType.SHARED_VALUES);
		
		assertEquals("The sharedValues KPI is not correct.", 1, sharedValuesKPI, 0);
		
		
		//Let's do some happy trust assessment with our KPIs. - the "given" values that are checked against are manually created.
		//You may use the included excel spread sheet about the trust assessment to simplify test calculations
		
		double updatedReliabilityValue = (reliabilityKPI - reliability.getDimensionRating()) * reliability.getDimensionRating();
		
		assertEquals("The updated reliability value is not correct.", 0.21, (double)Math.round(updatedReliabilityValue * 100d) / 100d, 0);
		
		double updatedQualityValue = (qualityKPI - quality.getDimensionRating()) * quality.getDimensionRating();
		
		assertEquals("The updated quality value is not correct.", 0.16, (double)Math.round(updatedQualityValue * 100d) / 100d, 0.05);
		
		double updatedCompetenceValue = (competenceKPI - competence.getDimensionRating()) * competence.getDimensionRating();
		
		assertEquals("The updated competence value is not correct.", 0.0275, (double)Math.round(updatedCompetenceValue * 100000d) / 100000d, 0);
		
		double updatedsharedValuesValue = (sharedValuesKPI - sharedValues.getDimensionRating()) * sharedValues.getDimensionRating();
		
		assertEquals("The updated shared values value is not correct.", 0.05, (double)Math.round(updatedsharedValuesValue * 100d) / 100d, 0);
		
		double summedUpValues = updatedReliabilityValue + updatedQualityValue + updatedCompetenceValue + updatedsharedValuesValue;
		
		double oldtValue = 0.5;
		
		//trust.getUnifiedTrustValue() can not be used here, as the simulation has advanced already and the trust assessment has already been completed
		//thus, the "new" trust Value has already been set.
		//But we know that the current trust value is 0.5 at tick 1, so we can safely assume this constant here.
		double currentTrustValue = 0.5;
		
		//calculate the new trust value
		double _tValue = oldtValue * (1 + summedUpValues);
		// 0.6 = learning rate
		double newtValue = ((1 - 0.6) * currentTrustValue) + (0.6 * _tValue);
		
		assertEquals("The newly computed trust value is not correct.", 0.632, newtValue, 0.01);
		
		
	}
}