package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Blacksmith;
import mazegame.entity.FiniteInventory;
import mazegame.entity.Player;
import mazegame.entity.item.Item;
import mazegame.entity.utility.WeightLimit;

public class BuyCommand implements Command {
	// If bought player pays as per the weapon and armour table
	@Override
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		// Check if player is in a blacksmith (shop)
		if (!(currentPlayer.getCurrentLocation() instanceof Blacksmith)) {
			return new CommandResponse("You can only buy items in a shop.");
		}

		// Check if player is in combat
		if (currentPlayer.inCombat()) {
			return new CommandResponse("You can't shop while in combat!");
		}

		// Check if item name was given
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("What would you like to buy? Use: buy <item_name>");
		}

		String itemName = (String) userInput.getArguments().get(0);
		String itemNameLower = itemName.trim().toLowerCase();
		Blacksmith shop = (Blacksmith) currentPlayer.getCurrentLocation();

		// Check if item is available in the shop
		Item itemToBuy = shop.getInventory().findItem(itemNameLower);
		if (itemToBuy == null) {
			return new CommandResponse("'" + itemName + "' is not available in this shop.");
		}

		// Check if player has enough gold
		int itemPrice = itemToBuy.getValue();
		if (currentPlayer.getInventory().getGold().getTotal() < itemPrice) {
			return new CommandResponse("You don't have enough gold. " + itemToBuy.getLabel() + " costs " + itemPrice
					+ " gold pieces, but you only have " + currentPlayer.getInventory().getGold().getTotal() + ".");
		}

		// Check if player already has this item
		if (currentPlayer.getInventory().hasItem(itemToBuy.getLabel())) {
			return new CommandResponse("You already have " + itemToBuy.getLabel() + ".");
		}

		// check if player weight limit exceeds

		FiniteInventory playerInventory = (FiniteInventory) currentPlayer.getInventory();
		double playerInventoryWeight = playerInventory.getWeight();

		double weightLimit = playerInventory.getWeightLimit();

		double wearingItemsWeight = 0;
		for (Item item : currentPlayer.getWearingItems().values()) {
			wearingItemsWeight += item.getWeight();
		}

		double itemWeight = itemToBuy.getWeight();

		if (playerInventoryWeight + itemWeight + wearingItemsWeight > weightLimit) {
			return new CommandResponse("The item you are trying to purchase exceeds your weight limit.");
		}

		// Process the purchase & remove gold from player
		boolean paymentSuccessful = currentPlayer.getInventory().removeMoney(itemPrice);
		if (!paymentSuccessful) {
			return new CommandResponse("Payment failed. Please try again.");
		}

		// Remove item from shop and add to player
		Item purchasedItem = shop.getInventory().removeItem(itemToBuy.getLabel());
		boolean successFlag = playerInventory.addItem(purchasedItem, wearingItemsWeight);

		if (!successFlag) {
			return new CommandResponse("Something went wrong when adding item to the inventory!.");
		}

		return new CommandResponse(
				"You bought " + purchasedItem.getLabel() + " for " + itemPrice + " gold pieces. You have "
						+ currentPlayer.getInventory().getGold().getTotal() + " gold pieces remaining.");
	}
}
