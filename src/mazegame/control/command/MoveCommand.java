package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Exit;
import mazegame.entity.Player;

/**
 * Command to move the player through a named exit. Blocks movement during
 * combat and if the exit is locked.
 */
public class MoveCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player thePlayer) {
		// No running away mid-fight
		if (thePlayer.inCombat()) {
			return new CommandResponse("You can't move while in combat!");
		}

		// Require a destination keyword
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("If you want to move you need to tell me where.");
		}

		String exitLabel = (String) userInput.getArguments().get(0);
		Exit desiredExit = thePlayer.getCurrentLocation().getExitCollection().getExit(exitLabel);

		// Unknown exit name
		if (desiredExit == null) {
			return new CommandResponse("There is no exit named like that. Try moving somewhere else!");
		}

		// Locked path cannot be taken yet
		if (desiredExit.isLocked()) {
			return new CommandResponse("The path is locked! You need to unlock it first.");
		}

		// Move the player and report the new location (plus any encounter hint)
		thePlayer.setCurrentLocation(desiredExit.getDestination());
		boolean hasHostiles = thePlayer.getCurrentLocation().getNpcCollection().hasHostileNPCs();
		return new CommandResponse("You successfully move " + exitLabel + " and find yourself somewhere else\n\n"
				+ thePlayer.getCurrentLocation().toString() + encounterHostileMessage(hasHostiles));
	}

	// append a combat notice if enemies are present
	private String encounterHostileMessage(boolean isHostile) {
		String message = "\n\n";
		if (isHostile) {
			message += "You are in COMBAT Mode now!\nYou have encountered some hostile NPCs. Start the attack by choosing an NPC..";
		}
		return message;
	}
}
