package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.control.CombatSession;
import mazegame.entity.Player;
import mazegame.entity.item.Item;

public class EquipItemCommand implements Command {

	@Override
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("If you need to wear an item you need to tell me what.");
		}

		String itemName = (String) userInput.getArguments().get(0);

		if (currentPlayer.getWearingItems().containsKey(itemName)) {
			return new CommandResponse("You are already wearing " + itemName + ".");
		}

		if (!currentPlayer.getInventory().getItemList().containsKey(itemName)) {
			return new CommandResponse("You don’t have the " + itemName + ".");
		}

		Item itemToEquip = currentPlayer.getInventory().findItem(itemName);

		Item alreadyWearing = currentPlayer.getWearingItems().values().stream()
				.filter(i -> i.getClass().equals(itemToEquip.getClass())).findFirst().orElse(null);

		String message = "";

		if (alreadyWearing != null) {
			// only one equipped per item type (Weapon, Armor, Shield)
			message += alreadyWearing.getLabel() + " unequipped. \n";
			currentPlayer.unequipItem(alreadyWearing.getLabel());
		}

		// Equip
		currentPlayer.equipItem(itemName);

		message += itemName + " equipped.";

		CombatSession cs = currentPlayer.getCombatSession();
		if (cs != null && !cs.isOver() && cs.isAwaitingPlayerAction()) {
			message = "\n\n" + cs.playerSkipTurnAndAdvance("You equipped " + itemName + ".");
			if (cs.isOver()) {
				currentPlayer.setCombatSession(null);
			}
		}
		return new CommandResponse(message);

	}
}
