package mazegame.entity;

import java.util.HashMap;
import java.util.List;

import mazegame.entity.item.Armor;
import mazegame.entity.item.HealingPotion;
import mazegame.entity.item.Item;
import mazegame.entity.item.MiscellaneousItem;
import mazegame.entity.item.Shield;
import mazegame.entity.item.Weapon;

/**
 * Represents a player's inventory in the maze game.
 */
public class Inventory {
	private Money gold; // player’s gold balance
	private HashMap<String, Item> itemList; // stores all normal items
	private HashMap<String, HealingPotion> healingPotionList; // stores all healing potions

	// constructor
	public Inventory() {
		gold = new Money();
		itemList = new HashMap<String, Item>();
		healingPotionList = new HashMap<String, HealingPotion>();
	}

	// add gold to inventory
	public void addMoney(int goldPieces) {
		gold.add(goldPieces);
	}

	// remove gold from inventory
	public boolean removeMoney(int goldPieces) {
		return gold.subtract(goldPieces);
	}

	// add single item
	public boolean addItem(Item theItem) {
		itemList.put(theItem.getLabel(), theItem);
		return true;
	}

	// add new potion
	public Inventory addPotion(HealingPotion potion) {
		healingPotionList.put(potion.getLabel(), potion);
		return this;
	}

	// add many items at once
	public void addItems(List<Item> items) {
		if (items == null || items.isEmpty())
			return;
		for (Item it : items) {
			addItem(it);
		}
	}

	// remove single item by name
	public Item removeItem(String itemName) {
		if (itemList.containsKey(itemName)) {
			return itemList.remove(itemName);
		} else {
			return null;
		}
	}

	// remove single potion by name
	public void removePotion(String potionName) {
		if (healingPotionList.containsKey(potionName)) {
			healingPotionList.remove(potionName);
		}
	}

	// get all items
	public HashMap<String, Item> getItemList() {
		return itemList;
	}

	// get all potions
	public HashMap<String, HealingPotion> getPotionList() {
		return healingPotionList;
	}

	// get current gold
	public Money getGold() {
		return gold;
	}

	// set gold
	public void setGold(Money gold) {
		this.gold = gold;
	}

	// find an item by its label
	public Item findItem(String itemLabel) {
		if (itemList.containsKey(itemLabel)) {
			return itemList.get(itemLabel);
		} else {
			return null;
		}
	}

	// get item names filtered by item type (Weapon, Armor, Shield)
	public <T extends Item> String availableItemsByType(Class<T> type) {
		StringBuilder returnMsg = new StringBuilder();
		itemList.values().stream().filter(type::isInstance) // only items of this type
				.map(Item::getLabel) // get names
				.forEach(label -> returnMsg.append("[").append(label).append("] "));
		return returnMsg.toString();
	}

	// check if item or potion exists
	public boolean hasItem(String name) {
		if (name == null)
			return false;
		return itemList.containsKey(name) || healingPotionList.containsKey(name);
	}

	// return formatted string of all inventory content
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		// Weapons
		String weapons = availableItemsByType(Weapon.class).trim();
		appendCategory(sb, "Weapons", weapons);

		// Armors
		String armors = availableItemsByType(Armor.class).trim();
		appendCategory(sb, "Armors", armors);

		// Shields
		String shields = availableItemsByType(Shield.class).trim();
		appendCategory(sb, "Shields", shields);

		// Potions
		String potions = formatPotionList();
		appendCategory(sb, "Potions", potions);

		// Gold
		String gold = this.gold.toString();
		sb.append(gold).append('\n');

		// Miscellaneous items (keys, banners, etc.)
		String misc = availableItemsByType(MiscellaneousItem.class).trim();
		appendCategory(sb, "Misc", misc);

		return sb.toString();
	}

	// helper method to format category
	private void appendCategory(StringBuilder sb, String title, String content) {
		if (content != null && !content.isBlank()) {
			sb.append(title).append(" :: ").append(content).append('\n');
		} else {
			sb.append("No ").append(title).append('\n');
		}
	}

	// helper method to format the potion list
	private String formatPotionList() {
		if (healingPotionList == null || healingPotionList.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for (String label : healingPotionList.keySet()) {
			sb.append('[').append(label).append("] ");
		}
		return sb.toString().trim();
	}
}
