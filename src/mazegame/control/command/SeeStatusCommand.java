package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.GameStatus;
import mazegame.entity.Player;

public class SeeStatusCommand implements Command {

    public CommandResponse execute(ParsedInput userInput, Player thePlayer) {
        StringBuilder response = new StringBuilder();
        int locationCount = GameStatus.getInstance().getLocations().size();
        int shopCount = GameStatus.getInstance().getShops().size();
        response.append("Number of locations :: " + locationCount + "\n");
        response.append("Number of shops :: " + shopCount + "\n");
        return new CommandResponse(response.toString());
    }
}
