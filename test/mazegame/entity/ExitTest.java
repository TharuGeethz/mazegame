package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ExitTest {
    
    private Exit exit;
    private Location destination;
    
    @BeforeEach
    public void setUp() {
        destination = new Location("A peaceful garden", "Garden");
        exit = new Exit("A wooden door leading to the garden", destination);
    }
    
    @Test
    public void testExitConstructorAndGetters() {
        assertNotNull(exit, "Exit should be created successfully");
        assertEquals("A wooden door leading to the garden", exit.getDescription(), "Description should be set correctly");
        assertEquals(destination, exit.getDestination(), "Destination should be set correctly");
        assertFalse(exit.isLocked(), "Exit should be unlocked by default");
    }
    
    @Test
    public void testSetDestinationAndDescription() {
        Location newDestination = new Location("A dark cave", "Cave");
        exit.setDestination(newDestination);
        assertEquals(newDestination, exit.getDestination(), "setDestination should update destination");
        
        exit.setDescription("A steel gate");
        assertEquals("A steel gate", exit.getDescription(), "setDescription should update description");
    }
    
    @Test
    public void testLockingMechanism() {
        assertFalse(exit.isLocked(), "Exit should be unlocked by default");
        
        exit.setLocked(true);
        assertTrue(exit.isLocked(), "setLocked(true) should lock the exit");
        
        exit.setLocked(false);
        assertFalse(exit.isLocked(), "setLocked(false) should unlock the exit");
    }
}