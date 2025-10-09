package mazegame.entity.item;

import mazegame.entity.Dice;

/**
 * Represents a healing potion that restores the player's health by a random
 * amount.
 */
public class HealingPotion {

	private String label; // potion name
	private String description; // potion description
	private static final String HEALING_POWER = "2d6"; // roll 2 six-sided dice and restore health up to (2–12 points)

	// construct healing potion with label and description
	public HealingPotion(String label, String description) {
		this.label = label;
		this.description = description;
	}

	// get potion label
	public String getLabel() {
		return label;
	}

	// set potion label
	public void setLabel(String label) {
		this.label = label;
	}

	// get potion description
	public String getDescription() {
		return description;
	}

	// set potion description
	public void setDescription(String description) {
		this.description = description;
	}

	// perform healing roll and return restored hit points
	public int heal() {
		return Dice.roll(HEALING_POWER);
	}
}
