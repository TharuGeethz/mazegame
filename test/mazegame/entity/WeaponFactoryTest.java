package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import mazegame.entity.item.Weapon;

public class WeaponFactoryTest {
    
    private WeaponFactory factory;
    private Weapon sword;
    private Weapon axe;
    
    @BeforeEach
    public void setUp() {
        factory = WeaponFactory.getInstance();
        sword = new Weapon("Iron Sword", 100, 5, "1d8");
        axe = new Weapon("Battle Axe", 150, 8, "1d10");
        
        // Clear any existing data (singleton)
        factory.getAllWeapons().clear();
    }
    
    @Test
    public void testSingletonPattern() {
        WeaponFactory factory1 = WeaponFactory.getInstance();
        WeaponFactory factory2 = WeaponFactory.getInstance();
        assertSame(factory1, factory2, "getInstance should return the same instance");
    }
    
    @Test
    public void testAddAndRetrieveWeapon() {
        assertTrue(factory.addWeapon(sword), "Should successfully add weapon");
        assertEquals(sword, factory.getWeapon("Iron Sword"), "Should retrieve added weapon");
        
        factory.addWeapon(axe);
        assertEquals(2, factory.getAllWeapons().size(), "Should contain both weapons");
        assertEquals(axe, factory.getWeapon("Battle Axe"), "Should retrieve second weapon");
    }
    
    @Test
    public void testRetrieveNonExistentWeapon() {
        assertNull(factory.getWeapon("Non-existent"), "Should return null for non-existent weapon");
        
        factory.addWeapon(sword);
        assertNull(factory.getWeapon("iron sword"), "Should be case sensitive");
        assertEquals(sword, factory.getWeapon("Iron Sword"), "Should return correct weapon with exact match");
    }
}