package mazegame.control.command;

import mazegame.control.CombatSession;
import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.item.Item;
import mazegame.entity.item.Weapon;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetItemCommandTest {

    private GetItemCommand command;
    private Player player;
    private Location location;
    private Item sword;

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);
        weightLimitTable.setModifier(20, 266);

        command = new GetItemCommand();
        player = new Player("Hero");
        location = new Location("Forge", "A fiery forge full of weapons");
        player.setCurrentLocation(location);

        sword = new Weapon("sword", 10, 3, "1d6");
        location.getInventory().addItem(sword);

        // ensure the player can carry enough weight
        player.setStrength(20);
    }

    @Test
    void testSuccessfulItemPickup() {
        ParsedInput input = new ParsedInput("get", new ArrayList<>(List.of("sword")));
        CommandResponse response = command.execute(input, player);

        assertTrue(player.getInventory().hasItem("sword"));
        assertFalse(location.getInventory().getItemList().containsKey("sword"));
        assertEquals("sword has been collected successfully.", response.getMessage());
    }

    @Test
    void testPickupWhileInCombat() {

        // mock isOver()
        CombatSession mockSession = mock(CombatSession.class);
        when(mockSession.isOver()).thenReturn(false);
        player.setCombatSession(mockSession);
        ParsedInput input = new ParsedInput("get", new ArrayList<>(List.of("sword")));
        CommandResponse response = command.execute(input, player);

        assertEquals("You can't pick up items while in combat!", response.getMessage());
    }

    @Test
    void testItemNotFoundInLocation() {
        ParsedInput input = new ParsedInput("get", new ArrayList<>(List.of("shield")));
        CommandResponse response = command.execute(input, player);

        assertEquals("shield is not there in current location.", response.getMessage());
    }
}
