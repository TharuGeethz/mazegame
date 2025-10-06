package mazegame.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandResponseTest {

    // Tests for isFinishedGame()
    @Test
    void isFinishedGame_defaultsToFalseForSingleArgConstructor() {
        CommandResponse cr = new CommandResponse("hello");
        assertFalse(cr.isFinishedGame());
    }

    @Test
    void isFinishedGame_trueWhenQuitFlagTrue() {
        CommandResponse cr = new CommandResponse("bye", true);
        assertTrue(cr.isFinishedGame());
    }

    @Test
    void isFinishedGame_falseWhenQuitFlagFalse() {
        CommandResponse cr = new CommandResponse("keep going", false);
        assertFalse(cr.isFinishedGame());
    }

    // Tests for getMessage()
    @Test
    void getMessage_returnsMessageFromSingleArgConstructor() {
        CommandResponse cr = new CommandResponse("welcome");
        assertEquals("welcome", cr.getMessage());
    }

    @Test
    void getMessage_returnsMessageFromTwoArgConstructor() {
        CommandResponse cr = new CommandResponse("quit now", true);
        assertEquals("quit now", cr.getMessage());
    }

    @Test
    void getMessage_canReturnNullIfConstructedWithNull() {
        CommandResponse cr = new CommandResponse(null);
        assertNull(cr.getMessage());
    }

    // Tests for setMessage()
    @Test
    void setMessage_updatesMessage() {
        CommandResponse cr = new CommandResponse("old");
        cr.setMessage("new");
        assertEquals("new", cr.getMessage());
    }

    @Test
    void setMessage_allowsEmptyString() {
        CommandResponse cr = new CommandResponse("non-empty");
        cr.setMessage("");
        assertEquals("", cr.getMessage());
    }

    @Test
    void setMessage_allowsNull() {
        CommandResponse cr = new CommandResponse("something");
        cr.setMessage(null);
        assertNull(cr.getMessage());
    }
}
