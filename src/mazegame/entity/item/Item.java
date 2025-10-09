package mazegame.entity.item;

/**
 * Abstract base class for all item types in the game such as weapons, armor,
 * shields, and potions. Defines shared properties like label, value, weight,
 * and description.
 */
public abstract class Item {

	private int value; // gold value of the item
	private double weight; // item weight
	private final String label; // item name
	private String description; // item description

	// construct with only label and description
	public Item(String label, String description) {
		this.label = label;
		this.description = description;
	}

	// construct with label, value and weight
	public Item(String label, int value, int weight) {
		this.label = label;
		this.value = value;
		this.weight = weight;
	}

	// get item label
	public String getLabel() {
		return label;
	}

	// get item value
	public int getValue() {
		return value;
	}

	// get item weight
	public double getWeight() {
		return weight;
	}

	// get item description
	public String getDescription() {
		return description;
	}

	// set item value
	public void setValue(int value) {
		this.value = value;
	}
}
