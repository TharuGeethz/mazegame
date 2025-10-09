package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;
import mazegame.entity.item.Item;

/**
 * Command that lets the player drop an item from their inventory. The dropped
 * item is placed in the current location unless in combat.
 */
public class DropItemCommand implements Command {

	// execute the drop command
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		// check if player specified an item name
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("Which item do you want to drop? ");
		}

		String itemName = (String) userInput.getArguments().get(0);

		// check if player has the item
		if (currentPlayer.hasItem(itemName)) {
			String returnStr = "";
			// unequip item first if it is being worn
			if (currentPlayer.isWearing(itemName)) {
				returnStr +=  itemName + " unequipped. \n";
				currentPlayer.unequipItem(itemName);
			}

			// remove item from player and add to current location
			Item item = currentPlayer.getInventory().removeItem(itemName);
			currentPlayer.getCurrentLocation().getInventory().addItem(item);
			returnStr += itemName+ " dropped. " ;
			return new CommandResponse(returnStr);
		}

		// item not found in inventory
		return new CommandResponse("You don't have " + itemName + " to drop");
	}
}