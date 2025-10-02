package mazegame.entity.item;

import mazegame.entity.Dice;

public class HealingPotion {

	private String label;
	private String description;

	private static final String HEALING_POWER = "2d6"; // roll 2 six-sided dice and restore health up to (2–12 points)

	public HealingPotion(String label, String description) {
		this.label = label;
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int heal() {
		return Dice.roll(HEALING_POWER);

	}

}