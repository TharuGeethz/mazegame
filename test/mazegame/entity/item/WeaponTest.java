package mazegame.entity.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    @Test
    void getDamage_returnsCorrectValue() {
        Weapon weapon = new Weapon("Longsword", 15, 10, "1d8");
        assertEquals("1d8", weapon.getDamage(), "Damage should match the initialized value");
    }

    @Test
    void getDamage_handlesDifferentDamageDice() {
        Weapon weapon = new Weapon("Greatsword", 50, 15, "2d6");
        assertEquals("2d6", weapon.getDamage(), "Damage format should reflect 2d6 correctly");
    }

    @Test
    void getDamage_allowsEmptyDamageString() {
        Weapon weapon = new Weapon("Training Sword", 5, 5, "");
        assertEquals("", weapon.getDamage(), "Damage string can be empty for special or training weapons");
    }
}
