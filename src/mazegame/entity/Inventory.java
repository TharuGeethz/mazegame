package mazegame.entity;

import java.util.HashMap;
import java.util.List;

import mazegame.entity.item.HealingPotion;
import mazegame.entity.item.Item;

public class Inventory {
	private Money gold;
	private HashMap<String, Item> itemList;
	private HashMap<String, HealingPotion> healingPotionList;

	public Inventory() {
		gold = new Money();
		itemList = new HashMap<String, Item>();
		healingPotionList = new HashMap<String, HealingPotion>();
	}

	public void addMoney(int goldPieces) {
		gold.Add(goldPieces);
	}

	public boolean removeMoney(int goldPieces) {
		return gold.Subtract(goldPieces);
	}

	public boolean addItem(Item theItem) {
		itemList.put(theItem.getLabel(), theItem);
		return true;
	}

	// added new
	public Inventory addPotion(HealingPotion potion) {
		healingPotionList.put(potion.getLabel(), potion);
		return this;
	}

	// added new - add many items
	public void addItems(List<Item> items) {
		if (items == null || items.isEmpty())
			return;
		for (Item it : items) {
			addItem(it);
		}
	}

	public Item removeItem(String itemName) {
		if (itemList.containsKey(itemName)) {
			return itemList.remove(itemName);
		} else {
			return null;
		}
	}

	public void removePotion(String potionName) {
		if (healingPotionList.containsKey(potionName)) {
			healingPotionList.remove(potionName);
		}
	}
	public HashMap<String, Item> getItemList() {
		return itemList;
	}

	// added new
	public HashMap<String, HealingPotion> getPotionList() {
		return healingPotionList;
	}

	// added new
	public Money getGold() {
		return gold;
	}

	// added new
	public void setGold(Money gold) {
		this.gold = gold;
	}

	public Item findItem(String itemLabel) {
		if (itemList.containsKey(itemLabel)) {
			return itemList.get(itemLabel);
		} else {
			return null;
		}

	}

	// added new
	public <T extends Item> String availableItemsByType(Class<T> type) {
		StringBuilder returnMsg = new StringBuilder();
		itemList.values().stream().filter(type::isInstance).map(Item::getLabel)
				.forEach(label -> returnMsg.append("[").append(label).append("] "));
		return returnMsg.toString();
	}

	public boolean hasItem(String name) {
		if (name == null)
			return false;
		return itemList.containsKey(name) || healingPotionList.containsKey(name);
	}

	// --- inside Inventory class ---

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		// Weapons
		String weapons = availableItemsByType(mazegame.entity.item.Weapon.class).trim();
		appendCategory(sb, "Weapons", weapons);

		// Armors
		String armors = availableItemsByType(mazegame.entity.item.Armor.class).trim();
		appendCategory(sb, "Armors", armors);

		// Shields
		String shields = availableItemsByType(mazegame.entity.item.Shield.class).trim();
		appendCategory(sb, "Shields", shields);

		// Potions
		String potions = formatPotionList();
		appendCategory(sb, "Potions", potions);

		// Gold
		String gold = this.gold.toString();
		sb.append(gold).append('\n');

		// Miscellaneous items (keys, banners, etc.)
		String misc = availableItemsByType(mazegame.entity.item.MiscellaneousItem.class).trim();
		appendCategory(sb, "Misc", misc);

		return sb.toString();
	}

	private void appendCategory(StringBuilder sb, String title, String content) {
		if (content != null && !content.isBlank()) {
			sb.append(title).append(" :: ").append(content).append('\n');
		} else {
			sb.append("No ").append(title).append('\n');
		}
	}

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