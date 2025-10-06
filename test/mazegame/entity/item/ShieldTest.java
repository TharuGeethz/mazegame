package mazegame.entity.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShieldTest {

    // Constructor tests (3)

    @Test
    void constructor_initializesAllFields() {
        Shield s = new Shield("Steel Shield", 20, 15, 2);
        assertEquals("Steel Shield", s.getLabel());
        assertEquals(20, s.getValue());
        assertEquals(15, s.getWeight());
        assertEquals(2, s.getBonus());
    }

    @Test
    void constructor_allowsZeroValues() {
        Shield s = new Shield("Practice Shield", 0, 0, 0);
        assertEquals("Practice Shield", s.getLabel());
        assertEquals(0, s.getValue());
        assertEquals(0, s.getWeight());
        assertEquals(0, s.getBonus());
    }

    @Test
    void constructor_handlesLargeValues() {
        Shield s = new Shield("Tower Shield", 50_000, 2_000, 99);
        assertEquals("Tower Shield", s.getLabel());
        assertEquals(50_000, s.getValue());
        assertEquals(2_000, s.getWeight());
        assertEquals(99, s.getBonus());
    }

    // getBonus() tests (3)

    @Test
    void getBonus_returnsCorrectPositiveBonus() {
        Shield s = new Shield("Wooden Shield", 10, 8, 1);
        assertEquals(1, s.getBonus());
    }

    @Test
    void getBonus_returnsZeroWhenConstructedWithZero() {
        Shield s = new Shield("Blank Shield", 5, 5, 0);
        assertEquals(0, s.getBonus());
    }

    @Test
    void getBonus_canReturnNegativeIfProvided() {
        // No validation in class, so negative bonus should be stored as-is
        Shield s = new Shield("Cursed Shield", 1, 1, -2);
        assertEquals(-2, s.getBonus());
    }
}
