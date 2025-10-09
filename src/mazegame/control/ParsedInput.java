package mazegame.control;

import java.util.ArrayList;

/**
 * Represents the parsed structure of a player's input. Stores the command
 * keyword and any additional arguments.
 */
public class ParsedInput {

	private String command;
	private ArrayList arguments;

	public ParsedInput() {
		setArguments(new ArrayList());
		setCommand("");
	}

	public ParsedInput(String command, ArrayList arguments) {
		this.setCommand(command);
		this.setArguments(arguments);
	}

	// Returns the command word (e.g., "move", "look", "attack")
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	// Returns any extra words typed after the command
	public ArrayList getArguments() {
		return arguments;
	}

	public void setArguments(ArrayList arguments) {
		this.arguments = arguments;
	}
}
