package mazegame.control;

import mazegame.control.command.Command;
import mazegame.entity.Player;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandStateTest {

    private CommandState commandState;

    // A simple concrete subclass to test the abstract class
    static class TestCommandState extends CommandState {
        @Override
        public CommandState update(Player thePlayer) {
            return this;
        }
    }

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);

        commandState = new TestCommandState();
    }

    // Tests for getAvailableCommands()
    @Test
    void getAvailableCommands_returnsEmptyInitially() {
        assertTrue(commandState.getAvailableCommands().isEmpty());
    }

    @Test
    void getAvailableCommands_reflectsAddedCommands() {
        Command mockCmd = mock(Command.class);
        commandState.getAvailableCommands().put("move", mockCmd);
        assertEquals(1, commandState.getAvailableCommands().size());
    }

    @Test
    void getAvailableCommands_returnsSameMapReference() {
        HashMap<String, Command> map = commandState.getAvailableCommands();
        map.put("test", mock(Command.class));
        assertEquals(map, commandState.getAvailableCommands());
    }

    // Tests for getCommand()
    @Test
    void getCommand_returnsCommandWhenPresent() {
        Command mockCmd = mock(Command.class);
        commandState.getAvailableCommands().put("attack", mockCmd);
        assertEquals(mockCmd, commandState.getCommand("attack"));
    }

    @Test
    void getCommand_returnsNullWhenNotPresent() {
        assertNull(commandState.getCommand("missing"));
    }

    @Test
    void getCommand_isCaseSensitive() {
        Command mockCmd = mock(Command.class);
        commandState.getAvailableCommands().put("LOOK", mockCmd);
        assertNull(commandState.getCommand("look"));
    }

    // Tests for getLabels()
    @Test
    void getLabels_returnsEmptyListWhenNoCommands() {
        assertTrue(commandState.getLabels().isEmpty());
    }

    @Test
    void getLabels_returnsAllCommandKeys() {
        commandState.getAvailableCommands().put("go", mock(Command.class));
        commandState.getAvailableCommands().put("quit", mock(Command.class));

        ArrayList<String> labels = commandState.getLabels();
        assertTrue(labels.contains("go"));
        assertTrue(labels.contains("quit"));
    }

    @Test
    void getLabels_returnsNewListCopy() {
        commandState.getAvailableCommands().put("test", mock(Command.class));
        ArrayList<String> labels1 = commandState.getLabels();
        ArrayList<String> labels2 = commandState.getLabels();
        assertNotSame(labels1, labels2);
    }
}
