package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.control.CombatSession;
import mazegame.entity.Player;
import mazegame.entity.item.Item;

/**
 * Command to equip an item from the player's inventory.
 * Prevents equipping potions and ensures only one item per type is worn.
 */
public class EquipItemCommand implements Command {

	@Override
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {

		// require an item name
		if (userInput.getArguments().isEmpty()) {
			return new CommandResponse("If you need to wear an item you need to tell me what.");
		}

		String itemName = (String) userInput.getArguments().get(0); // requested item label

		// already wearing the same item
		if (currentPlayer.getWearingItems().containsKey(itemName)) {
			return new CommandResponse("You are already wearing " + itemName + ".");
		}

		// potions cannot be equipped
		if (currentPlayer.getInventory().getPotionList().containsKey(itemName)) {
			return new CommandResponse("You can't equip a potion!");
		}

		// item must be in inventory
		if (!currentPlayer.getInventory().getItemList().containsKey(itemName)) {
			return new CommandResponse("You don’t have the " + itemName + ".");
		}

		Item itemToEquip = currentPlayer.getInventory().findItem(itemName); // item reference

		// find an already worn item of the same class
		Item alreadyWearing = currentPlayer.getWearingItems().values().stream()
				.filter(i -> i.getClass().equals(itemToEquip.getClass()))
				.findFirst()
				.orElse(null);

		String message = "";

		// only one equipped per type
		if (alreadyWearing != null) {
			message += alreadyWearing.getLabel() + " unequipped. \n";
			currentPlayer.unequipItem(alreadyWearing.getLabel()); // remove old item of same type
		}

		// equip the new item
		currentPlayer.equipItem(itemName);

		message += itemName + " equipped.";

		// integrate with combat flow if a combat turn is waiting on the player
		CombatSession cs = currentPlayer.getCombatSession();
		if (cs != null && !cs.isOver() && cs.isAwaitingPlayerAction()) {
			message = "\n\n" + cs.playerSkipTurnAndAdvance("You equipped " + itemName + ".");
			if (cs.isOver()) {
				currentPlayer.setCombatSession(null); // clear if combat ended
			}
		}
		return new CommandResponse(message);
	}
}
