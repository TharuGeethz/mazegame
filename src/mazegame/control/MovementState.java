package mazegame.control;

import mazegame.control.command.*;
import mazegame.entity.Blacksmith;
import mazegame.entity.Player;

public class MovementState extends CommandState {
    public MovementState()
    {
        this.getAvailableCommands().put("go", new MoveCommand());
        this.getAvailableCommands().put("quit", new QuitCommand());
        this.getAvailableCommands().put("move", new MoveCommand());
        this.getAvailableCommands().put("look", new LookCommand());
        //this.getAvailableCommands().put("unlock", new UnlockCommand());
        this.getAvailableCommands().put("attack", new AttackCommand());

        this.getAvailableCommands().put("pick", new GetItemCommand());
        this.getAvailableCommands().put("list", new ListItemsCommand());
        this.getAvailableCommands().put("drop", new DropItemCommand());
        this.getAvailableCommands().put("equip", new EquipItemCommand());
        this.getAvailableCommands().put("unequip", new UnequipItemCommand());
        this.getAvailableCommands().put("see", new SeeStatusCommand());
        this.getAvailableCommands().put("flee", new FleeCommand());
        this.getAvailableCommands().put("join", new JoinPartyCommand());
        this.getAvailableCommands().put("leave", new LeavePartyCommand());
        this.getAvailableCommands().put("use", new UsePotionCommand());
        this.getAvailableCommands().put("talk", new TalkCommand());
       // this.getAvailableCommands().put("help", new HelpCommand());
    }

    public CommandState update(Player thePlayer) {
        if (thePlayer.getCurrentLocation() instanceof Blacksmith)
            return new CommerceState();
        return this;
    }
}
