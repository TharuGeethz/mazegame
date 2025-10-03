package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import mazegame.entity.item.Armor;

public class ArmorFactoryTest {
    
    private ArmorFactory factory;
    private Armor chainmail;
    private Armor plateArmor;
    
    @BeforeEach
    public void setUp() {
        factory = ArmorFactory.getInstance();
        chainmail = new Armor("Chainmail", 200, 10, 3);
        plateArmor = new Armor("Plate Armor", 500, 20, 5);
        
        // Clear any existing data  (singleton)
        factory.getAllArmors().clear();
    }
    
    @Test
    public void testSingletonPattern() {
        ArmorFactory factory1 = ArmorFactory.getInstance();
        ArmorFactory factory2 = ArmorFactory.getInstance();
        assertSame(factory1, factory2, "getInstance should return the same instance");
    }
    
    @Test
    public void testAddAndRetrieveArmor() {
        assertTrue(factory.addArmor(chainmail), "Should successfully add armor");
        assertEquals(chainmail, factory.getArmor("Chainmail"), "Should retrieve added armor");
        
        factory.addArmor(plateArmor);
        assertEquals(2, factory.getAllArmors().size(), "Should contain both armors");
        assertEquals(plateArmor, factory.getArmor("Plate Armor"), "Should retrieve second armor");
    }
    
    @Test
    public void testRetrieveNonExistentArmor() {
        assertNull(factory.getArmor("Non-existent"), "Should return null for non-existent armor");
        
        factory.addArmor(chainmail);
        assertNull(factory.getArmor("chainmail"), "Should be case sensitive");
        assertEquals(chainmail, factory.getArmor("Chainmail"), "Should return correct armor with exact match");
    }
}