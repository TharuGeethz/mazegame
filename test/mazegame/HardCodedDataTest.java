package mazegame;

import mazegame.entity.*;
import mazegame.entity.item.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HardCodedDataTest {

    private HardCodedData data;
    private GameStatus gameStatus;

    @BeforeEach
    void setUp() {
        data = new HardCodedData();
        gameStatus = GameStatus.getInstance();
    }

    // ===== Test 1: Locations contain items (indirect test for addItemsToLocation) =====
    @Test
    void testLocationsHaveExpectedItems() {
        Location forest = gameStatus.getLocations().get("Forest Clearing");
        assertNotNull(forest, "Forest Clearing should exist");

        assertTrue(forest.getInventory().hasItem("nunchaku"), "Forest should have 'nunchaku'");
        assertTrue(forest.getInventory().hasItem("leather"), "Forest should have 'leather' armor");
    }

    // ===== Test 2: NPCs have expected items (indirect test for addItemsToNPC) =====
    @Test
    void testNPCsHaveExpectedWeaponsAndArmor() {
        Location inn = gameStatus.getLocations().get("Inn of the Boar");
        assertNotNull(inn, "Inn of the Boar should exist");
        NonPlayerCharacter philip = inn.getNpcCollection().get("philip");
        assertNotNull(philip, "Philip NPC should exist");

        assertTrue(philip.getInventory().hasItem("greatsword"));
        assertTrue(philip.getInventory().hasItem("full plate"));
        assertTrue(philip.getInventory().hasItem("shield, large, steel"));
    }

    @Test
    void testFactoriesReturnExpectedItems() {
        WeaponFactory wf = WeaponFactory.getInstance();
        ArmorFactory af = ArmorFactory.getInstance();
        ShieldFactory sf = ShieldFactory.getInstance();

        Weapon sword = wf.getWeapon("longsword");
        Armor chain = af.getArmor("chainmail");
        Shield shield = sf.getShield("shield, large, steel");

        assertNotNull(sword, "WeaponFactory should return longsword");
        assertNotNull(chain, "ArmorFactory should return chainmail");
        assertNotNull(shield, "ShieldFactory should return steel shield");
        assertEquals("longsword", sword.getLabel());
        assertEquals("chainmail", chain.getLabel());
        assertEquals("shield, large, steel", shield.getLabel());
    }

    @Test
    void testExitsBetweenLocations() {
        Location forest = gameStatus.getLocations().get("Forest Clearing");
        Location marsh = gameStatus.getLocations().get("Whispering Marsh");

        assertTrue(forest.getExitCollection().containsExit("south"), "Forest should have south exit to Marsh");
        Exit southExit = forest.getExitCollection().getExit("south");
        assertEquals(marsh, southExit.getDestination());
    }

    @Test
    void testCastleExitsAreInitiallyLocked() {
        Location townSquare = gameStatus.getLocations().get("Town Square");
        Location cave = gameStatus.getLocations().get("Crystal Cave");

        Exit toCastleFromTown = townSquare.getExitCollection().getExit("southwest");
        Exit toCastleFromCave = cave.getExitCollection().getExit("west");

        assertNotNull(toCastleFromTown);
        assertNotNull(toCastleFromCave);
        assertTrue(toCastleFromTown.isLocked(), "Town Square to Castle should start locked");
        assertTrue(toCastleFromCave.isLocked(), "Crystal Cave to Castle should start locked");
    }

    @Test
    void testHealingPotionsExist() {
        Location inn = gameStatus.getLocations().get("Inn of the Boar");
        Location town = gameStatus.getLocations().get("Town Square");

        assertTrue(town.getInventory().hasItem("sacred flask"));
        assertTrue(inn.getInventory().hasItem("dwarven tonic"));
    }
}
