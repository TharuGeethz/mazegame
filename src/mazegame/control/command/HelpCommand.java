package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Blacksmith;
import mazegame.entity.Player;

public class HelpCommand implements Command {
    @Override
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {
		
		StringBuilder helpText = new StringBuilder();
		helpText.append("====== MAZE GAME HELP ======\n\n");
		
		// Check if player is in a shop
		boolean inShop = currentPlayer.getCurrentLocation() instanceof Blacksmith;
		
		if (inShop) {
			helpText.append("You are in a SHOP. Available commands:\n\n");
			helpText.append("COMMERCE COMMANDS:\n");
			helpText.append("  buy <item>      - Purchase an item from the shop\n");
			helpText.append("  sell <item>     - Sell an item to the shop (80% of value)\n\n");
		} else {
			helpText.append("You are exploring the MAZE. Available commands:\n\n");
			helpText.append("COMBAT COMMANDS:\n");
			helpText.append("  attack [target]  - Attack a hostile NPC (optional target name)\n");
			helpText.append("  flee             - Attempt to escape from combat\n");
			helpText.append("  use <potion>     - Use a healing potion\n\n");
			
			helpText.append("PARTY COMMANDS:\n");
			helpText.append("  join [name]      - Join party with NPC (or all if no name)\n");
			helpText.append("  leave [name]     - Leave party member (or all if no name)\n");
			helpText.append("  talk <name>      - Talk to an NPC\n\n");
			
			helpText.append("ITEM COMMANDS:\n");
			helpText.append("  pick <item>      - Pick up an item from the ground\n");
			helpText.append("  drop <item>      - Drop an item from your inventory\n");
			helpText.append("  equip <item>     - Equip a weapon, armor, or shield\n");
			helpText.append("  unequip <item>   - Unequip a worn item\n\n");
		}
		
		helpText.append("NAVIGATION COMMANDS:\n");
		helpText.append("  go <direction>     - Move in a direction (north, south, east, west)\n");
		helpText.append("  move <direction>   - Same as 'go'\n");
		helpText.append("  look               - Examine your current location\n\n");
		helpText.append("  unlock <direction> - Unlock a locked path (requires appropriate key)\n");
		
		helpText.append("INFORMATION COMMANDS:\n");
		helpText.append("  list               - Show your inventory and equipped items\n");
		helpText.append("  see                - Show game status report\n");
		helpText.append("  help               - Show this help message\n\n");

		helpText.append("OTHER COMMANDS:\n");
		helpText.append("  quit               - Exit the game\n\n");

		if (inShop) {
			helpText.append("TIP: Use 'look' to see what items are available for purchase.\n");
			helpText.append("TIP: Move away from the shop to access exploration commands.");
		} else {
			helpText.append("TIP: Use 'look' to see exits, items, and NPCs in your location.\n");
			helpText.append("TIP: Visit a shop to buy and sell items.\n");
			helpText.append("TIP: Some paths to Gregor's castle are locked - find the banner!\n");
			helpText.append("TIP: The banner can be found after defeating Philip and his gang.");
		}
		
		return new CommandResponse(helpText.toString());
	}
}
