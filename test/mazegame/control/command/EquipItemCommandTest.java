package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Player;
import mazegame.entity.item.Item;
import mazegame.entity.item.Weapon;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquipItemCommandTest {
	@BeforeEach
	void setUp() {
		WeightLimit weightLimitTable = WeightLimit.getInstance();
		weightLimitTable.setModifier(10, 66);
		weightLimitTable.setModifier(20, 266);
	}

	@Test
	void whenNoArgument_shouldPromptForItemName() {
		Player p = new Player("Hero");
		EquipItemCommand cmd = new EquipItemCommand();

		ParsedInput input = new ParsedInput("equip", new ArrayList<>());
		CommandResponse resp = cmd.execute(input, p);

		assertEquals("If you need to wear an item you need to tell me what.", resp.getMessage());
	}

	@Test
	void whenItemNotOwned_shouldInformUser() {
		Player p = new Player("Hero");
		EquipItemCommand cmd = new EquipItemCommand();

		ParsedInput input = new ParsedInput("equip", new ArrayList<>(List.of("longsword")));
		CommandResponse resp = cmd.execute(input, p);

		assertTrue(resp.getMessage().contains("don’t have the longsword"),
				"Should tell the player they don't have the item.");
	}

	@Test
	void whenEquippingSameType_shouldUnequipExistingAndEquipNewOne() {
		Player p = new Player("Hero");
		EquipItemCommand cmd = new EquipItemCommand();

		Weapon dagger = new Weapon("dagger", 1, 2, "1d4");
		Weapon longsword = new Weapon("longsword", 15, 4, "1d8");

		// Both items in inventory
		p.getInventory().addItem(dagger);
		p.getInventory().addItem(longsword);

		// First equip dagger
		CommandResponse first = cmd.execute(new ParsedInput("equip", new ArrayList<>(List.of("dagger"))), p);
		assertTrue(first.getMessage().toLowerCase().contains("equipped"),
				"First equip should succeed.");
		// dagger is equipped
		Item equipped1 = p.getWearingItems().values().stream().findFirst().orElse(null);
		assertNotNull(equipped1);
		assertEquals("dagger", equipped1.getLabel());

		// Second equip longsword (should unequip dagger and equip longsword)
		CommandResponse second = cmd.execute(new ParsedInput("equip", new ArrayList<>(List.of("longsword"))), p);

		// Message should mention prior unequip and new equip
		assertTrue(second.getMessage().contains("dagger unequipped"),
				"Should report the old item was unequipped.");
		assertTrue(second.getMessage().contains("longsword equipped"),
				"Should report the new item was equipped.");

		// Equipped should be longsword
		Item equipped2 = p.getWearingItems().values().stream().findFirst().orElse(null);
		assertNotNull(equipped2);
		assertEquals("longsword", equipped2.getLabel());

		// Inventory should contain dagger (returned) and not contain longsword (now worn)
		assertTrue(p.getInventory().getItemList().containsKey("dagger"),
				"Old item should be returned to inventory after swap.");
		assertFalse(p.getInventory().getItemList().containsKey("longsword"),
				"Newly equipped item should be removed from inventory.");
	}
}
