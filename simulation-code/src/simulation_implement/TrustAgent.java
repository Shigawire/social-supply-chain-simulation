package simulation_implement;

import repast.simphony.random.RandomHelper;
import simulation_interface.ChainLink;
import simulation_interface.TrustAgenti;

public class TrustAgent implements TrustAgenti{
	//momentan nur Wahl zwischen zewi sailorn spaeter mit array oder so
	//eventuell hier auch die ChainLink die garantiert ne order funktion hat
	private ChainLink sailor_ein;
	private ChainLink sailor_zwei;
	private ChainLink momentan;
	private int trust_ein=0;
	private int trust_zwei=0;
	public TrustAgent(ChainLink erster,ChainLink zweiter){
		sailor_ein=erster;
		sailor_zwei=zweiter;
		trust_ein=RandomHelper.nextIntFromTo(5, 20);
		trust_zwei=RandomHelper.nextIntFromTo(5, 20);
	}
	@Override
	public ChainLink chooseSailor() {
		if(trust_ein>=trust_zwei){
			momentan=sailor_ein;
			return sailor_ein;
		}
		else{
			momentan=sailor_zwei;
			return sailor_zwei;
		}
	}

	@Override
	public void updateTrust(int someValue) {
		if(momentan==sailor_ein){
			trust_ein=someValue;
		}
		else{
			trust_zwei=someValue;
		}
		
	}

}
