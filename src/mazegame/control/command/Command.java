package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;

public interface Command {
        public abstract CommandResponse execute(ParsedInput userInput, Player thePlayer);
}
