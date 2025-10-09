package mazegame.entity;

import java.util.HashMap;
import mazegame.entity.item.Shield;

/**
 * Singleton factory class for managing all shield types in the game.
 */
public class ShieldFactory {

	private static ShieldFactory instance; // single shared instance
	private final HashMap<String, Shield> shields; // stores all shields by label

	// private constructor to enforce singleton pattern
	private ShieldFactory(HashMap<String, Shield> shields) {
		this.shields = shields;
	}

	// get or create the single instance
	public static ShieldFactory getInstance() {
		if (instance == null) { // create if not initialized yet
			instance = new ShieldFactory(new HashMap<String, Shield>());
		}
		return instance;
	}

	// add a new shield to the collection
	public boolean addShield(Shield shield) {
		shields.put(shield.getLabel(), shield); // key is shield label
		return true;
	}

	// retrieve a shield by name
	public Shield getShield(String shieldLabel) {
		return shields.get(shieldLabel);
	}

	// get all stored shields
	public HashMap<String, Shield> getAllShields() {
		return shields;
	}
}
