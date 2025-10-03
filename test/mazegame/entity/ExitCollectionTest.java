package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ExitCollectionTest {
    
    private ExitCollection exitCollection;
    private Exit northExit;
    private Exit southExit;
    private Location destination1;
    private Location destination2;
    
    @BeforeEach
    public void setUp() {
        exitCollection = new ExitCollection();
        destination1 = new Location("North room", "North");
        destination2 = new Location("South room", "South");
        northExit = new Exit("Door to the north", destination1);
        southExit = new Exit("Door to the south", destination2);
    }
    
    @Test
    public void testAddExitAndRetrieval() {
        assertTrue(exitCollection.addExit("north", northExit), "Adding new exit should return true");
        assertEquals(northExit, exitCollection.getExit("north"), "Exit should be retrievable after adding");
        
        assertFalse(exitCollection.addExit("north", southExit), "Adding duplicate exit should return false");
        assertEquals(northExit, exitCollection.getExit("north"), "Original exit should remain unchanged");
    }
    
    @Test
    public void testContainsExitFunctionality() {
        assertFalse(exitCollection.containsExit("north"), "containsExit should return false for non-existing exit");
        
        exitCollection.addExit("north", northExit);
        assertTrue(exitCollection.containsExit("north"), "containsExit should return true for existing exit");
        assertFalse(exitCollection.containsExit("NORTH"), "containsExit should be case sensitive");
    }
    
    @Test
    public void testAvailableExitsFormatting() {
        assertEquals("", exitCollection.availableExits(), "availableExits should return empty string for empty collection");
        
        exitCollection.addExit("north", northExit);
        assertEquals("[ north ]", exitCollection.availableExits(), "availableExits should format single exit correctly");
        
        exitCollection.addExit("south", southExit);
        String result = exitCollection.availableExits();
        assertTrue(result.contains("[ north ]") && result.contains("[ south ]"), "Result should contain both exits");
    }
}