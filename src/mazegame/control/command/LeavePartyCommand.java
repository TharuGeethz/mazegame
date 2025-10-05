package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Location;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;
import mazegame.entity.utility.NonPlayerCharacterCollection;

public class LeavePartyCommand implements Command {

	private static final String MSG_NO_PARTY = "You don't have a party to leave";
	private static final String MSG_LEFT_PARTY = "You left the party";
	private static final String MSG_HOSTILE_LOCATION = "Location occupied with enemies! You cannot leave your party here.";

	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		final Location currentLocation = currentPlayer.getCurrentLocation();
		final NonPlayerCharacterCollection npcsHere = currentLocation.getNpcCollection();
		final NonPlayerCharacterCollection playerParty = currentPlayer.getNpcCollection();

		if (playerParty.isEmpty()) {
			return new CommandResponse(MSG_NO_PARTY);
		}

		// If name given, leave only that party member
		if (!userInput.getArguments().isEmpty()) {
			String npcName = (String) userInput.getArguments().get(0);
			String npcNameLower = npcName.trim().toLowerCase();
			NonPlayerCharacter npc = playerParty.get(npcNameLower);
			if (npc == null) {
				return new CommandResponse("'" + npcName + "' is not in your party.");
			}
			return new CommandResponse(leaveSpecificNpc(currentPlayer, npc));
		}

		// If no name given, leave all party members
		// Add party members to current location
		for (NonPlayerCharacter npc : playerParty.values()) {
			currentLocation.getNpcCollection().put(npc.getName().toLowerCase(), npc);
		}
		playerParty.clear();
		return new CommandResponse(MSG_LEFT_PARTY);
	}

	private String leaveSpecificNpc(Player currentPlayer, NonPlayerCharacter npc) {
		// Remove from party and add to current location
		currentPlayer.getNpcCollection().remove(npc.getName().toLowerCase());
		currentPlayer.getCurrentLocation().getNpcCollection().put(npc.getName().toLowerCase(), npc);
		return npc.getName() + " has left your party.";
	}
}
