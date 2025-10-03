package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import mazegame.entity.item.Weapon;
import mazegame.entity.item.HealingPotion;

public class InventoryTest {
    
    private Inventory inventory;
    private Weapon sword;
    private HealingPotion potion;
    
    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
        sword = new Weapon("Iron Sword", 100, 5, "1d8");
        potion = new HealingPotion("Health Potion", "Restores health");
    }
    
    @Test
    public void testMoneyOperations() {
        assertEquals(0, inventory.getGold().getTotal(), "Initial gold should be 0");
        
        inventory.addMoney(100);
        assertEquals(100, inventory.getGold().getTotal(), "Gold should be added correctly");
        
        assertTrue(inventory.removeMoney(50), "Should be able to remove valid amount");
        assertEquals(50, inventory.getGold().getTotal(), "Remaining gold should be correct");
        
        assertFalse(inventory.removeMoney(100), "Should not be able to remove more than available");
    }
    
    @Test
    public void testItemManagement() {
        inventory.addItem(sword);
        assertEquals(sword, inventory.findItem("Iron Sword"), "Should be able to find added item");
        assertTrue(inventory.hasItem("Iron Sword"), "Should confirm item exists");
        
        assertEquals(sword, inventory.removeItem("Iron Sword"), "Should be able to remove item");
        assertNull(inventory.findItem("Iron Sword"), "Item should no longer exist after removal");
        assertFalse(inventory.hasItem("Iron Sword"), "Should confirm item no longer exists");
    }
    
    @Test
    public void testPotionManagement() {
        inventory.addPotion(potion);
        assertTrue(inventory.hasItem("Health Potion"), "Should confirm potion exists");
        assertEquals(1, inventory.getPotionList().size(), "Potion list should contain one item");
        
        inventory.removePotion("Health Potion");
        assertFalse(inventory.hasItem("Health Potion"), "Potion should no longer exist after removal");
        assertEquals(0, inventory.getPotionList().size(), "Potion list should be empty");
    }
}