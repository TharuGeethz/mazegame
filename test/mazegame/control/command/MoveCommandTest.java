package mazegame.control.command;

import mazegame.control.CombatSession;
import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Exit;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MoveCommandTest {

    private MoveCommand command;
    private Player player;
    private Location start;
    private Location forest;
    private Exit northExit;

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);

        command = new MoveCommand();
        player = new Player("Hero");

        start = new Location("Town Square", "Busy market area");
        forest = new Location("Forest", "Dark, quiet forest");

        northExit = new Exit("north", forest);
        start.getExitCollection().addExit("north", northExit);
        player.setCurrentLocation(start);
    }

    @Test
    void testCannotMoveDuringCombat() {
        // mock isOver()
        CombatSession mockSession = mock(CombatSession.class);
        when(mockSession.isOver()).thenReturn(false);
        player.setCombatSession(mockSession);

        ParsedInput input = new ParsedInput("move", new ArrayList<>(List.of("north")));
        CommandResponse resp = command.execute(input, player);

        assertEquals("You can't move while in combat!", resp.getMessage());
    }

    @Test
    void testNoDirectionProvided() {
        ParsedInput input = new ParsedInput("move", new ArrayList<>());
        CommandResponse resp = command.execute(input, player);

        assertEquals("If you want to move you need to tell me where.", resp.getMessage());
    }

    @Test
    void testSuccessfulMoveToAnotherLocation() {
        ParsedInput input = new ParsedInput("move", new ArrayList<>(List.of("north")));
        CommandResponse resp = command.execute(input, player);

        assertEquals(forest, player.getCurrentLocation());
        assertTrue(resp.getMessage().contains("You successfully move north"));
        assertTrue(resp.getMessage().contains("Forest"));
    }
}
