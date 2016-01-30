package tests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.DefaultScheduleRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import artefacts.trust.CompetenceDimension;
import artefacts.trust.QualityDimension;
import artefacts.trust.ReliabilityDimension;
import artefacts.trust.SharedValuesDimension;
import artefacts.trust.Trust;

public class TrustAssessment {
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		Schedule schedule = new Schedule();
		RunEnvironment.init(schedule, new DefaultScheduleRunner(), null, true);
		Context<Object> context = new DefaultContext<Object>();
		RunState.init().setMasterContext(context);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void ConstantShouldFireOnEvenTicks() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();

        for (int i = 0; i < 200; ++i) {
        	RunEnvironment.getInstance().getCurrentSchedule().execute();
        	
        	//schedule.execute();    // geht nicht.
        }
        
        assertEquals(0, schedule.getTickCount(), 0);
		schedule.execute();
		assertEquals(schedule.getTickCount(),2,0);
		schedule.execute();
		assertEquals(schedule.getTickCount(),3,0);
		//assertEquals(admissions.getFacility().getOccupancy(),3);
		
		System.out.println(schedule.getTickCount());
	}
	

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
	
	@Test
	public void assertTrustUpdate() {
		
		ReliabilityDimension reliability = new ReliabilityDimension(0.25, 0.5);
		CompetenceDimension competence = new CompetenceDimension(0.25, 0.5);
		QualityDimension quality = new QualityDimension(0.25, 0.5);
		SharedValuesDimension shared_values = new SharedValuesDimension(0.25, 0.5);
		Trust trust = new Trust(reliability, competence, quality, shared_values);
		
		assertEquals("Initial Trust Value is not correct.", 0.5, trust.getUnifiedTrustValue(), 0);
	}

}
