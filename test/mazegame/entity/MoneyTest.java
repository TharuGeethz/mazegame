package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {
    
    private Money money;
    
    @BeforeEach
    public void setUp() {
        money = new Money(100);
    }
    
    @Test
    public void testConstructorAndGetTotal() {
        Money defaultMoney = new Money();
        assertEquals(0, defaultMoney.getTotal(), "Default constructor should initialize with 0 gold");
        
        Money paramMoney = new Money(50);
        assertEquals(50, paramMoney.getTotal(), "Constructor should set the correct initial amount");
    }
    
    @Test
    public void testSubtractValidAndInvalidAmounts() {
        boolean result = money.Subtract(40);
        assertTrue(result, "Subtracting valid amount should return true");
        assertEquals(60, money.getTotal(), "Total should be reduced by subtracted amount");
        
        boolean invalidResult = money.Subtract(150);
        assertFalse(invalidResult, "Subtracting more than available should return false");
        assertEquals(60, money.getTotal(), "Total should remain unchanged when subtraction fails");
    }
    
    @Test
    public void testToStringVariations() {
        Money noMoney = new Money(0);
        assertEquals("No gold pieces", noMoney.toString(), "Zero gold should display 'No gold pieces'");
        
        Money oneMoney = new Money(1);
        assertEquals("One gold piece", oneMoney.toString(), "One gold should display 'One gold piece'");
        assertEquals("There are 100 gold pieces", money.toString(), "Multiple gold should display count");
    }
}