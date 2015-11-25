package simulation_implement;

import simulation_interface.ForecastAgenti;

public class ForecastAgent implements ForecastAgenti{

	@Override
	public double calculateDemand() {
		// Hier muss wieder unterschieden werden/ausgearbeitet werden siehe unten
		return 10;
	}
	//unterscheidung durch überladen optimal?
	public double calculateDemand(double shortage){
		return (10+shortage);
	}

}
