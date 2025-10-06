package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Location;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TalkCommandTest {

    private TalkCommand command;
    private Player player;
    private Location location;

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);
        command = new TalkCommand();
        player = new Player("Hero");
        location = new Location("Forest", "Dark forest with strange figures");
        player.setCurrentLocation(location);
    }

    @Test
    void testTalkWhenNoNPCsInLocation() {
        ParsedInput input = new ParsedInput("talk", new ArrayList<>(List.of()));
        CommandResponse resp = command.execute(input, player);
        assertEquals("There is no one here to talk to.", resp.getMessage());
    }

    @Test
    void testTalkToNonexistentNPC() {
        ParsedInput input = new ParsedInput("talk", new ArrayList<>(List.of("ghost")));
        CommandResponse resp = command.execute(input, player);
        assertEquals("There is no one with that name to talk to.", resp.getMessage());
    }

    @Test
    void testTalkToFriendlyNPC() {
        NonPlayerCharacter ally = new NonPlayerCharacter("Luna", 10, 8, 25, "Hello there! ", false);
        location.getNpcCollection().put("Luna", ally);

        ParsedInput input = new ParsedInput("talk", new ArrayList<>(List.of("Luna")));
        CommandResponse resp = command.execute(input, player);

        assertNotNull(resp);
        assertTrue(resp.getMessage().isEmpty(), "Friendly talk should return an empty CommandResponse message");
    }

    @Test
    void testTalkToHostileNPC() {
        NonPlayerCharacter bandit = new NonPlayerCharacter("Rufus", 12, 10, 30, "Grrr! ", true);
        location.getNpcCollection().put("Rufus", bandit);

        ParsedInput input = new ParsedInput("talk", new ArrayList<>(List.of("Rufus")));
        CommandResponse resp = command.execute(input, player);

        assertNotNull(resp);
        assertTrue(resp.getMessage().isEmpty(), "Hostile talk should return an empty CommandResponse message");
    }
}
