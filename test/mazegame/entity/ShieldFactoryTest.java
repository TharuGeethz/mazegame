package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import mazegame.entity.item.Shield;

public class ShieldFactoryTest {
    
    private ShieldFactory factory;
    private Shield woodenShield;
    private Shield steelShield;
    
    @BeforeEach
    public void setUp() {
        factory = ShieldFactory.getInstance();
        woodenShield = new Shield("Wooden Shield", 50, 3, 1);
        steelShield = new Shield("Steel Shield", 200, 8, 3);
        
        // Clear any existing data (singleton)
        factory.getAllShields().clear();
    }
    
    @Test
    public void testSingletonPattern() {
        ShieldFactory factory1 = ShieldFactory.getInstance();
        ShieldFactory factory2 = ShieldFactory.getInstance();
        assertSame(factory1, factory2, "getInstance should return the same instance");
    }
    
    @Test
    public void testAddAndRetrieveShield() {
        assertTrue(factory.addShield(woodenShield), "Should successfully add shield");
        assertEquals(woodenShield, factory.getShield("Wooden Shield"), "Should retrieve added shield");
        
        factory.addShield(steelShield);
        assertEquals(2, factory.getAllShields().size(), "Should contain both shields");
        assertEquals(steelShield, factory.getShield("Steel Shield"), "Should retrieve second shield");
    }
    
    @Test
    public void testRetrieveNonExistentShield() {
        assertNull(factory.getShield("Non-existent"), "Should return null for non-existent shield");
        
        factory.addShield(woodenShield);
        assertNull(factory.getShield("wooden shield"), "Should be case sensitive");
        assertEquals(woodenShield, factory.getShield("Wooden Shield"), "Should return correct shield with exact match");
    }
}