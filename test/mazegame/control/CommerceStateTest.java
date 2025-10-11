package mazegame.control;

import mazegame.control.command.*;
import mazegame.entity.Blacksmith;
import mazegame.entity.Location;
import mazegame.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommerceStateTest {

    private CommerceState commerceState;

    @BeforeEach
    void setUp() {
        commerceState = new CommerceState();
    }

    // Tests for Constructor setup
    @Test
    void constructor_shouldInitializeAllExpectedCommands() {
        assertTrue(commerceState.getAvailableCommands().containsKey("buy"));
        assertTrue(commerceState.getAvailableCommands().containsKey("sell"));
        assertTrue(commerceState.getAvailableCommands().containsKey("move"));
        assertTrue(commerceState.getAvailableCommands().containsKey("look"));
        assertTrue(commerceState.getAvailableCommands().containsKey("quit"));
        assertTrue(commerceState.getAvailableCommands().containsKey("list"));
        assertTrue(commerceState.getAvailableCommands().containsKey("see"));
        assertTrue(commerceState.getAvailableCommands().containsKey("help"));
    }

    @Test
    void constructor_shouldMapCorrectCommandClasses() {
        assertInstanceOf(BuyCommand.class, commerceState.getCommand("buy"));
        assertInstanceOf(SellCommand.class, commerceState.getCommand("sell"));
        assertInstanceOf(MoveCommand.class, commerceState.getCommand("move"));
    }

    @Test
    void constructor_shouldCreateNonEmptyCommandMap() {
        assertFalse(commerceState.getAvailableCommands().isEmpty());
        assertEquals(10, commerceState.getAvailableCommands().size());
    }

    // Tests for update(Player)
    @Test
    void update_returnsSameStateIfPlayerInBlacksmith() {
        Player player = new Player("Hero");
        player.setCurrentLocation(new Blacksmith("Shop of Wonders", "Blacksmith Shop"));

        CommandState result = commerceState.update(player);
        assertSame(commerceState, result);
    }

    @Test
    void update_returnsMovementStateIfNotInBlacksmith() {
        Player player = new Player("Hero");
        player.setCurrentLocation(new Location("Forest", "Forest Clearing"));

        CommandState result = commerceState.update(player);
        assertInstanceOf(MovementState.class, result);
    }

    @Test
    void update_doesNotModifyCommandMap() {
        Player player = new Player("Hero");
        player.setCurrentLocation(new Location("Cave", "Crystal Cave"));

        commerceState.update(player);
        assertTrue(commerceState.getAvailableCommands().containsKey("buy"));
        assertTrue(commerceState.getAvailableCommands().containsKey("sell"));
    }
}
