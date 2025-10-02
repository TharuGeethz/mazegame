package mazegame.entity;

import java.util.HashMap;

import mazegame.entity.item.Armor;

public class ArmorFactory {

	private static ArmorFactory instance;
	private HashMap<String, Armor> armors;

	private ArmorFactory(HashMap<String, Armor> armors) {
		this.armors = armors;
	}

	public static ArmorFactory getInstance() {
		if (instance == null) {
			instance = new ArmorFactory(new HashMap<String, Armor>());
		}
		return instance;
	}

	public boolean addArmor(Armor armor) {
		armors.put(armor.getLabel(), armor);
		return true;
	}

	public Armor getArmor(String armorLabel) {
		return armors.get(armorLabel);
	}

	public HashMap<String, Armor> getAllArmors() {
		return armors;
	}

}
