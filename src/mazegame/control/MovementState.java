package mazegame.control;

import mazegame.control.command.*;
import mazegame.entity.Blacksmith;
import mazegame.entity.Player;

/**
 * Represents the normal exploration state of the game. Allows movement,
 * interaction, and general non-combat actions.
 */
public class MovementState extends CommandState {

	public MovementState() {
		// Register all standard movement and interaction commands
		this.getAvailableCommands().put("quit", new QuitCommand());
		this.getAvailableCommands().put("move", new MoveCommand());
		this.getAvailableCommands().put("look", new LookCommand());
		this.getAvailableCommands().put("unlock", new UnlockCommand());
		this.getAvailableCommands().put("use", new UsePotionCommand());
		this.getAvailableCommands().put("pick", new GetItemCommand());
		this.getAvailableCommands().put("list", new ListItemsCommand());
		this.getAvailableCommands().put("drop", new DropItemCommand());
		this.getAvailableCommands().put("equip", new EquipItemCommand());
		this.getAvailableCommands().put("unequip", new UnequipItemCommand());
		this.getAvailableCommands().put("see", new SeeStatusCommand());
		this.getAvailableCommands().put("join", new JoinPartyCommand());
		this.getAvailableCommands().put("leave", new LeavePartyCommand());
		this.getAvailableCommands().put("talk", new TalkCommand());
		this.getAvailableCommands().put("help", new HelpCommand());
		this.getAvailableCommands().put("check", new CheckCommand());
	}

	// Switch to appropriate state depending on player's environment
	public CommandState update(Player thePlayer) {
		if (thePlayer.getCurrentLocation() instanceof Blacksmith) {
			return new CommerceState(); // inside shop
		} else if (thePlayer.getCurrentLocation().getNpcCollection().hasHostileNPCs()) {
			return new CombatState(); // in combat zone
		}
		return this; // remain in normal movement mode
	}
}
