package mazegame.entity.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeightLimitTest {

    private WeightLimit weightLimit;

    @BeforeEach
    void setUp() {
        weightLimit = WeightLimit.getInstance();
        weightLimit.setModifier(5, 30);
        weightLimit.setModifier(10, 60);
        weightLimit.setModifier(15, 120);
        weightLimit.setModifier(20, 200);
    }

    // Tests for getInstance()
    @Test
    void testGetInstanceNotNull() {
        assertNotNull(WeightLimit.getInstance(), "getInstance() should not return null");
    }

    @Test
    void testGetInstanceSingletonConsistency() {
        WeightLimit instance1 = WeightLimit.getInstance();
        WeightLimit instance2 = WeightLimit.getInstance();
        assertSame(instance1, instance2, "getInstance() should always return the same instance");
    }

    @Test
    void testGetInstancePersistsData() {
        WeightLimit instance = WeightLimit.getInstance();
        instance.setModifier(25, 300);
        assertEquals(300, instance.getModifier(25), "Values set in singleton instance should persist");
    }

    // Tests for setModifier()
    @Test
    void testSetModifierStoresNewValue() {
        weightLimit.setModifier(12, 100);
        assertEquals(100, weightLimit.getModifier(12), "setModifier() should store new strength-weight pair");
    }

    @Test
    void testSetModifierUpdatesExistingValue() {
        weightLimit.setModifier(10, 80);
        assertEquals(80, weightLimit.getModifier(10), "setModifier() should update an existing strength entry");
    }

    @Test
    void testSetModifierAddsUniqueEntry() {
        weightLimit.setModifier(25, 250);
        assertEquals(250, weightLimit.getModifier(25), "setModifier() should add a new key-value pair");
    }

    // Tests for getModifier()
    @Test
    void testGetModifierExactMatch() {
        assertEquals(120, weightLimit.getModifier(15), "getModifier() should return the correct value for exact key");
    }

    @Test
    void testGetModifierAboveMaxStrength() {
        assertEquals(933, weightLimit.getModifier(30), "getModifier() should return the max strength value for above range");
    }
}
