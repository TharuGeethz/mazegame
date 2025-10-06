package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Blacksmith;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.item.Weapon;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SellCommandTest {

    private SellCommand command;
    private Player player;
    private Blacksmith shop;
    private Weapon sword;

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);
        command = new SellCommand();
        player = new Player("Hero");
        shop = new Blacksmith("Forge of Helan", "A shop for weapons");
        player.setCurrentLocation(shop);

        sword = new Weapon("sword", 10, 5,  "1d6");
        sword.setValue(10);
        player.getInventory().addItem(sword);
    }

    @Test
    void testSuccessfulSale() {
        player.getInventory().getGold().add(5);
        ParsedInput input = new ParsedInput("sell", new ArrayList<>(List.of("sword")));

        CommandResponse resp = command.execute(input, player);

        assertFalse(player.getInventory().hasItem("sword"));
        assertTrue(shop.getInventory().hasItem("sword"));
        assertTrue(resp.getMessage().contains("You sold sword for 8 gold pieces"));
        assertEquals(13, player.getInventory().getGold().getTotal()); // 5 + 8 = 13
    }

    @Test
    void testSellItemNotOwned() {
        ParsedInput input = new ParsedInput("sell", new ArrayList<>(List.of("shield")));
        CommandResponse resp = command.execute(input, player);

        assertEquals("You don't have 'shield' to sell.", resp.getMessage());
    }

    @Test
    void testSellOutsideShop() {
        player.setCurrentLocation(new Location("Forest", "Dark path"));
        ParsedInput input = new ParsedInput("sell", new ArrayList<>(List.of("sword")));

        CommandResponse resp = command.execute(input, player);

        assertEquals("You can only sell items in a shop.", resp.getMessage());
    }
}
