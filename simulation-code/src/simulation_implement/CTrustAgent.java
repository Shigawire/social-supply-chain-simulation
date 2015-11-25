package simulation_implement;

import simulation_interface.ChainLink_simple;
import simulation_interface.TrustAgent;

public class CTrustAgent implements TrustAgent{
	private Retailer retailer_ein;
	private Retailer retailer_zwei;
	private Retailer momentan;
	private int trust_ein=0;
	private int trust_zwei=0;
	public CTrustAgent(Retailer erster,Retailer zweiter){
		retailer_ein=erster;
		retailer_zwei=zweiter;
	}
	@Override
	public ChainLink_simple chooseSailor() {
		if(trust_ein>=trust_zwei){
			momentan=retailer_ein;
			return retailer_ein;
		}
		else{
			momentan=retailer_zwei;
			return retailer_zwei;
		}
	}

	@Override
	public void updateTrust(int someValue) {
		if(momentan==retailer_ein){
			trust_ein=someValue;
		}
		else{
			trust_zwei=someValue;
		}
		
	}

}
