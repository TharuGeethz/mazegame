package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Exit;
import mazegame.entity.Player;

/**
 * Command to unlock special exits using the banner. Only works on castle
 * entrances from Town Square or Crystal Cave.
 */
public class UnlockCommand implements Command {
	@Override
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		// Prevent unlocking during combat
		if (currentPlayer.inCombat()) {
			return new CommandResponse("You can't unlock exits while in combat!");
		}

		// Require a direction argument
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("Which direction do you want to unlock? Use: unlock <direction>");
		}

		String direction = (String) userInput.getArguments().get(0);
		Exit exit = currentPlayer.getCurrentLocation().getExitCollection().getExit(direction);

		// Invalid exit direction
		if (exit == null) {
			return new CommandResponse("There is no exit in that direction.");
		}

		// Exit already open
		if (!exit.isLocked()) {
			return new CommandResponse("That exit is already unlocked.");
		}

		// Must have the banner to unlock castle paths
		if (!currentPlayer.hasItem("banner")) {
			return new CommandResponse("You need the banner to unlock this path.");
		}

		// Allow unlocking only at specific castle entrances
		String currentLocationLabel = currentPlayer.getCurrentLocation().getLabel();
		boolean isValidCastleEntrance = false;

		if ("Town Square".equals(currentLocationLabel) && "southwest".equals(direction)) {
			isValidCastleEntrance = true;
		} else if ("Crystal Cave".equals(currentLocationLabel) && "west".equals(direction)) {
			isValidCastleEntrance = true;
		}

		// Reject invalid banner use
		if (!isValidCastleEntrance) {
			return new CommandResponse("The banner doesn't work on this lock.");
		}

		// Unlock and confirm success
		exit.setLocked(false);

		return new CommandResponse("You used the banner to unlock the path. The way to Gregor's castle is now open!");
	}
}
