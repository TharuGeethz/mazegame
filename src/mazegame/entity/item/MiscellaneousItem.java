package mazegame.entity.item;

/**
 * Represents a miscellaneous item that does not belong to a specific equipment
 * type. Used for objects like keys, banners and other quest related items.
 */
public class MiscellaneousItem extends Item {

	// construct miscellaneous item with label, value and weight
	public MiscellaneousItem(String label, int value, int weight) {
		super(label, value, weight);
	}
}
