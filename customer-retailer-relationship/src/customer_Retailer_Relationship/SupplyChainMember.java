package customer_Retailer_Relationship;

import java.util.ArrayList;

public abstract class SupplyChainMember {
	protected String id;
	protected TrustAgent trustAgent;
	protected InventoryAgent inventoryAgent;
	protected ForecastAgent forecastAgent;
	
	public SupplyChainMember() {
		this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
	}

}
