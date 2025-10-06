package mazegame.control;

import mazegame.control.command.*;
import mazegame.entity.Blacksmith;
import mazegame.entity.Player;

public class CommerceState extends CommandState {

	public CommerceState() {
		this.getAvailableCommands().put("go", new MoveCommand());
		this.getAvailableCommands().put("buy", new BuyCommand());
		this.getAvailableCommands().put("move", new MoveCommand());
		this.getAvailableCommands().put("look", new LookCommand());
		this.getAvailableCommands().put("sell", new SellCommand());
		this.getAvailableCommands().put("quit", new QuitCommand());

		this.getAvailableCommands().put("list", new ListItemsCommand());
		this.getAvailableCommands().put("see", new SeeStatusCommand());
		this.getAvailableCommands().put("help", new HelpCommand());
	}

	public CommandState update(Player thePlayer) {
		if (thePlayer.getCurrentLocation() instanceof Blacksmith)
			return this;
		return new MovementState();
	}
}