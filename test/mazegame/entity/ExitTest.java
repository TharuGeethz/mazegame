package mazegame.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExitTest {

    private Exit exit;
    private Location dummyDestination;

    @BeforeEach
    void setUp() {
        dummyDestination = new Location("A test room", "Test Chamber");
        exit = new Exit("A door to the chamber", dummyDestination);
    }

    // Tests for setLocked(boolean)

    @Test
    void testSetLockedTrue() {
        exit.setLocked(true);
        assertTrue(exit.isLocked(), "Exit should be locked after calling setLocked(true)");
    }

    @Test
    void testSetLockedFalse() {
        exit.setLocked(false);
        assertFalse(exit.isLocked(), "Exit should be unlocked after calling setLocked(false)");
    }

    @Test
    void testSetLockedChangesState() {
        exit.setLocked(false);
        exit.setLocked(true);
        assertTrue(exit.isLocked(), "Exit lock state should change to true when setLocked(true) is called after false");
    }

    // Tests for isLocked()

    @Test
    void testIsLockedDefaultValue() {
        assertFalse(exit.isLocked(), "Exit should be unlocked by default");
    }

    @Test
    void testIsLockedAfterLocking() {
        exit.setLocked(true);
        assertTrue(exit.isLocked(), "isLocked() should return true after exit is locked");
    }

    @Test
    void testIsLockedAfterUnlocking() {
        exit.setLocked(true);
        exit.setLocked(false);
        assertFalse(exit.isLocked(), "isLocked() should return false after exit is unlocked");
    }
}
