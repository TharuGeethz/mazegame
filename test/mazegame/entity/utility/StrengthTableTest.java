package mazegame.entity.utility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrengthTableTest {

    private StrengthTable table;

    @BeforeEach
    void setUp() {
        table = StrengthTable.getInstance();
        table.setModifier(5, -3);
        table.setModifier(10, 0);
        table.setModifier(15, 2);
        table.setModifier(20, 4);
    }

    // Tests for getInstance()
    @Test
    void testGetInstanceNotNull() {
        assertNotNull(StrengthTable.getInstance(), "getInstance() should never return null");
    }

    @Test
    void testGetInstanceReturnsSameInstance() {
        StrengthTable first = StrengthTable.getInstance();
        StrengthTable second = StrengthTable.getInstance();
        assertSame(first, second, "getInstance() should always return the same singleton instance");
    }

    @Test
    void testGetInstanceReflectsChanges() {
        StrengthTable instance = StrengthTable.getInstance();
        instance.setModifier(12, 1);
        assertEquals(1, instance.getModifier(12), "Changes in singleton should persist between calls");
    }

    // Tests for setModifier()
    @Test
    void testSetModifierStoresValue() {
        table.setModifier(8, -1);
        assertEquals(-1, table.getModifier(8), "setModifier() should correctly store new modifier values");
    }

    @Test
    void testSetModifierOverridesExistingValue() {
        table.setModifier(10, 5);
        assertEquals(5, table.getModifier(10), "setModifier() should override an existing modifier value");
    }

    @Test
    void testSetModifierAddsNewEntry() {
        table.setModifier(25, 6);
        assertEquals(6, table.getModifier(25), "setModifier() should add a new key-value pair if it doesn't exist");
    }


    // Tests for getModifier()
    @Test
    void testGetModifierReturnsExactValue() {
        assertEquals(2, table.getModifier(15), "getModifier() should return the correct modifier for an exact match");
    }

}
