package mazegame.entity;

import mazegame.entity.item.Item;
import mazegame.entity.utility.WeightLimit;

/**
 * Represents a player inventory with a carrying weight limit based on the
 * character’s strength attribute.
 */
public class FiniteInventory extends Inventory {
	private double weightLimit; // maximum carrying capacity

	// create inventory with a limit derived from strength
	public FiniteInventory(int strength) {
		super();
		this.setStrength(strength);
	}

	// set strength and update weight limit accordingly
	public void setStrength(int strength) {
		weightLimit = (double) WeightLimit.getInstance().getModifier(strength);
	}

	// calculate total current carried weight (items + gold)
	public double getWeight() {
		double currentWeight = 0;
		for (Item theItem : this.getItemList().values()) {
			currentWeight += theItem.getWeight();
		}
		currentWeight += this.getGold().getWeight();
		return currentWeight;
	}

	// add item only if under weight limit
	public boolean addItem(Item theItem) {
		if (weightLimit > theItem.getWeight() + getWeight()) {
			return super.addItem(theItem);
		}
		return false;
	}
}
