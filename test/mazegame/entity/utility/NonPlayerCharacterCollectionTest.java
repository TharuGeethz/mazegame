package mazegame.entity.utility;

import mazegame.entity.NonPlayerCharacter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NonPlayerCharacterCollectionTest {

    private NonPlayerCharacterCollection npcCollection;

    @BeforeEach
    void setUp() {
        npcCollection = new NonPlayerCharacterCollection();
        npcCollection.put("grog", new NonPlayerCharacter("Grog", 10, 8, 15, "Grr!", true));
        npcCollection.put("lyra", new NonPlayerCharacter("Lyra", 9, 10, 14, "Peace be with you", false));
    }

    // Tests for hasNPC(String name)

    @Test
    void testHasNPCReturnsTrueForExistingNPC() {
        assertTrue(npcCollection.hasNPC("grog"), "Expected hasNPC to return true for an existing NPC name");
    }

    @Test
    void testHasNPCIsCaseInsensitive() {
        assertTrue(npcCollection.hasNPC("LyRa"), "Expected hasNPC to work case-insensitively");
    }

    @Test
    void testHasNPCReturnsFalseForNullOrMissingName() {
        assertFalse(npcCollection.hasNPC(null), "Expected hasNPC to return false for null");
        assertFalse(npcCollection.hasNPC("thor"), "Expected hasNPC to return false for non-existing name");
    }

    //Tests for hasHostileNPCs()

    @Test
    void testHasHostileNPCsReturnsTrueWhenHostileExists() {
        assertTrue(npcCollection.hasHostileNPCs(), "Should return true because 'Grog' is hostile");
    }

    @Test
    void testHasHostileNPCsReturnsFalseWhenAllFriendly() {
        NonPlayerCharacterCollection allFriendly = new NonPlayerCharacterCollection();
        allFriendly.put("lyra", new NonPlayerCharacter("Lyra", 8, 10, 12, "Hello!", false));
        assertFalse(allFriendly.hasHostileNPCs(), "Should return false when no hostile NPCs are present");
    }

    @Test
    void testHasHostileNPCsReturnsFalseWhenEmpty() {
        NonPlayerCharacterCollection empty = new NonPlayerCharacterCollection();
        assertFalse(empty.hasHostileNPCs(), "Should return false when collection is empty");
    }

    // Tests for getNPCsByHostility(boolean hostile)

    @Test
    void testGetNPCsByHostilityReturnsHostileNPCs() {
        List<NonPlayerCharacter> hostiles = npcCollection.getNPCsByHostility(true);
        assertEquals(1, hostiles.size(), "Should return only one hostile NPC");
        assertEquals("Grog", hostiles.get(0).getName());
    }

    @Test
    void testGetNPCsByHostilityReturnsFriendlyNPCs() {
        List<NonPlayerCharacter> friendlies = npcCollection.getNPCsByHostility(false);
        assertEquals(1, friendlies.size(), "Should return only one friendly NPC");
        assertEquals("Lyra", friendlies.get(0).getName());
    }

    @Test
    void testGetNPCsByHostilityReturnsEmptyWhenNoneMatch() {
        NonPlayerCharacterCollection allHostile = new NonPlayerCharacterCollection();
        allHostile.put("orc", new NonPlayerCharacter("Orc", 10, 9, 14, "Raaah!", true));
        assertTrue(allHostile.getNPCsByHostility(false).isEmpty(), "Should return empty list if no friendly NPCs exist");
    }

    // Tests for toString()

    @Test
    void testToStringIncludesAllNPCNames() {
        String output = npcCollection.toString();
        assertTrue(output.contains("grog") && output.contains("lyra"),
                "toString should include all NPC names in the collection");
    }

    @Test
    void testToStringWithEmptyCollection() {
        NonPlayerCharacterCollection empty = new NonPlayerCharacterCollection();
        String output = empty.toString();
        assertTrue(output.contains("NPC Characters: None present"), "Empty collection should show 'No characters'");
    }

    @Test
    void testToStringFormat() {
        String output = npcCollection.toString();
        assertTrue(output.startsWith("NPC Characters:"), "Output should start with 'NPC Characters ::'");
        assertTrue(output.contains("<<"), "Should contain '<<' delimiters for NPC names");
    }
}
