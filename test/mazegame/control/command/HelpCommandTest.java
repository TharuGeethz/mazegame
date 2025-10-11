package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Blacksmith;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HelpCommandTest {

    private HelpCommand command;
    private Player player;

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);

        command = new HelpCommand();
        player = new Player("Hero");
    }

    @Test
    void helpInShopShowsCommerceNotCombat() {
        player.setCurrentLocation(new Blacksmith("Forge", "Shop"));
        ParsedInput input = new ParsedInput("help", new ArrayList<>(List.of()));
        CommandResponse resp = command.execute(input, player);

        String msg = resp.getMessage();
        assertTrue(msg.contains("You are in a SHOP"));
        assertTrue(msg.contains("COMMERCE COMMANDS"));
        assertTrue(msg.contains("buy <item>"));
        assertTrue(msg.contains("sell <item>"));
        assertFalse(msg.contains("COMBAT COMMANDS"));
        assertTrue(msg.contains("TIP: Use 'look' to see what items are available for purchase."));
    }

    @Test
    void helpInMazeShowsCombatPartyItemSections() {
        player.setCurrentLocation(new Location("Forest", "Maze"));
        ParsedInput input = new ParsedInput("help", new ArrayList<>(List.of()));
        CommandResponse resp = command.execute(input, player);

        String msg = resp.getMessage();
        assertTrue(msg.contains("You are exploring the MAZE"));
        assertTrue(msg.contains("PARTY COMMANDS"));
        assertTrue(msg.contains("join [name]"));
        assertTrue(msg.contains("ITEM COMMANDS"));
        assertTrue(msg.contains("equip <item>"));
        assertTrue(msg.contains("TIP: Visit a shop to buy and sell items."));
    }

    @Test
    void commonSectionsAlwaysPresent() {
        // Check common blocks appear regardless of context
        player.setCurrentLocation(new Location("Hall", "Maze"));
        ParsedInput input = new ParsedInput("help", new ArrayList<>(List.of()));
        CommandResponse resp = command.execute(input, player);
        String msg = resp.getMessage();
        assertTrue(msg.contains("NAVIGATION COMMANDS"));
        assertTrue(msg.contains("INFORMATION COMMANDS"));
        assertTrue(msg.contains("list"));
        assertTrue(msg.contains("help"));
        assertTrue(msg.contains("OTHER COMMANDS"));
        assertTrue(msg.contains("quit"));
    }
}
