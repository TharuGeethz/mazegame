package mazegame.entity;


import java.util.HashMap;

import mazegame.entity.item.Weapon;

public class WeaponFactory {

	private static WeaponFactory instance;
	private HashMap<String, Weapon> weapons;

	private WeaponFactory(HashMap<String, Weapon> weapons) {
		this.weapons = weapons;
	}

	public static WeaponFactory getInstance() {
		if (instance == null) {
			instance = new WeaponFactory(new HashMap<String, Weapon>());
		}
		return instance;
	}

	public boolean addWeapon(Weapon weapon) {
		weapons.put(weapon.getLabel(), weapon);
		return true;
	}

	public Weapon getWeapon(String weaponLabel) {
		return weapons.get(weaponLabel);
	}

	public HashMap<String, Weapon> getAllWeapons() {
		return weapons;
	}

}
