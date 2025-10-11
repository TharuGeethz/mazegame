package mazegame.control.command;

import java.util.Map;
import java.util.Random;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Exit;
import mazegame.entity.Location;
import mazegame.entity.Player;

/**
 * Command allowing a player to flee from combat. Upon fleeing, the player
 * escapes to a random exit.
 */
public class FleeCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		// end the combat session as player flees from current fight
		currentPlayer.setCombatSession(null);

		// get all exits from current location
		Map<String, Exit> exits = currentPlayer.getCurrentLocation().getExitCollection().getAllExits();

		// flee to a random exit's destination
		Location destination = getRandomExit(exits).getDestination();

		// move player to new location
		currentPlayer.setCurrentLocation(destination);

		// confirm successful escape
		return new CommandResponse("You successfully fled to " + destination.getLabel()
				+ " and find yourself somewhere else\n\n" + currentPlayer.getCurrentLocation().toString());
	}

	// randomly selects one available exit
	private Exit getRandomExit(Map<String, Exit> exits) {
		Object[] keys = exits.keySet().toArray();
		String key = (String) keys[new Random().nextInt(keys.length)];
		return exits.get(key);
	}
}
