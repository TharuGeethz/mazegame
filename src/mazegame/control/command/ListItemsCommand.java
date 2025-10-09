package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;
import mazegame.entity.item.Item;

/**
 * Command to list everything the player carries and what’s currently equipped.
 * Prints inventory (items/potions/gold) and then grouped equipped gear.
 */
public class ListItemsCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		StringBuilder itemList = new StringBuilder();

		// Show a simple message if the player has nothing at all
		if (currentPlayer.getInventory().getItemList().isEmpty()
				&& currentPlayer.getInventory().getPotionList().isEmpty()
				&& currentPlayer.getInventory().getGold().getTotal() == 0) {
			itemList.append("No items held \n");
		} else {
			// Otherwise, dump the inventory’s own string view
			itemList.append("Items held :: \n");
			itemList.append(currentPlayer.getInventory().toString());
		}

		// Equipped section (weapons/armors/shields), or a “none” line
		if (currentPlayer.getWearingItems().isEmpty()) {
			itemList.append("\nNo wearing items");
		} else {
			itemList.append("\nItems wearing :: \n");
			// Weapons
			String weapons = availableItemsByType(mazegame.entity.item.Weapon.class, currentPlayer).trim();
			appendCategory(itemList, "Weapons", weapons);

			// Armors
			String armors = availableItemsByType(mazegame.entity.item.Armor.class, currentPlayer).trim();
			appendCategory(itemList, "Armors", armors);

			// Shields
			String shields = availableItemsByType(mazegame.entity.item.Shield.class, currentPlayer).trim();
			appendCategory(itemList, "Shields", shields);
		}

		return new CommandResponse(itemList.toString());
	}

	// Append a titled line only if there’s content; otherwise say “No <title>”
	private void appendCategory(StringBuilder sb, String title, String content) {
		if (content != null && !content.isBlank()) {
			sb.append(title).append(" :: ").append(content).append('\n');
		} else {
			sb.append("No ").append(title).append('\n');
		}
	}

	// list equipped items of a given class (e.g., Weapon) as [label] tokens
	public <T extends Item> String availableItemsByType(Class<T> type, Player currentPlayer) {
		StringBuilder returnMsg = new StringBuilder();
		currentPlayer.getWearingItems().values().stream().filter(type::isInstance).map(Item::getLabel)
				.forEach(label -> returnMsg.append("[").append(label).append("] "));
		return returnMsg.toString();
	}
}
