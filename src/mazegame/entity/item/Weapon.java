package mazegame.entity.item;

/**
 * Represents a weapon that can be equipped by a character and use in combat.
 * Each weapon defines its damage in dice notation ("1d8", "2d6").
 */
public class Weapon extends Item {

	private final String damage; // damage in dice notation

	// construct weapon with label, value, weight and damage
	public Weapon(String label, int value, int weight, String damage) {
		super(label, value, weight);
		this.damage = damage;
	}

	// get weapon damage string
	public String getDamage() {
		return damage;
	}
}
