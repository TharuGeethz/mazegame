package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.control.CombatSession;
import mazegame.entity.Player;
import mazegame.entity.item.HealingPotion;

/**
 * Command to use a healing potion if the player has one and is not at max life.
 * Handles combat turn advancement if used during combat.
 */
public class UsePotionCommand implements Command {

	@Override
	public CommandResponse execute(ParsedInput userInput, Player thePlayer) {
		// require a potion name
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("Which potion do you want to use? ");
		}

		String potionName = (String) userInput.getArguments().get(0); // desired potion label

		// only useful if below max life
		if (thePlayer.getLifePoints() < 20) {

			// verify potion is in inventory
			if (thePlayer.getInventory().getPotionList().containsKey(potionName)) {
				HealingPotion potion = thePlayer.getInventory().getPotionList().remove(potionName); // consume potion
				int lifePointsRecovered = potion.heal(); // healing amount
				int initialLifePoints = thePlayer.getLifePoints(); // snapshot before heal
				int newTotalLifePoints = initialLifePoints + lifePointsRecovered; // sum up
				thePlayer.setLifePoints(Math.min(newTotalLifePoints, 20)); // cap to max life

				String message = "Life points restored from potion " + potion.getLabel() + "\nLife points :: "
						+ initialLifePoints + " -> " + thePlayer.getLifePoints();

				// interact with combat flow if applicable
				CombatSession cs = thePlayer.getCombatSession();
				if (cs != null && !cs.isOver() && cs.isAwaitingPlayerAction()) {
					message += "\n\n" + cs.playerSkipTurnAndAdvance(
							"You drank " + potion.getLabel() + " potion and recovered life points.");
					if (cs.isOver()) {
						thePlayer.setCombatSession(null); // clear finished combat
					}
				}
				return new CommandResponse(message); // return success message
			}
			return new CommandResponse("You dont have the potion " + potionName); // missing potion
		}
		// already at or above max life
		return new CommandResponse(
				"You already have full  life points quota ! \nLife points :: " + thePlayer.getLifePoints());
	}
}
