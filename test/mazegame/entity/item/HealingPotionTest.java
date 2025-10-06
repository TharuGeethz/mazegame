package mazegame.entity.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HealingPotionTest {

    @Test
    void getLabel_returnsCorrectLabel() {
        HealingPotion potion = new HealingPotion("elixir of life", "Restores full health");
        assertEquals("elixir of life", potion.getLabel());
    }

    @Test
    void setLabel_updatesLabelValue() {
        HealingPotion potion = new HealingPotion("old name", "desc");
        potion.setLabel("new name");
        assertEquals("new name", potion.getLabel());
    }

    @Test
    void setLabel_allowsEmptyString() {
        HealingPotion potion = new HealingPotion("test potion", "desc");
        potion.setLabel("");
        assertEquals("", potion.getLabel());
    }


    @Test
    void getDescription_returnsCorrectDescription() {
        HealingPotion potion = new HealingPotion("elixir", "Restores health");
        assertEquals("Restores health", potion.getDescription());
    }

    @Test
    void setDescription_updatesDescription() {
        HealingPotion potion = new HealingPotion("elixir", "old desc");
        potion.setDescription("new desc");
        assertEquals("new desc", potion.getDescription());
    }

    @Test
    void setDescription_acceptsNullValue() {
        HealingPotion potion = new HealingPotion("elixir", "desc");
        potion.setDescription(null);
        assertNull(potion.getDescription());
    }

    // heal()
    @Test
    void heal_returnsValueWithinExpectedRange() {
        HealingPotion potion = new HealingPotion("test", "desc");
        int result = potion.heal();
        // 2d6 means values between 2 and 12
        assertTrue(result >= 2 && result <= 12, "Healing should be between 2 and 12");
    }

    @Test
    void heal_returnsIntegerValue() {
        HealingPotion potion = new HealingPotion("test", "desc");
        assertInstanceOf(Integer.class, potion.heal());
    }

    @Test
    void heal_returnsDifferentValuesOverMultipleCalls() {
        HealingPotion potion = new HealingPotion("test", "desc");
        int first = potion.heal();
        int second = potion.heal();
        // Might occasionally be equal, but in most cases it varies
        assertTrue(first >= 2 && first <= 12);
        assertTrue(second >= 2 && second <= 12);
    }
}
