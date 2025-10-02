package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;

public class ListItemsCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		StringBuilder itemList = new StringBuilder();

		if (currentPlayer.getInventory().getItemList().isEmpty()) {
			itemList.append("No items held");
		} else {
			itemList.append("Items held :: ");
			for (String item : currentPlayer.getInventory().getItemList().keySet()) {
				itemList.append("[ " + item + " ]");
			}
		}

		if (currentPlayer.getWearingItems().isEmpty()) {
			itemList.append("\nNo wearing items");
		} else {
			itemList.append("\nItems wearing :: ");
			for (String item : currentPlayer.getWearingItems().keySet()) {
				itemList.append("[ " + item + " ]");
			}
		}

		if (currentPlayer.getInventory().getPotionList().isEmpty()) {
			itemList.append("\nNo potions held");
		} else {
			itemList.append("\nPotions held :: ");
			for (String item : currentPlayer.getInventory().getPotionList().keySet()) {
				itemList.append("[ " + item + " ]");
			}
		}

		if (currentPlayer.getInventory().getGold().getTotal() == 0) {
			itemList.append("\nNo gold");
		} else {
			itemList.append("\nGold :: ");
			itemList.append("[ " + currentPlayer.getInventory().getGold().getTotal() + " pieces]");
		}

		return new CommandResponse(itemList.toString());
	}
}
