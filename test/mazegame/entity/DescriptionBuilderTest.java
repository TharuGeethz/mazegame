package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.ArrayList;

public class DescriptionBuilderTest {
    
    private DescriptionBuilder builder;
    
    @BeforeEach
    public void setUp() {
        builder = new DescriptionBuilder();
    }
    
    @Test
    public void testLineMethod() {
        builder.line("First line").line("Second line");
        String result = builder.build();
        assertEquals("First line\nSecond line\n", result, "Lines should be added with newlines");
        
        // Test method chaining
        DescriptionBuilder chainedBuilder = new DescriptionBuilder();
        DescriptionBuilder returned = chainedBuilder.line("Test");
        assertSame(chainedBuilder, returned, "line() should return same instance for chaining");
    }
    
    @Test
    public void testSectionMethod() {
        builder.section("Health:", "100/100")
              .section("Mana:", "50/50")
              .section("Empty:", "")
              .section("Null:", null);
        
        String result = builder.build();
        assertTrue(result.contains("Health: 100/100\n"), "Should include non-empty sections");
        assertTrue(result.contains("Mana: 50/50\n"), "Should include second section");
        assertFalse(result.contains("Empty:"), "Should skip sections with empty content");
        assertFalse(result.contains("Null:"), "Should skip sections with null content");
    }
    
    @Test
    public void testSectionBlockMethod() {
        builder.sectionBlock("Items:", Arrays.asList("Sword", "Shield", "Potion"))
              .sectionBlock("Empty List:", new ArrayList<>())
              .sectionBlock("Null List:", null);
        
        String result = builder.build();
        assertTrue(result.contains("Items:\n"), "Should include section title");
        assertTrue(result.contains("Sword\n"), "Should include list items");
        assertTrue(result.contains("Shield\n"), "Should include all list items");
        assertFalse(result.contains("Empty List:"), "Should skip empty lists");
        assertFalse(result.contains("Null List:"), "Should skip null lists");
    }
}