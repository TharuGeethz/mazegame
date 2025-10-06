package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.item.Weapon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DropItemCommandTest {

	@Test
	void whenNoArgument_shouldAskWhichItem() {
		Player p = new Player("Hero");
		DropItemCommand cmd = new DropItemCommand();

		ParsedInput input = new ParsedInput("drop", new ArrayList<>());

		CommandResponse resp = cmd.execute(input, p);

		assertTrue(resp.getMessage().startsWith("Which item do you want to drop?"),
				"Should prompt for an item name when none is provided.");
	}

	@Test
	void whenItemNotOwned_shouldInformUser() {
		Player p = new Player("Hero");
		DropItemCommand cmd = new DropItemCommand();

		ParsedInput input = new ParsedInput("drop", new ArrayList<>(List.of("longsword")));

		CommandResponse resp = cmd.execute(input, p);

		assertEquals("You don't have longsword to drop", resp.getMessage(),
				"Should clearly state the player doesn't have the item.");
	}

	@Test
	void whenDroppingEquippedItem_shouldUnequipAndMoveToLocation() {
		Location town = new Location("Town", "A small town.");
		Player p = new Player("Hero");
		p.setCurrentLocation(town);

		Weapon sword = new Weapon("longsword",  10, 4, "1d5");
		p.getInventory().addItem(sword);

		// Equip it first
		EquipItemCommand equip = new EquipItemCommand();
		equip.execute(new ParsedInput("equip", new ArrayList<>(List.of("longsword"))), p);
		assertTrue(p.isWearing("longsword"), "Sword should be equipped before dropping.");

		DropItemCommand drop = new DropItemCommand();
		ParsedInput input = new ParsedInput("drop", new ArrayList<>(List.of("longsword")));

		CommandResponse resp = drop.execute(input, p);


		assertFalse(p.isWearing("longsword"), "Dropping should unequip it.");
		assertFalse(p.getInventory().getItemList().containsKey("longsword"),
				"Dropped item should no longer be in player's inventory.");
		assertTrue(town.getInventory().getItemList().containsKey("longsword"),
				"Dropped item should now be in location inventory.");
		assertEquals("You dropped the longsword", resp.getMessage());
	}
}
