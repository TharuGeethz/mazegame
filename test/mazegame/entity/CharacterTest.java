package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import mazegame.entity.item.Weapon;
import mazegame.entity.item.Armor;
import mazegame.entity.utility.AgilityTable;

public class CharacterTest {
    
    private Character character;
    private Weapon sword;
    private Armor chainmail;
    
    @BeforeEach
    public void setUp() {
        character = new Character("TestHero", 15, 12, 50);
        sword = new Weapon("Iron Sword", 100, 5, "1d8");
        chainmail = new Armor("Chainmail", 200, 10, 3);
        
        // Initialize AgilityTable for testing
        AgilityTable agilityTable = AgilityTable.getInstance();
        agilityTable.setModifier(12, 1); // Set agility 12 to have +1 modifier
    }
    
    @Test
    public void testCharacterCreationAndStats() {
        assertEquals("TestHero", character.getName(), "Name should be set correctly");
        assertEquals(15, character.getStrength(), "Strength should be set correctly");
        assertEquals(12, character.getAgility(), "Agility should be set correctly");
        assertEquals(50, character.getLifePoints(), "Life points should be set correctly");
        assertNotNull(character.getInventory(), "Inventory should be initialized");
    }
    
    @Test
    public void testEquipAndUnequipItems() {
        character.getInventory().addItem(sword);
        character.equipItem("Iron Sword");
        
        assertEquals(sword, character.getEquippedWeapon(), "Weapon should be equipped");
        assertFalse(character.getInventory().hasItem("Iron Sword"), "Item should be removed from inventory");
        
        character.unequipItem("Iron Sword");
        assertNull(character.getEquippedWeapon(), "No weapon should be equipped");
        assertTrue(character.getInventory().hasItem("Iron Sword"), "Item should be back in inventory");
    }
    
    @Test
    public void testArmorClassCalculation() {
        int baseAC = character.getArmorClass(); // 10 + agility modifier
        assertEquals(11, baseAC, "Base AC should be 10 + agility modifier (1)");
        
        character.getInventory().addItem(chainmail);
        character.equipItem("Chainmail");
        
        int newAC = character.getArmorClass();
        assertEquals(14, newAC, "AC should be 10 + agility(1) + armor(3) = 14");
        assertEquals(chainmail, character.getEquippedArmor(), "Armor should be equipped");
    }
}