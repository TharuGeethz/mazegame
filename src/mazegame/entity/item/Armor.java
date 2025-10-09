package mazegame.entity.item;

/**
 * Represents a piece of armor that gives a defensive bonus to the wearer.
 */
public class Armor extends Item {
	private final int bonus; // defense bonus value

	// construct armor with label, value, weight and bonus
	public Armor(String label, int value, int weight, int bonus) {
		super(label, value, weight);
		this.bonus = bonus;
	}

	// get armor bonus value
	public int getBonus() {
		return bonus;
	}
}
