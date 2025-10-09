package mazegame.control;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Breaks a player's raw text input into a command and arguments. Ignores filler
 * words like "the" or "to" for simpler parsing.
 */
public class Parser {
	private ArrayList<String> dropWords;
	private ArrayList<String> validCommands;

	public Parser(ArrayList<String> validCommands) {
		dropWords = new ArrayList<String>(Arrays.asList("an", "and", "the", "this", "to"));
		this.validCommands = validCommands;
	}

	public ParsedInput parse(String rawInput) {
		ParsedInput parsedInput = new ParsedInput();
		String lowercaseInput = rawInput.toLowerCase();
		ArrayList<String> stringTokens = new ArrayList<String>(Arrays.asList(lowercaseInput.split(" ", 2)));

		// Identify the main command and filter out drop words
		for (String token : stringTokens) {
			if (validCommands.contains(token)) {
				parsedInput.setCommand(token);
			} else if (!dropWords.contains(token)) {
				parsedInput.getArguments().add(token);
			}
		}
		return parsedInput;
	}
}
