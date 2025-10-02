package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;
import mazegame.entity.item.Item;

public class DropItemCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("Which item do you want to drop? ");
		}

		String itemName = (String) userInput.getArguments().get(0);
		if (currentPlayer.hasItem(itemName)) {
			Item item = currentPlayer.getInventory().removeItem(itemName);
			currentPlayer.getCurrentLocation().getInventory().addItem(item);
			return new CommandResponse("You dropped the " + itemName);
		}
		return new CommandResponse("You don't have " + itemName + " to drop");
	}

}