package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Exit;
import mazegame.entity.Player;

public class MoveCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player thePlayer) {
		if (thePlayer.inCombat()) {
			return new CommandResponse("You can't move while in combat!");
		}

		if (userInput.getArguments().size() == 0) {
			return new CommandResponse("If you want to move you need to tell me where.");
		}
		String exitLabel = (String) userInput.getArguments().get(0);
		Exit desiredExit = thePlayer.getCurrentLocation().getExitCollection().getExit(exitLabel);
		if (desiredExit == null) {
			return new CommandResponse("There is no exit named like that. Try moving somewhere else!");
		}

		// Check if the exit is locked
		if (desiredExit.isLocked()) {
			return new CommandResponse("The path is locked! You need to unlock it first.");
		}
		
		thePlayer.setCurrentLocation(desiredExit.getDestination());
		boolean hasHostiles = thePlayer.getCurrentLocation().getNpcCollection().hasHostileNPCs();
		return new CommandResponse("You successfully move " + exitLabel + " and find yourself somewhere else\n\n"
				+ thePlayer.getCurrentLocation().toString() + encounterHostileMessage(hasHostiles));
	}

	private String encounterHostileMessage(boolean isHostile) {
		String message = "\n\n";
		if (isHostile) {
			message += "You are soon encountering some hostile NPCs. You shall begin the attack by choosing an NPC..";
		}
		return message;

	}
}
