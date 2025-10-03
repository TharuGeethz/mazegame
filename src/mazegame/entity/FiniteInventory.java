package mazegame.entity;

import mazegame.entity.item.Item;
import mazegame.entity.utility.WeightLimit;

public class FiniteInventory extends Inventory {
	private double weightLimit;
	
	public FiniteInventory (int strength) {
		super ();
		this.setStrength(strength);
	}
	
	public void setStrength (int strength) {
		weightLimit = (double) WeightLimit.getInstance().getModifier(strength);
	}
	
	public double getWeight () {
		double currentWeight = 0;
		for (Item theItem: this.getItemList().values()) {
			currentWeight += theItem.getWeight();
		}
		return currentWeight;
	}
	
	public boolean addItem (Item theItem) {
		if (weightLimit > theItem.getWeight() + getWeight()) {
			return super.addItem(theItem);
		}
		return false;
	}

}
