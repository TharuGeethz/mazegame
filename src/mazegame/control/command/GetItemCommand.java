package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.FiniteInventory;
import mazegame.entity.Player;
import mazegame.entity.item.HealingPotion;
import mazegame.entity.item.Item;
import mazegame.entity.utility.WeightLimit;

import java.util.Map;

/**
 * Command to pick up an item or potion from the current location. Enforces
 * combat restriction, banner rule, and carrying weight limit.
 */
public class GetItemCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		// cannot pick up items during combat
		if (currentPlayer.inCombat()) {
			return new CommandResponse("You can't pick up items while in combat!");
		}

		// require item name
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("Please specify the item you want.");
		}

		String itemName = (String) userInput.getArguments().get(0); // requested item label

		// banner rule check
		if ("banner".equals(itemName)) {
			boolean hasLivingHostiles = currentPlayer.getCurrentLocation().getNpcCollection().values().stream()
					.anyMatch(npc -> npc.isHostile() && npc.getLifePoints() > 0);

			if (hasLivingHostiles) {
				return new CommandResponse(
						"The banner is heavily guarded! You must defeat Philip and his gang before you can take it.");
			}
		}

		// items available at location
		Map<String, Item> itemsInLocation = currentPlayer.getCurrentLocation().getInventory().getItemList();

		// potions available at location
		Map<String, HealingPotion> potionsInLocation = currentPlayer.getCurrentLocation().getInventory()
				.getPotionList();

		// already owned or missing checks
		if (currentPlayer.hasItem(itemName)) {
			return new CommandResponse(itemName + " has been collected already.");
		} else if (!itemsInLocation.containsKey(itemName) && !potionsInLocation.containsKey(itemName)) {
			return new CommandResponse(itemName + " is not there in current location.");
		} else {
			Item currentItem = itemsInLocation.get(itemName); // try as normal item first

			if (currentItem != null) {
				// compute carry limit and current weight
				int weightLimit = WeightLimit.getInstance().getModifier(currentPlayer.getStrength());
				FiniteInventory playerInventory = (FiniteInventory) currentPlayer.getInventory();
				double playerInventoryWeight = playerInventory.getWeight();

				// include equipped items in total carried weight
				for (Item item : currentPlayer.getWearingItems().values()) {
					playerInventoryWeight += item.getWeight();
				}

				double itemWeight = currentItem.getWeight();

				// enforce carry capacity
				if (playerInventoryWeight + itemWeight <= weightLimit) {
					currentPlayer.getInventory().addItem(currentItem); // add to player
					currentPlayer.getCurrentLocation().getInventory().removeItem(currentItem.getLabel()); // remove from
																											// ground
					return new CommandResponse(itemName + " has been collected successfully.");

				} else {

					return new CommandResponse("The item you are trying to collect exceeds your weight limit.");

				}
			} else {
				// handle potion pickup
				HealingPotion currentPotion = potionsInLocation.get(itemName);
				currentPlayer.getInventory().addPotion(currentPotion); // add to player
				currentPlayer.getCurrentLocation().getInventory().removePotion(currentPotion.getLabel()); // remove from
																											// ground
				return new CommandResponse(itemName + " has been collected successfully.");
			}

		}
	}
}
