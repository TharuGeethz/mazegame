package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;
import mazegame.entity.item.Item;

/**
 * Command that lets the player to check details of their allies (NPC party
 * members). Displays all allies if no name is given, or specific details if a
 * name is provided.
 */
public class CheckCommand implements Command {

	// execute the check command
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {
		// if player has no allies
		if (currentPlayer.getNpcCollection().isEmpty()) {
			return new CommandResponse("You don't have any ally with you currently!");
		}

		// if no arguments are given, list all allies
		if (userInput.getArguments().isEmpty()) {
			StringBuilder npcList = new StringBuilder();

			// iterate through all NPCs and show details
			for (NonPlayerCharacter npc : currentPlayer.getNpcCollection().values()) {
				npcList.append(npc.toString()).append("\n");
			}

			return new CommandResponse(npcList.toString());
		}

		// check details of a specific NPC by name
		String npcName = (String) userInput.getArguments().get(0);
		if (currentPlayer.getNpcCollection().hasNPC(npcName)) {
			return new CommandResponse(currentPlayer.getNpcCollection().get(npcName.toLowerCase()).toString());
		}

		// if given name not found
		return new CommandResponse("You don't have an ally named " + npcName);
	}
}
