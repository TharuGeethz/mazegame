package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.CombatSession;
import mazegame.entity.Player;
import mazegame.entity.item.HealingPotion;

public class UsePotionCommand implements Command {

	@Override
	public CommandResponse execute(ParsedInput userInput, Player thePlayer) {
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("Which potion do you want to use? ");
		}

		String potionName = (String) userInput.getArguments().get(0);

		if (thePlayer.getLifePoints() < 20) {

			if (thePlayer.getInventory().getPotionList().containsKey(potionName)) {
				HealingPotion potion = thePlayer.getInventory().getPotionList().remove(potionName);
				int lifePointsRecovered = potion.heal();
				int newTotalLifePoints = thePlayer.getLifePoints() + lifePointsRecovered;
				thePlayer.setLifePoints(Math.min(newTotalLifePoints, 20));

				String message = "Life points restored from potion " + potion.getLabel() + "\nLife points :: "
						+ thePlayer.getLifePoints();

				CombatSession cs = thePlayer.getCombatSession();
				if (cs != null && !cs.isOver() && cs.isAwaitingPlayerAction()) {
					message += "\n\n" + cs.playerSkipTurnAndAdvance("You used a potion and recovered life points.");
					if (cs.isOver()) {
						thePlayer.setCombatSession(null);
					}
				}
				return new CommandResponse(message);
			}
			return new CommandResponse("You dont have the potion " + potionName);
		}
		return new CommandResponse(
				"You already have full  life points quota ! \nLife points :: " + thePlayer.getLifePoints());
	}
}
