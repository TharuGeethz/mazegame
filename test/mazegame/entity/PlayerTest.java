package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import mazegame.control.CombatSession;

public class PlayerTest {
    
    private Player player;
    private Location location;
    
    @BeforeEach
    public void setUp() {
        player = new Player("TestPlayer");
        location = new Location("Test Room", "TestRoom");
    }
    
    @Test
    public void testPlayerCreation() {
        assertEquals("TestPlayer", player.getName(), "Player name should be set correctly");
        assertNotNull(player.getInventory(), "Player should have inventory");
        assertNotNull(player.getNpcCollection(), "Player should have NPC collection");
        assertNull(player.getCurrentLocation(), "Player should start with no location");
    }
    
    @Test
    public void testLocationManagement() {
        assertNull(player.getCurrentLocation(), "Initial location should be null");
        
        player.setCurrentLocation(location);
        assertEquals(location, player.getCurrentLocation(), "Location should be set correctly");
        
        Location newLocation = new Location("New Room", "NewRoom");
        player.setCurrentLocation(newLocation);
        assertEquals(newLocation, player.getCurrentLocation(), "Location should be updated");
    }
    
    @Test
    public void testCombatSessionManagement() {
        assertFalse(player.inCombat(), "Player should not be in combat initially");
        assertNull(player.getCombatSession(), "Combat session should be null initially");
        
        CombatSession combatSession = new CombatSession(player);
        player.setCombatSession(combatSession);
        assertEquals(combatSession, player.getCombatSession(), "Combat session should be set");
        // inCombat() depends on CombatSession.isOver() implementation
    }
}