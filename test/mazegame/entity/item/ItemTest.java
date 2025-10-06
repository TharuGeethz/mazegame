package mazegame.entity.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemSetValueTest {

    @Test
    void setValue_updatesToPositive() {
        Weapon weapon = new Weapon("test blade", 10, 3, "1d6");
        weapon.setValue(25);
        assertEquals(25, weapon.getValue());
    }

    @Test
    void setValue_allowsZero() {
        Weapon weapon = new Weapon("test blade", 10, 3, "1d6");
        weapon.setValue(0);
        assertEquals(0, weapon.getValue());
    }

    @Test
    void setValue_allowsNegativeAndOverwritesPrevious() {
        Weapon weapon = new Weapon("test blade", 10, 3, "1d6");
        weapon.setValue(-5);           // no validation in setter, so store as-is
        assertEquals(-5, weapon.getValue());

        weapon.setValue(7);            // overwrite again
        assertEquals(7, weapon.getValue());
    }
}
