package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;
import mazegame.entity.item.Weapon;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListItemsCommandTest {

    private ListItemsCommand command;
    private Player player;

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);

        command = new ListItemsCommand();
        player = new Player("Hero");
    }

    @Test
    void testEmptyInventoryAndNoWearingItems() {
        ParsedInput input = new ParsedInput("list", new ArrayList<>(List.of()));
        CommandResponse resp = command.execute(input, player);

        String msg = resp.getMessage();
        assertTrue(msg.contains("No items held"));
        assertTrue(msg.contains("No wearing items"));
    }

    @Test
    void testItemsHeldButNoWearingItems() {
        player.getInventory().addItem(new Weapon("dagger", 5, 2, "1d4"));
        ParsedInput input = new ParsedInput("list", new ArrayList<>(List.of()));
        CommandResponse resp = command.execute(input, player);

        String msg = resp.getMessage();
        assertTrue(msg.contains("Items held"));
        assertTrue(msg.contains("dagger"));
        assertTrue(msg.contains("No wearing items"));
    }

    @Test
    void testWearingItemsDisplayed() {
        Weapon sword = new Weapon("sword", 10, 5, "1d6");
        player.getInventory().addItem(sword);
        player.equipItem("sword");

        ParsedInput input = new ParsedInput("list", new ArrayList<>(List.of()));
        CommandResponse resp = command.execute(input, player);

        String msg = resp.getMessage();
        assertTrue(msg.contains("Items wearing"));
        assertTrue(msg.contains("Weapons"));
        assertTrue(msg.contains("sword"));
    }
}
