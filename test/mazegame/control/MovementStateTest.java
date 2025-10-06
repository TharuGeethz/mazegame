package mazegame.control;

import mazegame.entity.Blacksmith;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovementStateTest {

    private MovementState movementState;

    @BeforeEach
    void setup() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);
        movementState = new MovementState();
    }

    // Tests for constructor (command setup)
    @Test
    void constructor_shouldInitializeCommandsMap() {
        assertNotNull(movementState.getAvailableCommands(), "Available commands map should not be null");
        assertFalse(movementState.getAvailableCommands().isEmpty(), "Commands should be initialized");
    }

    @Test
    void constructor_shouldContainExpectedCoreCommands() {
        assertTrue(movementState.getAvailableCommands().containsKey("go"), "Should contain 'go' command");
        assertTrue(movementState.getAvailableCommands().containsKey("move"), "Should contain 'move' command");
        assertTrue(movementState.getAvailableCommands().containsKey("look"), "Should contain 'look' command");
    }

    @Test
    void constructor_shouldContainCommerceAndCombatCommands() {
        assertTrue(movementState.getAvailableCommands().containsKey("attack"), "Should contain 'attack' command");
        assertTrue(movementState.getAvailableCommands().containsKey("flee"), "Should contain 'flee' command");
        assertTrue(movementState.getAvailableCommands().containsKey("talk"), "Should contain 'talk' command");
    }

    // Tests for update(Player)
    @Test
    void update_shouldReturnCommerceStateIfPlayerInBlacksmith() {
        Player player = new Player("Tharu");
        player.setCurrentLocation(new Blacksmith("Forge of Helan", "A bustling blacksmith shop."));

        CommandState result = movementState.update(player);

        assertTrue(result instanceof CommerceState, "Should transition to CommerceState when in a Blacksmith");
    }

    @Test
    void update_shouldReturnSameInstanceIfNotInBlacksmith() {
        Player player = new Player("Tharu");
        player.setCurrentLocation(new Location("Forest Path", "A quiet forest trail."));

        CommandState result = movementState.update(player);

        assertSame(movementState, result, "Should remain in MovementState if not in a Blacksmith");
    }

    @Test
    void update_shouldHandleNullLocationGracefully() {
        Player player = new Player("Tharu");
        player.setCurrentLocation(null);

        CommandState result = movementState.update(player);

        // Defensive expectation — should not crash
        assertNotNull(result, "Should not return null even if player has no location");
        assertSame(movementState, result, "Should stay in MovementState if location is null");
    }
}
