package mazegame.control;

import mazegame.control.command.Command;
import mazegame.entity.Player;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedConstruction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandHandlerTest {

    private CommandHandler handler;
    private Player player;

    @BeforeEach
    void setup() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);
        handler = new CommandHandler();
        player = new Player("Hero");
    }

    // Utility: set private availableCommands via reflection
    private void setAvailableCommands(CommandState state) throws Exception {
        Field f = CommandHandler.class.getDeclaredField("availableCommands");
        f.setAccessible(true);
        f.set(handler, state);
    }

    // Utility: fake command that returns a fixed response
    private static class OkCommand implements Command {
        @Override public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {
            return new CommandResponse("OK");
        }
    }

    // Tests for constructor
    @Test
    void constructor_initializesNonNullHandler() {
        assertNotNull(handler);
    }

    @Test
    void constructor_setsInitialStateToMovementState() throws Exception {
        Field f = CommandHandler.class.getDeclaredField("availableCommands");
        f.setAccessible(true);
        Object state = f.get(handler);
        assertNotNull(state);
        assertEquals(MovementState.class, state.getClass(), "Initial state should be MovementState");
    }

    @Test
    void constructor_doesNotThrowWhenProcessingSimpleInput() {
        CommandState fakeState = mock(CommandState.class);
        when(fakeState.update(any())).thenReturn(fakeState);
        when(fakeState.getLabels()).thenReturn(new ArrayList<>(List.of("noop")));
        when(fakeState.getCommand(anyString())).thenReturn(null);

        try (MockedConstruction<Parser> mocked = mockConstruction(Parser.class,
                (parser, ctx) -> when(parser.parse(anyString()))
                        .thenReturn(new ParsedInput("noop", new java.util.ArrayList<>())))) {
            assertDoesNotThrow(() -> {
                try {
                    setAvailableCommands(fakeState);
                    CommandResponse resp = handler.processTurn("noop", player);
                    assertEquals("Not a valid command", resp.getMessage());
                } catch (Exception e) {
                    fail(e);
                }
            });
        }
    }

    // Tests for processTurn(String, Player)
    @Test
    void processTurn_invokesUpdateOnCurrentState() throws Exception {
        CommandState fakeState = mock(CommandState.class);
        when(fakeState.update(any())).thenReturn(fakeState);
        when(fakeState.getLabels()).thenReturn(new ArrayList<>(List.of("noop")));
        when(fakeState.getCommand(anyString())).thenReturn(null);

        try (MockedConstruction<Parser> mocked = mockConstruction(Parser.class,
                (parser, ctx) -> when(parser.parse(anyString()))
                        .thenReturn(new ParsedInput("noop", new java.util.ArrayList<>())))) {
            setAvailableCommands(fakeState);
            handler.processTurn("noop", player);
            verify(fakeState, times(1)).update(player);
        }
    }

    @Test
    void processTurn_returnsNotValidCommandWhenCommandMissing() throws Exception {
        CommandState fakeState = mock(CommandState.class);
        when(fakeState.update(any())).thenReturn(fakeState);
        when(fakeState.getLabels()).thenReturn(new ArrayList<>(List.of("unknown")));
        when(fakeState.getCommand(anyString())).thenReturn(null);

        try (MockedConstruction<Parser> mocked = mockConstruction(Parser.class,
                (parser, ctx) -> when(parser.parse(anyString()))
                        .thenReturn(new ParsedInput("unknown", new java.util.ArrayList<>())))) {
            setAvailableCommands(fakeState);
            CommandResponse resp = handler.processTurn("unknown", player);
            assertEquals("Not a valid command", resp.getMessage());
        }
    }

    @Test
    void processTurn_executesResolvedCommandAndReturnsResponse() throws Exception {
        CommandState fakeState = mock(CommandState.class);
        when(fakeState.update(any())).thenReturn(fakeState);
        when(fakeState.getLabels()).thenReturn(new ArrayList<>(List.of("go")));
        when(fakeState.getCommand(eq("go"))).thenReturn(new OkCommand());

        try (MockedConstruction<Parser> mocked = mockConstruction(Parser.class,
                (parser, ctx) -> when(parser.parse(eq("go north")))
                        .thenReturn(new ParsedInput("go", new java.util.ArrayList<>(java.util.List.of("north")))))) {
            setAvailableCommands(fakeState);
            CommandResponse resp = handler.processTurn("go north", player);
            assertEquals("OK", resp.getMessage());
        }
    }
}
