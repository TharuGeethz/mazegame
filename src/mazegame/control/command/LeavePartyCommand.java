package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Location;
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

        if (!playerParty.isEmpty()) {
            if (npcsHere.isEmpty() || !npcsHere.isHostileCollection()) {
                NonPlayerCharacterCollection leftCNpcCollection = removeCollection(playerParty);
                currentLocation.setNpcCollection(leftCNpcCollection);

                return new CommandResponse(MSG_LEFT_PARTY);
            } else if (npcsHere.isHostileCollection()) {
                return new CommandResponse(MSG_HOSTILE_LOCATION);
            }
        }
        return new CommandResponse(MSG_NO_PARTY);
    }

    public NonPlayerCharacterCollection removeCollection(NonPlayerCharacterCollection npcList) {
        NonPlayerCharacterCollection npcCollection = new NonPlayerCharacterCollection();
        npcCollection.putAll(npcList);
        npcList.clear();
        return npcCollection;
    }
}
