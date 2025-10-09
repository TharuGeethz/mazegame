package mazegame.entity;

/**
 * A shop location where players can sell and buy equipment. Extends a normal
 * location but marks itself as a shop.
 */
public class Blacksmith extends Location {

	// construct a blacksmith with description and label
	public Blacksmith(String description, String label) {
		super(description, label);
	}

	// formatted view of the shop information plus base location details
	public String toString() {
		return "**********\n" + "\nThis is a shop!" + "\n**********\n" + super.toString();
	}
}
