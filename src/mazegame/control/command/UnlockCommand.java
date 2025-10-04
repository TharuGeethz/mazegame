package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Exit;
import mazegame.entity.Player;

public class UnlockCommand implements Command {
    @Override
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {
		
		if (currentPlayer.inCombat()) {
			return new CommandResponse("You can't unlock exits while in combat!");
		}
		
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("Which direction do you want to unlock? Use: unlock <direction>");
		}
		
		String direction = (String) userInput.getArguments().get(0);
		Exit exit = currentPlayer.getCurrentLocation().getExitCollection().getExit(direction);
		
		if (exit == null) {
			return new CommandResponse("There is no exit in that direction.");
		}
		
		if (!exit.isLocked()) {
			return new CommandResponse("That exit is already unlocked.");
		}

		// Check if player has banner
		if (!currentPlayer.hasItem("banner")) {
			return new CommandResponse("You need the banner to unlock this path.");
		}

		// Check if this is one of the castle entrances that can be unlocked with the banner
		String currentLocationLabel = currentPlayer.getCurrentLocation().getLabel();
		boolean isValidCastleEntrance = false;
		
		if ("Town Square".equals(currentLocationLabel) && "southwest".equals(direction)) {
			isValidCastleEntrance = true;
		} else if ("Crystal Cave".equals(currentLocationLabel) && "west".equals(direction)) {
			isValidCastleEntrance = true;
		}
		
		if (!isValidCastleEntrance) {
			return new CommandResponse("The banner doesn't work on this lock.");
		}
		
		// Unlock the exit
		exit.setLocked(false);
		
		return new CommandResponse("You use the banner to unlock the path. The way to Gregor's castle is now open!");
	}
}
