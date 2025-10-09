package mazegame.control;

import mazegame.control.command.*;
import mazegame.entity.Blacksmith;
import mazegame.entity.Player;

/**
 * Represents the trading state when the player is inside a shop (Blacksmith).
 * Enables buy/sell actions along with basic movement and inventory commands.
 */
public class CommerceState extends CommandState {

	public CommerceState() {
		// Register available shop-related and general commands
		this.getAvailableCommands().put("buy", new BuyCommand());
		this.getAvailableCommands().put("move", new MoveCommand());
		this.getAvailableCommands().put("look", new LookCommand());
		this.getAvailableCommands().put("equip", new EquipItemCommand());
		this.getAvailableCommands().put("unequip", new UnequipItemCommand());
		this.getAvailableCommands().put("sell", new SellCommand());
		this.getAvailableCommands().put("quit", new QuitCommand());
		this.getAvailableCommands().put("list", new ListItemsCommand());
		this.getAvailableCommands().put("see", new SeeStatusCommand());
		this.getAvailableCommands().put("help", new HelpCommand());
	}

	// Update the current state depending on where the player moves
	public CommandState update(Player thePlayer) {
		if (thePlayer.getCurrentLocation() instanceof Blacksmith) {
			return this; // stay in commerce mode
		} else if (thePlayer.getCurrentLocation().getNpcCollection().hasHostileNPCs()) {
			return new CombatState(); // switch to combat if enemies present
		}
		return new MovementState(); // otherwise, back to normal exploration
	}
}
