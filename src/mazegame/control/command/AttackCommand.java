package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.control.CombatSession;
import mazegame.entity.Player;

/**
 * Command to initiate or progress combat. Handles target selection and turn flow.
 */
public class AttackCommand implements Command {

	// execute the attack command
	public CommandResponse execute(ParsedInput input, Player thePlayer) {

		// no NPCs present to fight
		if (thePlayer.getCurrentLocation().getNpcCollection().isEmpty()) {
			return new CommandResponse("There is no one to attack in this location.");
		}

		// ensure at least one hostile and alive NPC exists
		boolean hasHostile = thePlayer.getCurrentLocation().getNpcCollection().values().stream()
				.anyMatch(n -> n.isHostile() && n.getLifePoints() > 0);
		if (!hasHostile) {
			return new CommandResponse("Friendly NPCs are here. Make them your allies!");
		}

		// get or create a combat session
		CombatSession session = thePlayer.getCombatSession();
		if (session == null || session.isOver()) {
			session = new CombatSession(thePlayer);   // start fresh combat
			thePlayer.setCombatSession(session);      // bind to player
		}

		// optional first argument as target name
		String arg = input.getArguments().isEmpty() ? null : String.valueOf(input.getArguments().get(0));

		String result;

		if (session.isAwaitingPlayerAction()) {
			// player's turn: perform attack with optional target
			result = session.playerAttackAndAdvance(arg);
		} else {
			// before first exchange, accept a preferred target name
			if (session.isFirstRound() && arg != null) {
				session.setInitialPreferredTargetName(arg); // remember initial target once
			}
			// advance combat until it becomes player's turn again
			result = session.advanceCombatUntilPlayerTurn();
		}

		// clear finished combat
		if (session.isOver()) {
			thePlayer.setCombatSession(null);
		}
		return new CommandResponse(result);
	}
}
