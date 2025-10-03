package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Blacksmith;
import mazegame.entity.Player;
import mazegame.entity.item.Item;

public class SellCommand implements Command {
    // If sold player receives the value listed on the weapon or armour table less 20%
    @Override
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {
		
		// Check if player is in a blacksmith (shop)
		if (!(currentPlayer.getCurrentLocation() instanceof Blacksmith)) {
			return new CommandResponse("You can only sell items in a shop.");
		}
		
		// Check if player is in combat
		if (currentPlayer.inCombat()) {
			return new CommandResponse("You can't trade while in combat!");
		}
		
		// Check if item name was given
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("What would you like to sell? Use: sell <item_name>");
		}
		
		String itemName = (String) userInput.getArguments().get(0);
		String itemNameLower = itemName.trim().toLowerCase();
		
		// Check if the item exists in player inventory
		Item itemToSell = currentPlayer.getInventory().findItem(itemNameLower);
		if (itemToSell == null) {
			return new CommandResponse("You don't have '" + itemName + "' to sell.");
		}
		
		// Check if the item is currently equipped
		if (currentPlayer.getWearingItems().containsKey(itemNameLower)) {
			return new CommandResponse("You cannot sell " + itemToSell.getLabel() + 
					" while it's equipped. Remove it first.");
		}
		
		Blacksmith shop = (Blacksmith) currentPlayer.getCurrentLocation();
		
		// Calculate sell price (80% of item value)
		int itemValue = itemToSell.getValue();
		int sellPrice = (int) Math.round(itemValue * 0.8);
		
		// Check minimum sell price of 1 gold (if item has any value)
		if (itemValue > 0 && sellPrice < 1) {
			sellPrice = 1;
		}
		
		// If item has no value, it can't be sold
		if (itemValue <= 0) {
			return new CommandResponse(itemToSell.getLabel() + " has no monetary value and cannot be sold.");
		}
		
		// Process the sale % remove item from player and add to shop
		Item soldItem = currentPlayer.getInventory().removeItem(itemToSell.getLabel());
		shop.getInventory().addItem(soldItem);
		
		// Add gold to player
		currentPlayer.getInventory().addMoney(sellPrice);
		
		return new CommandResponse("You sold " + soldItem.getLabel() + " for " + sellPrice + 
				" gold pieces (80% of " + itemValue + " gold value). You now have " + 
				currentPlayer.getInventory().getGold().getTotal() + " gold pieces.");
	}
}
