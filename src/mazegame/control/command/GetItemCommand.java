package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;
import mazegame.entity.item.HealingPotion;
import mazegame.entity.item.Item;
import mazegame.entity.utility.WeightLimit;

import java.util.Map;

public class GetItemCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		if (currentPlayer.inCombat()) {
			return new CommandResponse("You can't pick up items while in combat!");
		}

		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("Please specify the item you want.");
		}

		String itemName = (String) userInput.getArguments().get(0);
		// items
		Map<String, Item> itemsInLocation = currentPlayer.getCurrentLocation().getInventory().getItemList();

		// potions
		Map<String, HealingPotion> potionsInLocation = currentPlayer.getCurrentLocation().getInventory()
				.getPotionList();

		if (currentPlayer.hasItem(itemName)) {
			return new CommandResponse(itemName + " has been collected already.");
		} else if (!itemsInLocation.containsKey(itemName) && !potionsInLocation.containsKey(itemName)) {
			return new CommandResponse(itemName + " is not there in current location.");
		} else {

			Item currentItem = itemsInLocation.get(itemName);

			if (currentItem != null) {
				int weightLimit = WeightLimit.getInstance().getModifier(currentPlayer.getStrength());
				int playerCurrentWeight = currentPlayer.getItemsWeight();

				int itemWeight = currentItem.getWeight();

				if (playerCurrentWeight + itemWeight <= weightLimit) {
					currentPlayer.getInventory().addItem(currentItem);
					currentPlayer.getCurrentLocation().getInventory().removeItem(currentItem.getLabel());
					return new CommandResponse(itemName + " has been collected successfully.");

				} else {

					return new CommandResponse("The item you are trying to collect exceeds your weight limit.");

				}
			} else {
				HealingPotion currentPotion = potionsInLocation.get(itemName);
				currentPlayer.getInventory().addPotion(currentPotion);
				currentPlayer.getCurrentLocation().getInventory().removePotion(currentPotion.getLabel());
				return new CommandResponse(itemName + " has been collected successfully.");
			}

		}
	}
}
