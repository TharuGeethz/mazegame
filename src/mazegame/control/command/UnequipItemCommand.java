package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;

public class UnequipItemCommand implements Command {

    public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {
        if (userInput.getArguments().isEmpty()) {
            return new CommandResponse("you need to tell me what you want to unequip");
        }

        String itemName = (String) userInput.getArguments().get(0);
        
        if (currentPlayer.getWearingItems().containsKey(itemName)) {
        	currentPlayer.unequipItem(itemName);
        	
            return new CommandResponse(itemName + " unequipped");
        }
        return new CommandResponse("you are not wearing "+ itemName);

    }

}
