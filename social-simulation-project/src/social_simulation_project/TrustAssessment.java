package social_simulation_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import artefacts.trust.CompetenceDimension;
import artefacts.trust.DimensionType;
import artefacts.trust.QualityDimension;
import artefacts.trust.ReliabilityDimension;
import artefacts.trust.SharedValuesDimension;
import artefacts.trust.Trust;

public class TrustAssessment {

	@Test
	public void test() {
		fail("Not yet implemented");
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
