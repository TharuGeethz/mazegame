package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Blacksmith;
import mazegame.entity.Money;
import mazegame.entity.Player;
import mazegame.entity.item.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuyCommandTest {

    private BuyCommand command;
    private Player player;
    private Blacksmith shop;
    private Weapon sword;

    @BeforeEach
    void setUp() {
        command = new BuyCommand();
        player = new Player("Hero");
        shop = new Blacksmith("Forge of Helan", "A stronghold of fine weapons");

        sword = new Weapon("sword", 10, 5, "1d6");
        sword.setValue(10);

        shop.getInventory().addItem(sword);
        player.setCurrentLocation(shop);
        player.getInventory().getGold().add(20);
    }

    @Test
    void testSuccessfulPurchase() {
        ParsedInput input = new ParsedInput("buy", new ArrayList<>(List.of("sword")));
        CommandResponse response = command.execute(input, player);

        assertTrue(player.getInventory().hasItem("sword"));
        assertEquals(10, player.getInventory().getGold().getTotal());
        assertTrue(response.getMessage().contains("You bought sword"));
    }

    @Test
    void testBuyWithoutItemName() {
        ParsedInput input = new ParsedInput("buy", new ArrayList<>());
        CommandResponse response = command.execute(input, player);

        assertEquals("What would you like to buy? Use: buy <item_name>", response.getMessage());
    }

    @Test
    void testBuyItemWithoutEnoughGold() {
        player.getInventory().setGold(new Money(5));
        ParsedInput input = new ParsedInput("buy", new ArrayList<>(List.of("sword")));
        CommandResponse response = command.execute(input, player);

        assertTrue(response.getMessage().contains("You don't have enough gold"));
        assertFalse(player.getInventory().hasItem("sword"));
    }
}
