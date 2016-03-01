package artefacts;

/**
 * The Inventory Object is supposed to represent incoming and outgoing inventories.
 * For the sake of clean code we're OOP-focused - an object a day keeps the headache away.
 */
public class Inventory {

	private int level;
	
	public Inventory(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
