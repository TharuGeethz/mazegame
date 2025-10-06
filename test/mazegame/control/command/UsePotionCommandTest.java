package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;
import mazegame.entity.item.HealingPotion;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsePotionCommandTest {

    private UsePotionCommand command;
    private Player player;
    private HealingPotion potion;

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);
        command = new UsePotionCommand();
        player = new Player("Hero");
        potion = new HealingPotion("healing potion", "Restores health");
    }

    @Test
    void testNoPotionNameGiven() {
        ParsedInput input = new ParsedInput("use", new ArrayList<>());
        CommandResponse resp = command.execute(input, player);

        assertEquals("Which potion do you want to use? ", resp.getMessage());
    }

    @Test
    void testUsePotionSuccessfully() {
        player.setLifePoints(10);
        player.getInventory().addPotion(potion);

        ParsedInput input = new ParsedInput("use", new ArrayList<>(List.of("healing potion")));
        CommandResponse resp = command.execute(input, player);

        assertTrue(resp.getMessage().contains("Life points restored from potion"));
        assertTrue(player.getLifePoints() > 10);
    }

    @Test
    void testPotionNotFound() {
        player.setLifePoints(10);
        ParsedInput input = new ParsedInput("use", new ArrayList<>(List.of("unknown potion")));
        CommandResponse resp = command.execute(input, player);

        assertEquals("You dont have the potion unknown potion", resp.getMessage());
    }

    @Test
    void testFullHealthPreventsUsage() {
        player.setLifePoints(20);
        ParsedInput input = new ParsedInput("use", new ArrayList<>(List.of("healing potion")));
        CommandResponse resp = command.execute(input, player);

        assertTrue(resp.getMessage().contains("You already have full"));
        assertTrue(resp.getMessage().contains("Life points"));
    }
}
