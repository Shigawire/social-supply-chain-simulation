package SimulationSetups;

public class TrustSetter 
{ 
    private static final TrustSetter obj = new TrustSetter(); 
    private boolean trustIntegrated;
    private boolean indirectTrustIntegrated;
    private boolean informationSharingIntegrated;
    
    private TrustSetter() 
    { 
    } 
    
  	/*
  	 * GETTERS
  	 */
    public static TrustSetter getInstance() 
    { 
      return obj; 
    } 
    
  	public boolean getTrustIntegrated()
  	{
		return trustIntegrated;
	}
  	
	public boolean getIndirectTrustIntegrated()
	{
		return indirectTrustIntegrated;
	}
	
	public boolean getInformationSharingIntegrated()
	{
		return informationSharingIntegrated;
	}
	
	/*
	 * SETTERS
	 */
	public void setTrustIntegrated(boolean trust)
	{
		trustIntegrated = trust;
	}
	
	public void setIndirectTrustIntegrated(boolean indirectTrust)
	{
		indirectTrustIntegrated = indirectTrust;
	}
	
	public void setInformationSharingIntegrated(boolean informationSharing)
	{
		informationSharingIntegrated = informationSharing;
	}
}