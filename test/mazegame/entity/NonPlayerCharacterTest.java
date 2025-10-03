package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class NonPlayerCharacterTest {
    
    private NonPlayerCharacter friendly;
    private NonPlayerCharacter hostile;
    
    @BeforeEach
    public void setUp() {
        friendly = new NonPlayerCharacter("Shopkeeper", false);
        hostile = new NonPlayerCharacter("Goblin", 12, 14, 25, "Grrr! You shall not pass!", true);
    }
    
    @Test
    public void testBasicConstructor() {
        assertEquals("Shopkeeper", friendly.getName(), "Name should be set correctly");
        assertFalse(friendly.isHostile(), "Should not be hostile");
        assertNull(friendly.getConversation(), "Conversation should be null with basic constructor");
    }
    
    @Test
    public void testFullConstructor() {
        assertEquals("Goblin", hostile.getName(), "Name should be set correctly");
        assertEquals(12, hostile.getStrength(), "Strength should be set correctly");
        assertEquals(14, hostile.getAgility(), "Agility should be set correctly");
        assertEquals(25, hostile.getLifePoints(), "Life points should be set correctly");
        assertEquals("Grrr! You shall not pass!", hostile.getConversation(), "Conversation should be set");
        assertTrue(hostile.isHostile(), "Should be hostile");
    }
    
    @Test
    public void testHostilityAndConversationModification() {
        friendly.setIsHostile(true);
        assertTrue(friendly.isHostile(), "Hostility should be changed to true");
        
        friendly.setConversation("Hello, traveler!");
        assertEquals("Hello, traveler!", friendly.getConversation(), "Conversation should be set");
        
        hostile.setIsHostile(false);
        assertFalse(hostile.isHostile(), "Hostility should be changed to false");
    }
}