package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

public class DiceTest {
    
    private Dice standardDice;
    
    @BeforeEach
    public void setUp() {
        standardDice = new Dice(6);
    }
    
    @RepeatedTest(100)
    public void testInstanceRollWithinRange() {
        int result = standardDice.roll();
        assertTrue(result >= 1 && result <= 6, 
                   "Dice roll should be between 1 and 6, but was: " + result);
    }
    
    @Test
    public void testDamageNotationMultipleDice() {
        int result = Dice.roll("3d6");
        assertTrue(result >= 3 && result <= 18, 
                   "3d6 should roll between 3 and 18, but was: " + result);
    }
    
    @Test
    public void testDamageNotationInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            Dice.roll("invalid");
        }, "Invalid format should throw IllegalArgumentException");
        
        assertThrows(IllegalArgumentException.class, () -> {
            Dice.roll("d6");
        }, "Missing dice count should throw IllegalArgumentException");
        
        assertThrows(IllegalArgumentException.class, () -> {
            Dice.roll("1d");
        }, "Missing dice sides should throw IllegalArgumentException");
        
        assertThrows(IllegalArgumentException.class, () -> {
            Dice.roll("0d6");
        }, "Zero dice count should throw IllegalArgumentException");
        
        assertThrows(IllegalArgumentException.class, () -> {
            Dice.roll("1d0");
        }, "Zero dice sides should throw IllegalArgumentException");
    }
}