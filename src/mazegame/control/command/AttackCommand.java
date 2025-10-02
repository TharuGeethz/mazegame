
package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.control.CombatSession;
import mazegame.entity.Player;

public class AttackCommand implements Command {

	public CommandResponse execute(ParsedInput input, Player thePlayer) {

		if (thePlayer.getCurrentLocation().getNpcCollection().isEmpty()) {
			return new CommandResponse("There is no one to attack in this location.");
		}
		boolean hasHostile = thePlayer.getCurrentLocation().getNpcCollection().values().stream()
				.anyMatch(n -> n.isHostile() && n.getLifePoints() > 0);
		if (!hasHostile) {
			return new CommandResponse("Friendly NPCs are here. Make them your allies!");
		}

		CombatSession session = thePlayer.getCombatSession();
		if (session == null || session.isOver()) {
			session = new CombatSession(thePlayer);
			thePlayer.setCombatSession(session);
		}

		String arg = input.getArguments().isEmpty() ? null : String.valueOf(input.getArguments().get(0));

		String result;

		if (session.isAwaitingPlayerAction()) {
			// It is the player's turn now: execute player's attack
			result = session.playerAttackAndAdvance(arg);
		} else {
			// player provides a target in first round
			if (session.isFirstRound() && arg != null) {
				session.setInitialPreferredTargetName(arg);

			}
			result = session.advanceCombatUntilPlayerTurn();
		}

		if (session.isOver()) {
			thePlayer.setCombatSession(null);
		}
		return new CommandResponse(result);
	}
}
