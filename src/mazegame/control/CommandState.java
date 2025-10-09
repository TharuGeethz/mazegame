package mazegame.control;

import java.util.ArrayList;
import java.util.HashMap;

import mazegame.control.command.Command;
import mazegame.entity.Player;

/**
 * Abstract class which represent the current command state of the game. Manages
 * available commands and updates dynamically based on player context.
 */
public abstract class CommandState {
	private HashMap<String, Command> availableCommands;

	// Called to refresh or change the state depending on player situation
	public abstract CommandState update(Player thePlayer);

	public CommandState() {
		availableCommands = new HashMap<String, Command>();
	}

	// Returns all currently available commands
	public HashMap<String, Command> getAvailableCommands() {
		return this.availableCommands;
	}

	// Retrieves a specific command by its keyword
	public Command getCommand(String commandLabel) {
		return availableCommands.get(commandLabel);
	}

	// Returns a list of all available command labels
	public ArrayList<String> getLabels() {
		return new ArrayList<String>(availableCommands.keySet());
	}
}
