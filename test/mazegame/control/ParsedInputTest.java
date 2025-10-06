package mazegame.control;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ParsedInputTest {

    // Tests for no-args constructor
    @Test
    void noArgsConstructor_shouldInitializeCommandAsEmpty() {
        ParsedInput input = new ParsedInput();
        assertEquals("", input.getCommand(), "Default command should be empty string");
    }

    @Test
    void noArgsConstructor_shouldInitializeArgumentsAsEmptyList() {
        ParsedInput input = new ParsedInput();
        assertNotNull(input.getArguments(), "Arguments list should not be null");
        assertTrue(input.getArguments().isEmpty(), "Arguments list should be empty");
    }

    @Test
    void noArgsConstructor_shouldCreateIndependentInstances() {
        ParsedInput input1 = new ParsedInput();
        ParsedInput input2 = new ParsedInput();
        assertNotSame(input1.getArguments(), input2.getArguments(), "Each instance should have its own arguments list");
    }

    // Tests for full-args constructor
    @Test
    void fullArgsConstructor_shouldStoreCommandProperly() {
        ArrayList<String> args = new ArrayList<>();
        args.add("north");
        ParsedInput input = new ParsedInput("move", args);
        assertEquals("move", input.getCommand());
    }

    @Test
    void fullArgsConstructor_shouldStoreArgumentsProperly() {
        ArrayList<String> args = new ArrayList<>();
        args.add("gold");
        args.add("sword");
        ParsedInput input = new ParsedInput("pick", args);
        assertEquals(2, input.getArguments().size(), "Should store all provided arguments");
    }


    // Tests for getCommand()
    @Test
    void getCommand_shouldReturnSetValue() {
        ParsedInput input = new ParsedInput();
        input.setCommand("look");
        assertEquals("look", input.getCommand());
    }

    @Test
    void getCommand_shouldHandleNullCommand() {
        ParsedInput input = new ParsedInput();
        input.setCommand(null);
        assertNull(input.getCommand());
    }

    @Test
    void getCommand_shouldReturnUpdatedValueAfterChange() {
        ParsedInput input = new ParsedInput("move", new ArrayList<>());
        input.setCommand("attack");
        assertEquals("attack", input.getCommand());
    }

    // Tests for setCommand()
    @Test
    void setCommand_shouldReplaceExistingCommand() {
        ParsedInput input = new ParsedInput("look", new ArrayList<>());
        input.setCommand("quit");
        assertEquals("quit", input.getCommand());
    }

    @Test
    void setCommand_shouldAllowEmptyString() {
        ParsedInput input = new ParsedInput();
        input.setCommand("");
        assertEquals("", input.getCommand());
    }

    @Test
    void setCommand_shouldAllowWhitespaceCommand() {
        ParsedInput input = new ParsedInput();
        input.setCommand("   ");
        assertEquals("   ", input.getCommand());
    }

    // Tests for getArguments()
    @Test
    void getArguments_shouldReturnEmptyListIfNoArguments() {
        ParsedInput input = new ParsedInput();
        assertTrue(input.getArguments().isEmpty());
    }

    @Test
    void getArguments_shouldReturnSameReferenceAfterSet() {
        ArrayList<String> args = new ArrayList<>();
        ParsedInput input = new ParsedInput();
        input.setArguments(args);
        assertSame(args, input.getArguments());
    }

    @Test
    void getArguments_shouldAllowMixedTypeArguments() {
        ArrayList<Object> args = new ArrayList<>();
        args.add("north");
        args.add(123);
        args.add(true);
        ParsedInput input = new ParsedInput("test", args);
        assertEquals(3, input.getArguments().size());
    }

    // Tests for setArguments()
    @Test
    void setArguments_shouldReplaceList() {
        ParsedInput input = new ParsedInput();
        ArrayList<String> args = new ArrayList<>();
        args.add("key");
        input.setArguments(args);
        assertEquals(args, input.getArguments());
    }

    @Test
    void setArguments_shouldHandleNull() {
        ParsedInput input = new ParsedInput();
        input.setArguments(null);
        assertNull(input.getArguments());
    }

    @Test
    void setArguments_shouldAllowEmptyList() {
        ParsedInput input = new ParsedInput();
        input.setArguments(new ArrayList<>());
        assertTrue(input.getArguments().isEmpty());
    }
}
