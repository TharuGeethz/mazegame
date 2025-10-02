package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.utility.NonPlayerCharacterCollection;

public class LeavePartyCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {
		Location currentLocation = currentPlayer.getCurrentLocation();
		NonPlayerCharacterCollection npcCollection = currentPlayer.getNpcCollection();

		if (!npcCollection.isEmpty()) {
			if (currentPlayer.getCurrentLocation().getNpcCollection().isEmpty()) {
				NonPlayerCharacterCollection leftCNpcCollection = removeCollection(npcCollection);
				currentLocation.setNpcCollection(leftCNpcCollection);

				return new CommandResponse("You left the party");
			} else if (currentPlayer.getCurrentLocation().getNpcCollection().isHostileCollection()) {
				return new CommandResponse("Location occupied with enemies! You cannot leave your party here.");
			}
		}
		return new CommandResponse("You don't have a party to leave");
	}

	public NonPlayerCharacterCollection removeCollection(NonPlayerCharacterCollection npcList) {
		NonPlayerCharacterCollection npcCollection = new NonPlayerCharacterCollection();
		npcCollection.putAll(npcList);
		npcList.clear();
		return npcCollection;
	}
}
