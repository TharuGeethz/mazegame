package mazegame.control.command;

import java.util.Map;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Exit;
import mazegame.entity.Location;
import mazegame.entity.Player;
import java.util.Random;

public class FleeCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		currentPlayer.setCombatSession(null); // player flaw from location so the session is over in that location

		Map<String, Exit> exits = currentPlayer.getCurrentLocation().getExitCollection().getAllExits();
		Location destination = getRandomExit(exits).getDestination(); // If a player’s party flees they do so through a
																		// random exit

		currentPlayer.setCurrentLocation(destination);
		return new CommandResponse("You successfully fled to " + destination.getLabel()
				+ " and find yourself somewhere else\n\n" + currentPlayer.getCurrentLocation().toString());
	}

	private Exit getRandomExit(Map<String, Exit> exits) {
		Object[] keys = exits.keySet().toArray();
		String key = (String) keys[new Random().nextInt(keys.length)];
		return exits.get(key);
	}

}
