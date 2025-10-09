package mazegame.entity;

import java.util.HashMap;
import mazegame.entity.item.Armor;

/**
 * Singleton factory class for creating and storing all armor types in the game.
 */
public class ArmorFactory {

	private static ArmorFactory instance; // single shared instance
	private final HashMap<String, Armor> armors; // stores all available armors by name

	// private constructor to enforce singleton pattern
	private ArmorFactory(HashMap<String, Armor> armors) {
		this.armors = armors;
	}

	// get or create the single instance
	public static ArmorFactory getInstance() {
		if (instance == null) { // create once if not yet initialized
			instance = new ArmorFactory(new HashMap<String, Armor>());
		}
		return instance;
	}

	// add a new armor to the factory
	public boolean addArmor(Armor armor) {
		armors.put(armor.getLabel(), armor); // key is armor label
		return true;
	}

	// retrieve an armor by name
	public Armor getArmor(String armorLabel) {
		return armors.get(armorLabel);
	}

	// get all stored armors
	public HashMap<String, Armor> getAllArmors() {
		return armors;
	}
}
