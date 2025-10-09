package mazegame.entity.item;

/**
 * Represents a shield item that adds a defensive bonus to the player's armor
 * class.
 */
public class Shield extends Item {
	private final int bonus; // defensive bonus value

	// construct shield with label, value, weight and bonus
	public Shield(String label, int value, int weight, int bonus) {
		super(label, value, weight);
		this.bonus = bonus;
	}

	// get shield bonus value
	public int getBonus() {
		return bonus;
	}
}
