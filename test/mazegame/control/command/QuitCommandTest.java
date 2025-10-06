package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuitCommandTest {

    private QuitCommand command;
    private Player player;

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);
        command = new QuitCommand();
        player = new Player("Hero");
    }

    @Test
    void testQuitCommandEndsGame() {
        ParsedInput input = new ParsedInput("quit", new ArrayList<>(List.of()));
        CommandResponse response = command.execute(input, player);

        assertTrue(response.isFinishedGame());
    }

    @Test
    void testQuitCommandExactMessage() {
        ParsedInput input = new ParsedInput("quit", new ArrayList<>(List.of()));
        CommandResponse response = command.execute(input, player);

        assertEquals("Thanks for playing --- Goodbye", response.getMessage());
    }
}
