package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JoinPartyCommand implements Command {

	@Override
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		if (currentPlayer.inCombat()) {
			return new CommandResponse("You can't recruit allies while in combat!");
		}

		// If player specified a name
		if (!userInput.getArguments().isEmpty()) {
			String npcName = (String) userInput.getArguments().get(0);
			String npcNameLower = npcName.trim().toLowerCase();

			if (currentPlayer.getNpcCollection().get(npcNameLower) != null) {
				return new CommandResponse("You have already joined " + npcName);
			}

			NonPlayerCharacter npc = currentPlayer.getCurrentLocation().getNpcCollection().get(npcNameLower);
			if (npc == null) {
				return new CommandResponse("No one named '" + npcName + "' is here.");
			}

			return new CommandResponse(joinNpc(currentPlayer, npc));
		}

		// If no name is given then join all eligible NPCs
		Map<String, NonPlayerCharacter> locationNpcs = currentPlayer.getCurrentLocation().getNpcCollection();
		if (locationNpcs.isEmpty()) {
			return new CommandResponse("There’s no one here to join.");
		}

		List<String> results = new ArrayList<>();
		List<String> keys = new ArrayList<>(locationNpcs.keySet()); // avoid concurrent modification

		for (String key : keys) {
			NonPlayerCharacter npc = locationNpcs.get(key);
			if (npc != null) {
				results.add(joinNpc(currentPlayer, npc));
			}
		}

		return new CommandResponse(String.join("\n", results));
	}

	private String joinNpc(Player currentPlayer, NonPlayerCharacter npc) {
		if (npc.isHostile()) {
			return npc.getName() + " snarls and refuses to join you.";
		}

		if (currentPlayer.getNpcCollection().hasNPC(npc.getName())) {
			return npc.getName() + " is already in your party.";
		}

		// Move from location to party
		currentPlayer.getNpcCollection().put(npc.getName().toLowerCase(), npc);
		currentPlayer.getCurrentLocation().getNpcCollection().remove(npc.getName().toLowerCase());
		return npc.getName() + " has joined your party.";
	}
}
