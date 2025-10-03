package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;

public class QuitCommand implements Command {

	public CommandResponse execute(ParsedInput input, Player thePlayer) {
		if (thePlayer.inCombat()) {
			return new CommandResponse("You can't quit the game while in combat! Finish what you started!");
		}
		return new CommandResponse("Thanks for playing --- Goodbye", true);
	}
}
