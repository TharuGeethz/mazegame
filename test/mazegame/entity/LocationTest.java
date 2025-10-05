package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class LocationTest {
    
    private Location location;
    private NonPlayerCharacter friendly;
    private NonPlayerCharacter hostile;
    
    @BeforeEach
    public void setUp() {
        location = new Location("A peaceful meadow with flowers", "Meadow");
        friendly = new NonPlayerCharacter("Guide", false);
        hostile = new NonPlayerCharacter("Orc", true);
    }
    
    @Test
    public void testLocationCreation() {
        assertEquals("A peaceful meadow with flowers", location.getDescription(), "Description should be set");
        assertEquals("Meadow", location.getLabel(), "Label should be set");
        assertNotNull(location.getInventory(), "Location should have inventory");
        assertNotNull(location.getNpcCollection(), "Location should have NPC collection");
        assertNotNull(location.getExitCollection(), "Location should have exit collection");
    }
    
    @Test
    public void testDescriptionAndLabelManagement() {
        location.setDescription("A dark forest");
        assertEquals("A dark forest", location.getDescription(), "Description should be updated");
        
        location.setLabel("Forest");
        assertEquals("Forest", location.getLabel(), "Label should be updated");
    }
    
    @Test
    public void testNPCManagement() {
        List<NonPlayerCharacter> npcs = new ArrayList<>();
        npcs.add(friendly);
        npcs.add(hostile);
        
        location.setNpcs(npcs, true); // Set as hostile collection
        assertTrue(location.getNpcCollection().hasHostileNPCs(), "Should be marked as hostile collection");
        assertEquals(2, location.getNpcCollection().size(), "Should contain both NPCs");
        assertTrue(location.getNpcCollection().hasNPC("guide"), "Should contain guide (case insensitive)");
        assertTrue(location.getNpcCollection().hasNPC("orc"), "Should contain orc (case insensitive)");
    }
}