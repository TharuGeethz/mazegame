package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Location;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;
import mazegame.entity.utility.NonPlayerCharacterCollection;

/**
 * Command to leave the player's party. Supports leaving a specific member by
 * name or dismissing the entire party.
 */
public class LeavePartyCommand implements Command {

	private static final String MSG_NO_PARTY = "You don't have a party to leave"; // shown when party is empty
	private static final String MSG_LEFT_PARTY = "You left the party"; // confirmation after leaving all

	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		// prevent leaving during combat
		if (currentPlayer.inCombat()) {
			return new CommandResponse("You can't leave party while in combat!");
		}

		final Location currentLocation = currentPlayer.getCurrentLocation(); // where NPCs will be placed
		final NonPlayerCharacterCollection playerParty = currentPlayer.getNpcCollection(); // player's party map

		// nothing to leave
		if (playerParty.isEmpty()) {
			return new CommandResponse(MSG_NO_PARTY);
		}

		// if a name is provided, remove only that NPC
		if (!userInput.getArguments().isEmpty()) {
			String npcName = (String) userInput.getArguments().get(0); // requested NPC name
			String npcNameLower = npcName.trim().toLowerCase(); // normalize key
			NonPlayerCharacter npc = playerParty.get(npcNameLower); // lookup in party
			if (npc == null) {
				return new CommandResponse("'" + npcName + "' is not in your party.");
			}
			return new CommandResponse(leaveSpecificNpc(currentPlayer, npc)); // remove single NPC
		}

		// no name given, move all party members back to the location
		for (NonPlayerCharacter npc : playerParty.values()) {
			currentLocation.getNpcCollection().put(npc.getName().toLowerCase(), npc); // place NPC in location
		}
		playerParty.clear(); // empty the party
		return new CommandResponse(MSG_LEFT_PARTY);
	}

	// helper to move one NPC from party to current location
	private String leaveSpecificNpc(Player currentPlayer, NonPlayerCharacter npc) {
		currentPlayer.getNpcCollection().remove(npc.getName().toLowerCase()); // remove from party
		currentPlayer.getCurrentLocation().getNpcCollection().put(npc.getName().toLowerCase(), npc); // add to location
		return npc.getName() + " has left your party.";
	}
}
