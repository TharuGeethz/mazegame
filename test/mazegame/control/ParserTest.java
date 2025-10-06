package mazegame.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    // Tests for constructor behavior (validated via parse)

    @Test
    void constructor_usesProvidedValidCommands_recognizesCommand() {
        Parser parser = new Parser(new ArrayList<>(java.util.List.of("look", "go", "move")));
        ParsedInput out = parser.parse("look around");
        assertEquals("look", out.getCommand());
        assertEquals(1, out.getArguments().size());
        assertEquals("around", out.getArguments().get(0));
    }

    @Test
    void constructor_initializesDropWords_dropsSingleDropWordToken() {
        Parser parser = new Parser(new ArrayList<>(java.util.List.of("look", "go", "move")));
        ParsedInput out = parser.parse("the");
        assertEquals("", out.getCommand());
        assertTrue(out.getArguments().isEmpty(), "Drop word should not be added as argument");
    }

    @Test
    void constructor_respectsValidCommands_goIsRecognized() {
        Parser parser = new Parser(new ArrayList<>(java.util.List.of("look", "go", "move")));
        ParsedInput out = parser.parse("go north");
        assertEquals("go", out.getCommand());
        assertEquals(1, out.getArguments().size());
        assertEquals("north", out.getArguments().get(0));
    }

    // Tests for parse(String)

    @Test
    void parse_isCaseInsensitive_forCommandAndLowersArguments() {
        Parser parser = new Parser(new ArrayList<>(java.util.List.of("look", "go", "move")));
        ParsedInput out = parser.parse("LoOk NORTH");
        assertEquals("look", out.getCommand());
        assertEquals(1, out.getArguments().size());
        assertEquals("north", out.getArguments().get(0));
    }

    @Test
    void parse_whenNoValidCommand_entireInputBecomesSingleArgumentUnlessDropWord() {
        Parser parser = new Parser(new ArrayList<>(java.util.List.of("look", "go", "move")));
        ParsedInput out = parser.parse("north");
        assertEquals("", out.getCommand());
        assertEquals(1, out.getArguments().size());
        assertEquals("north", out.getArguments().get(0));
    }

    @Test
    void parse_splitsIntoAtMostTwoTokens_secondTokenKeptWhole_andDropWordsIgnoredIfExact() {
        Parser parser = new Parser(new ArrayList<>(java.util.List.of("look", "go", "move")));

        // Two-token behavior: only first word and the entire remainder
        ParsedInput out1 = parser.parse("move to the dark cave");
        assertEquals("move", out1.getCommand());
        assertEquals(1, out1.getArguments().size());
        assertEquals("to the dark cave", out1.getArguments().get(0));

        // If the second token is exactly a drop word, it should be ignored
        ParsedInput out2 = parser.parse("look the");
        assertEquals("look", out2.getCommand());
        assertTrue(out2.getArguments().isEmpty(), "Exact drop-word second token should be ignored");
    }
}
