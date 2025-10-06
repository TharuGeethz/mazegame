package mazegame.entity.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArmorTest {

    // Tests for constructor
    @Test
    void testConstructorInitializesFieldsCorrectly() {
        Armor armor = new Armor("chainmail", 150, 40, 5);
        assertEquals("chainmail", armor.getLabel());
        assertEquals(150, armor.getValue());
        assertEquals(40, armor.getWeight());
        assertEquals(5, armor.getBonus());
    }

    @Test
    void testConstructorHandlesZeroValues() {
        Armor armor = new Armor("cloth", 0, 0, 0);
        assertEquals("cloth", armor.getLabel());
        assertEquals(0, armor.getValue());
        assertEquals(0, armor.getWeight());
        assertEquals(0, armor.getBonus());
    }

    @Test
    void testConstructorHandlesDifferentInputs() {
        Armor armor = new Armor("dragon scale", 1200, 55, 10);
        assertEquals("dragon scale", armor.getLabel());
        assertEquals(1200, armor.getValue());
        assertEquals(55, armor.getWeight());
        assertEquals(10, armor.getBonus());
    }

    // Tests for getBonus()
    @Test
    void testGetBonusReturnsCorrectValue() {
        Armor armor = new Armor("leather", 25, 15, 3);
        assertEquals(3, armor.getBonus());
    }

    @Test
    void testGetBonusWithHighValue() {
        Armor armor = new Armor("titan plate", 2000, 80, 15);
        assertEquals(15, armor.getBonus());
    }

    @Test
    void testGetBonusWithZeroBonus() {
        Armor armor = new Armor("cloth robe", 5, 5, 0);
        assertEquals(0, armor.getBonus());
    }
}
