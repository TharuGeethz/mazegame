package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class BlacksmithTest {
    
    private Blacksmith blacksmith;
    
    @BeforeEach
    public void setUp() {
        blacksmith = new Blacksmith("A busy blacksmith's workshop with anvils and forges", "Smithy");
    }
    
    @Test
    public void testBlacksmithCreation() {
        assertEquals("A busy blacksmith's workshop with anvils and forges", blacksmith.getDescription(), "Description should be set correctly");
        assertEquals("Smithy", blacksmith.getLabel(), "Label should be set correctly");
        assertNotNull(blacksmith.getInventory(), "Blacksmith should have inventory");
        assertNotNull(blacksmith.getExitCollection(), "Blacksmith should have exits");
    }
    
    @Test
    public void testInheritanceFromLocation() {
        // Test that blacksmith inherits Location functionality
        blacksmith.setDescription("Updated workshop description");
        assertEquals("Updated workshop description", blacksmith.getDescription(), "Should inherit setDescription");
        
        blacksmith.setLabel("NewSmithy");
        assertEquals("NewSmithy", blacksmith.getLabel(), "Should inherit setLabel");
    }
    
    @Test
    public void testToStringOverride() {
        String result = blacksmith.toString();
        assertTrue(result.contains("This is a shop!"), "toString should include shop indicator");
        assertTrue(result.contains("Smithy"), "toString should include location info from parent");
        assertTrue(result.contains("workshop"), "toString should include description from parent");
    }
}