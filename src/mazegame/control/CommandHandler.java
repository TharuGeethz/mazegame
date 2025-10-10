package mazegame.control;

import mazegame.control.command.Command;
import mazegame.entity.Player;

/**
 * Routes a player's input to the correct command based on the current state.
 * State can change each turn (movement/combat/commerce).
 */
public class CommandHandler {
	private CommandState availableCommands;

	public CommandHandler() {
		availableCommands = new MovementState(); // start in normal exploration
	}

	public CommandResponse processTurn(String userInput, Player thePlayer) {
		availableCommands = availableCommands.update(thePlayer); // get the appropriate state
		ParsedInput validInput = parse(userInput); // tokenize input
		Command theCommand = (Command) availableCommands.getCommand(validInput.getCommand());
		if (theCommand == null) {
			return new CommandResponse("Not a valid command");
		} else {
			return theCommand.execute(validInput, thePlayer); // run the command
		}
	}

	// Build a parser configured with the labels available in the current state
	private ParsedInput parse(String userInput) {
		Parser theParser = new Parser(availableCommands.getLabels());
		return theParser.parse(userInput);
	}
}
