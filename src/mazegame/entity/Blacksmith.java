package mazegame.entity;

public class Blacksmith extends Location {

	public Blacksmith(String description, String label) {
		super(description, label);
	}

	public String toString() {
		return super.toString() + "This is a shop!";
	}
}
