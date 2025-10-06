package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;
import mazegame.entity.item.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnequipItemCommandTest {

    private UnequipItemCommand command;
    private Player player;

    @BeforeEach
    void setUp() {
        command = new UnequipItemCommand();
        player = new Player("Hero");
        player.getInventory().addItem(new Weapon("sword", 10, 5, "1d6"));
        player.equipItem("sword");
    }

    @Test
    void testUnequipItemSuccessfully() {
        ParsedInput input = new ParsedInput("unequip", new ArrayList<>(List.of("sword")));
        CommandResponse response = command.execute(input, player);

        assertFalse(player.getWearingItems().containsKey("sword"));
        assertTrue(player.getInventory().hasItem("sword"));
        assertEquals("sword unequipped", response.getMessage());
    }

    @Test
    void testUnequipWithoutItemName() {
        ParsedInput input = new ParsedInput("unequip", new ArrayList<>(List.of()));
        CommandResponse response = command.execute(input, player);

        assertEquals("you need to tell me what you want to unequip", response.getMessage());
    }

    @Test
    void testUnequipItemNotWorn() {
        ParsedInput input = new ParsedInput("unequip", new ArrayList<>(List.of("shield")));
        CommandResponse response = command.execute(input, player);

        assertEquals("you are not wearing shield", response.getMessage());
    }
}
