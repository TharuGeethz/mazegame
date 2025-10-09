package mazegame.control;

import mazegame.control.command.*;
import mazegame.entity.Blacksmith;
import mazegame.entity.Player;

/**
 * Represents the combat state where the player is engaged with enemies. Enables
 * battle-related actions like attacking, fleeing, or using potions.
 */
public class CombatState extends CommandState {

	public CombatState() {
		// Register combat-specific and supportive commands
		this.getAvailableCommands().put("quit", new QuitCommand());
		this.getAvailableCommands().put("look", new LookCommand());
		this.getAvailableCommands().put("attack", new AttackCommand());
		this.getAvailableCommands().put("a", new AttackCommand()); // shorthand alias
		this.getAvailableCommands().put("list", new ListItemsCommand());
		this.getAvailableCommands().put("equip", new EquipItemCommand());
		this.getAvailableCommands().put("unequip", new UnequipItemCommand());
		this.getAvailableCommands().put("see", new SeeStatusCommand());
		this.getAvailableCommands().put("flee", new FleeCommand());
		this.getAvailableCommands().put("use", new UsePotionCommand());
		this.getAvailableCommands().put("talk", new TalkCommand());
		this.getAvailableCommands().put("help", new HelpCommand());
		this.getAvailableCommands().put("check", new CheckCommand());
	}

	// Determine if the player remains in combat or transitions out
	public CommandState update(Player thePlayer) {
		if (thePlayer.getCurrentLocation().getNpcCollection().hasHostileNPCs()) {
			return this; // still fighting
		} else if (thePlayer.getCurrentLocation() instanceof Blacksmith) {
			return new CommerceState(); // switch to trading if inside shop
		}
		return new MovementState(); // otherwise return to normal exploration
	}
}
