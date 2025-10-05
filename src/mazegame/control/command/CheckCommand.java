package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;
import mazegame.entity.item.Item;

public class CheckCommand implements Command {

    public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {
        if (currentPlayer.getNpcCollection().isEmpty()) {
            return new CommandResponse("You don't have any ally with you currently!");
        }
        if (userInput.getArguments().isEmpty()) {
            //ask which ally does player wanna check
            return new CommandResponse("Which ally do you want to check? ");
        }

        String npcName = (String) userInput.getArguments().get(0);
        if (currentPlayer.getNpcCollection().hasNPC(npcName)) {
            //print that npc details

            return new CommandResponse(currentPlayer.getNpcCollection().get(npcName.toLowerCase()).toString());
        }
        return new CommandResponse("You don't have an ally named " + npcName);
    }

}