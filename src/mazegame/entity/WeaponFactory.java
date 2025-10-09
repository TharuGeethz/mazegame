package mazegame.entity;

import java.util.HashMap;
import mazegame.entity.item.Weapon;

/**
 * Singleton factory class for managing and storing all weapon types in the
 * game.
 */
public class WeaponFactory {

	private static WeaponFactory instance; // single shared instance
	private HashMap<String, Weapon> weapons; // stores all weapons by label

	// private constructor to enforce singleton pattern
	private WeaponFactory(HashMap<String, Weapon> weapons) {
		this.weapons = weapons;
	}

	// get or create the single instance
	public static WeaponFactory getInstance() {
		if (instance == null) { // create if not initialized yet
			instance = new WeaponFactory(new HashMap<String, Weapon>());
		}
		return instance;
	}

	// add a new weapon to the collection
	public boolean addWeapon(Weapon weapon) {
		weapons.put(weapon.getLabel(), weapon); // key is weapon label
		return true;
	}

	// retrieve a weapon by name
	public Weapon getWeapon(String weaponLabel) {
		return weapons.get(weaponLabel);
	}

	// get all stored weapons
	public HashMap<String, Weapon> getAllWeapons() {
		return weapons;
	}
}
