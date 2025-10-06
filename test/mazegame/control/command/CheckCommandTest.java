package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;
import mazegame.entity.utility.NonPlayerCharacterCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckCommandTest {

    private CheckCommand command;
    private Player player;
    private NonPlayerCharacter ally;

    @BeforeEach
    void setUp() {
        command = new CheckCommand();
        player = new Player("Hero");
        ally = new NonPlayerCharacter("Luna", 12, 10, 25, "Stay close!", false);
        NonPlayerCharacterCollection npcCollection = new NonPlayerCharacterCollection();
        npcCollection.put("luna", ally);
        player.setNpcCollection(npcCollection);
    }

    @Test
    void testNoAllies() {
        player.getNpcCollection().clear();
        ParsedInput input = new ParsedInput("check", new ArrayList<>(List.of("Luna")));
        CommandResponse response = command.execute(input, player);

        assertEquals("You don't have any ally with you currently!", response.getMessage());
    }

    @Test
    void testAskForAllyNameWhenNoArgument() {
        ParsedInput input = new ParsedInput("check", new ArrayList<>());
        CommandResponse response = command.execute(input, player);

        assertEquals("Which ally do you want to check? ", response.getMessage());
    }

    @Test
    void testCheckExistingAlly() {
        ParsedInput input = new ParsedInput("check", new ArrayList<>(List.of("Luna")));
        CommandResponse response = command.execute(input, player);

        assertTrue(response.getMessage().contains("Luna"));
        assertTrue(response.getMessage().contains("Strength"));
    }
}
