package mazegame.entity;

import java.util.HashMap;
import java.util.Map;

import mazegame.entity.item.Armor;
import mazegame.entity.item.Item;
import mazegame.entity.item.Shield;
import mazegame.entity.item.Weapon;
import mazegame.entity.utility.AgilityTable;

public class Character {
	private int agility;
	private int lifePoints;
	private String name;
	private int strength;

	private Inventory inventory;
	private Map<String, Item> wearingItems;

	public Character(String name) {
		inventory = new Inventory();
		wearingItems = new HashMap<>();
		this.name = name;
	}

	public Character(String name, int strength, int agility, int lifePoints) {
		inventory = new Inventory();
		wearingItems = new HashMap<>(); // can have 1 weapon, shield and armor at a time
		this.name = name;
		this.strength = strength;
		this.agility = agility;
		this.lifePoints = lifePoints;

	}

	public int getAgility() {
		return agility;
	}

	public void setAgility(int agility) {
		this.agility = agility;
	}

	public int getLifePoints() {
		return lifePoints;
	}

	public void setLifePoints(int lifePoints) {
		this.lifePoints = lifePoints;
	}

	public String getName() {
		return name;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Map<String, Item> getWearingItems() {
		return wearingItems;
	}

	public Weapon getEquippedWeapon() {
		return wearingItems.values().stream().filter(i -> i instanceof Weapon).map(i -> (Weapon) i).findFirst()
				.orElse(null);
	}

	public Armor getEquippedArmor() {
		return wearingItems.values().stream().filter(i -> i instanceof Armor).map(i -> (Armor) i).findFirst()
				.orElse(null);
	}

	public Shield getEquippedShield() {
		return wearingItems.values().stream().filter(i -> i instanceof Shield).map(i -> (Shield) i).findFirst()
				.orElse(null);
	}

	public void equipItem(String currentItemName) {
		Item item = inventory.findItem(currentItemName);
		inventory.removeItem(currentItemName); // Remove item from inventory first
		wearingItems.put(currentItemName, item);
	}

	public void unequipItem(String currentItemName) {
		Item item = wearingItems.get(currentItemName);
		wearingItems.remove(currentItemName); // Remove item from wearing items first
		inventory.addItem(item);
	}

	public boolean hasItem(String itemName) {
		return (wearingItems.containsKey(itemName) || inventory.hasItem(itemName));
	}

	public boolean isWearing(String itemName) {
		return (wearingItems.containsKey(itemName));
	}

	public int getItemsWeight() {
		int totalWeight = inventory.getWeight();
		for (Item item : wearingItems.values()) {
			totalWeight += item.getWeight();
		}

		return totalWeight;
	}

	public int getArmorClass() {
		return 10 + this.getBonusByType(Armor.class) + this.getBonusByType(Shield.class)
				+ AgilityTable.getInstance().getModifier(this.agility);
	}

	private <T extends Item> int getBonusByType(Class<T> type) {
		int totalBonus = 0;

		if (wearingItems != null && !wearingItems.isEmpty()) {
			totalBonus = wearingItems.values().stream().filter(type::isInstance).map(type::cast) // cast Item to
																									// Armor/Shield
					.mapToInt(item -> {
						if (item instanceof Armor) {
							return ((Armor) item).getBonus();
						} else if (item instanceof Shield) {
							return ((Shield) item).getBonus();
						} else {
							return 0;
						}
					}).sum();
		}

		return totalBonus;
	}

}
