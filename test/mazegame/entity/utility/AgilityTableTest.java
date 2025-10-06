package mazegame.entity.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgilityTableTest {

    private AgilityTable table;

    @BeforeEach
    void setUp() {
        // Reset instance manually
        table = AgilityTable.getInstance();
        table.setModifier(10, 0);
        table.setModifier(15, 2);
        table.setModifier(20, 4);
    }

    // Tests for getInstance()
    @Test
    void testGetInstanceNotNull() {
        assertNotNull(AgilityTable.getInstance(), "getInstance should not return null");
    }

    @Test
    void testGetInstanceReturnsSameInstance() {
        AgilityTable instance1 = AgilityTable.getInstance();
        AgilityTable instance2 = AgilityTable.getInstance();
        assertSame(instance1, instance2, "getInstance should always return the same instance");
    }

    @Test
    void testGetInstanceAllowsAccessToSetValues() {
        AgilityTable instance = AgilityTable.getInstance();
        instance.setModifier(12, 1);
        assertEquals(1, instance.getModifier(12), "Newly set modifier should be retrievable from the singleton");
    }

    // Tests for setModifier(int agility, int modifier)
    @Test
    void testSetModifierStoresCorrectValue() {
        table.setModifier(8, -1);
        assertEquals(-1, table.getModifier(8), "setModifier should store the correct modifier value");
    }

    @Test
    void testSetModifierOverridesExistingValue() {
        table.setModifier(10, 5);
        assertEquals(5, table.getModifier(10), "setModifier should override previous value for same agility");
    }

    @Test
    void testSetModifierForNewAgilityAddsEntry() {
        table.setModifier(25, 6);
        assertEquals(6, table.getModifier(25), "setModifier should add a new agility-modifier pair");
    }

    // Tests for getModifier(int agility)
    @Test
    void testGetModifierReturnsExactValueWhenExists() {
        assertEquals(2, table.getModifier(15), "getModifier should return the correct modifier for existing agility");
    }

    @Test
    void testGetModifierReturnsMaxValueWhenAboveMaxAgility() {
        // 20 is max defined, so agility 30 should return same modifier as 20
        assertEquals(4, table.getModifier(30), "Agility above max should return modifier of max agility");
    }

    @Test
    void testGetModifierReturnsMinusOneWhenAgilityNotFound() {
        AgilityTable newTable = AgilityTable.getInstance();
        newTable.setModifier(5, -2);
        assertEquals(-1, newTable.getModifier(7), "Unknown agility value should return -1");
    }
}
