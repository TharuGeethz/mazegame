package mazegame.entity;

import java.util.HashMap;
import java.util.Map;

import mazegame.entity.item.Armor;
import mazegame.entity.item.Item;
import mazegame.entity.item.Shield;
import mazegame.entity.item.Weapon;
import mazegame.entity.utility.AgilityTable;

/**
 * Base character model with inventory and equipped items.
 */
public class Character {
	private int agility; // agility score
	private int lifePoints; // current hit points
	private String name; // character name
	private int strength; // strength score

	private Inventory inventory; // inventory of items, potions and gold
	private Map<String, Item> wearingItems; // currently equipped items by label

	// construct with name only
	public Character(String name) {
		inventory = new Inventory();
		wearingItems = new HashMap<>();
		this.name = name;
	}

	// construct with full stats
	public Character(String name, int strength, int agility, int lifePoints) {
		inventory = new Inventory();
		wearingItems = new HashMap<>(); // can have 1 weapon, shield and armor at a time
		this.name = name;
		this.strength = strength;
		this.agility = agility;
		this.lifePoints = lifePoints;
	}

	// get agility
	public int getAgility() {
		return agility;
	}

	// set agility
	public void setAgility(int agility) {
		this.agility = agility;
	}

	// get life points
	public int getLifePoints() {
		return lifePoints;
	}

	// set life points
	public void setLifePoints(int lifePoints) {
		this.lifePoints = lifePoints;
	}

	// get name
	public String getName() {
		return name;
	}

	// get strength
	public int getStrength() {
		return strength;
	}

	// set strength
	public void setStrength(int strength) {
		this.strength = strength;
	}

	// get inventory
	public Inventory getInventory() {
		return inventory;
	}

	// set inventory
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	// get equipped map
	public Map<String, Item> getWearingItems() {
		return wearingItems;
	}

	// get equipped weapon if any
	public Weapon getEquippedWeapon() {
		return wearingItems.values().stream().filter(i -> i instanceof Weapon).map(i -> (Weapon) i).findFirst()
				.orElse(null);
	}

	// get equipped armor if any
	public Armor getEquippedArmor() {
		return wearingItems.values().stream().filter(i -> i instanceof Armor).map(i -> (Armor) i).findFirst()
				.orElse(null);
	}

	// get equipped shield if any
	public Shield getEquippedShield() {
		return wearingItems.values().stream().filter(i -> i instanceof Shield).map(i -> (Shield) i).findFirst()
				.orElse(null);
	}

	// equip an item by name
	public void equipItem(String currentItemName) {
		Item item = inventory.findItem(currentItemName); // lookup in inventory
		inventory.removeItem(currentItemName); // remove from inventory
		wearingItems.put(currentItemName, item); // place into equipped map
	}

	// unequip an item by name
	public void unequipItem(String currentItemName) {
		Item item = wearingItems.get(currentItemName); // get equipped item
		wearingItems.remove(currentItemName); // remove from equipped
		inventory.addItem(item); // return to inventory
	}

	// check if player owns item either equipped or in inventory
	public boolean hasItem(String itemName) {
		return (wearingItems.containsKey(itemName) || inventory.hasItem(itemName));
	}

	// check if item is currently worn
	public boolean isWearing(String itemName) {
		return (wearingItems.containsKey(itemName));
	}

	// compute armor class
	public int getArmorClass() {
		return 10 + this.getBonusByType(Armor.class) // armor bonus
				+ this.getBonusByType(Shield.class) // shield bonus
				+ AgilityTable.getInstance().getModifier(this.agility); // agility modifier
	}

	// aggregate total bonus for a given equipped type
	private <T extends Item> int getBonusByType(Class<T> type) {
		int totalBonus = 0;

		if (wearingItems != null && !wearingItems.isEmpty()) {
			totalBonus = wearingItems.values().stream().filter(type::isInstance) // only desired type
					.map(type::cast) // cast Item to Armor/Shield
					.mapToInt(item -> {
						if (item instanceof Armor) {
							return ((Armor) item).getBonus(); // armor bonus
						} else if (item instanceof Shield) {
							return ((Shield) item).getBonus(); // shield bonus
						} else {
							return 0; // other items do not add AC
						}
					}).sum();
		}

		return totalBonus;
	}
}
