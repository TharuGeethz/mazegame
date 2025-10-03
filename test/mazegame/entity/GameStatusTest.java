package mazegame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class GameStatusTest {
    
    private GameStatus gameStatus;
    private Location forest;
    private Blacksmith shop;
    
    @BeforeEach
    public void setUp() {
        gameStatus = GameStatus.getInstance();
        forest = new Location("A dense forest", "Forest");
        shop = new Blacksmith("A blacksmith's workshop", "Smithy");
        
        // Clear any existing data (singleton)
        gameStatus.getLocations().clear();
        gameStatus.getShops().clear();
    }
    
    @Test
    public void testSingletonPattern() {
        GameStatus status1 = GameStatus.getInstance();
        GameStatus status2 = GameStatus.getInstance();
        assertSame(status1, status2, "getInstance should return the same instance");
    }
    
    @Test
    public void testLocationManagement() {
        assertEquals(0, gameStatus.getLocations().size(), "Should start with no locations");
        
        GameStatus result = gameStatus.addLocation(forest);
        assertSame(gameStatus, result, "addLocation should return same instance for chaining");
        assertEquals(1, gameStatus.getLocations().size(), "Should contain one location");
        assertEquals(forest, gameStatus.getLocations().get("Forest"), "Should retrieve added location");
    }
    
    @Test
    public void testShopManagement() {
        assertEquals(0, gameStatus.getShops().size(), "Should start with no shops");
        
        GameStatus result = gameStatus.addShop(shop);
        assertSame(gameStatus, result, "addShop should return same instance for chaining");
        assertEquals(1, gameStatus.getShops().size(), "Should contain one shop");
        assertEquals(shop, gameStatus.getShops().get("Smithy"), "Should retrieve added shop");
    }
}