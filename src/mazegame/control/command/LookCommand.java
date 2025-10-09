package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Exit;
import mazegame.entity.Player;

/**
 * Command to let the player examine surroundings or a specific exit. If no
 * argument is given, it shows full location details; otherwise it describes the
 * specified exit if found.
 */
public class LookCommand implements Command {

	private CommandResponse response;

	public CommandResponse execute(ParsedInput userInput, Player thePlayer) {
		response = new CommandResponse("Can't find that to look at here!");

		// show the location description if no arguments given
		if (userInput.getArguments().size() == 0) {
			response.setMessage(thePlayer.getCurrentLocation().toString());
			return response;
		}

		// Otherwise, check if the argument matches an exit name
		for (Object argument : userInput.getArguments()) {
			if (thePlayer.getCurrentLocation().getExitCollection().containsExit(argument.toString())) {
				Exit theExit = thePlayer.getCurrentLocation().getExitCollection().getExit((String) argument);
				return new CommandResponse(theExit.getDescription());
			}
		}

		// Fallback message if nothing matched
		return response;
	}
}
