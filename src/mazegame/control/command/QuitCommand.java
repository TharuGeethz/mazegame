package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;

/**
 * Command to quit the game gracefully. Displays a farewell message and signals
 * the game to end.
 */
public class QuitCommand implements Command {

	public CommandResponse execute(ParsedInput input, Player thePlayer) {
		return new CommandResponse("Thanks for playing --- Goodbye", true);
	}
}
