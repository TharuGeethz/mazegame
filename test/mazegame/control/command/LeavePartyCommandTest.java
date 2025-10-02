package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Location;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;
import mazegame.entity.utility.NonPlayerCharacterCollection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LeavePartyCommandTest {

	@Test
	void whenPlayerHasNoParty_shouldSayNoPartyToLeave() {

		Location town = new Location("The bustling heart of Mount Helenis.", "Town Square");
		Player hero = new Player("Hero");
		hero.setCurrentLocation(town);

		LeavePartyCommand cmd = new LeavePartyCommand();
		ParsedInput input = new ParsedInput("leaveparty", new ArrayList<>()); // no args


		CommandResponse resp = cmd.execute(input, hero);


		assertEquals("You don't have a party to leave", resp.getMessage());
		assertTrue(hero.getNpcCollection().isEmpty(), "Player should still have no party.");
		assertTrue(town.getNpcCollection().isEmpty(), "Location NPCs should remain empty.");
	}

	@Test
	void whenLocationEmpty_andPlayerHasParty_shouldMovePartyToLocationAndClearPlayer() {

		Location cave = new Location("Glittering crystals line the walls.", "Crystal Cave");
		Player hero = new Player("Hero");
		hero.setCurrentLocation(cave);

		// Build player's party
		NonPlayerCharacter quartz = new NonPlayerCharacter(
				"Quartz", 13, 12, 17, "I’ll guide you through.", false);
		NonPlayerCharacter iris = new NonPlayerCharacter(
				"Iris", 12, 13, 15, "Stay close.", false);

		hero.getNpcCollection().put("quartz", quartz);
		hero.getNpcCollection().put("iris", iris);

		// Location has no NPCs initially
		assertTrue(cave.getNpcCollection().isEmpty());

		LeavePartyCommand cmd = new LeavePartyCommand();
		ParsedInput input = new ParsedInput("leaveparty", new ArrayList<>());


		CommandResponse resp = cmd.execute(input, hero);


		assertEquals("You left the party", resp.getMessage());


		assertTrue(hero.getNpcCollection().isEmpty(), "Player party should be cleared after leaving.");

		// Location should have the NPCs that were in the player's party
		NonPlayerCharacterCollection locNpcs = cave.getNpcCollection();
		assertFalse(locNpcs.isEmpty(), "Location should now contain the party NPCs.");
		assertTrue(locNpcs.containsKey("quartz"));
		assertTrue(locNpcs.containsKey("iris"));
		assertSame(quartz, locNpcs.get("quartz"));
		assertSame(iris, locNpcs.get("iris"));
	}

	@Test
	void whenLocationOccupied_andPlayerHasParty_shouldRejectAndNotChangeCollections() {

		Location inn = new Location("Warm fires and loud chatter.", "Inn of the Boar");
		Player hero = new Player("Hero");
		hero.setCurrentLocation(inn);

		// Player has a party
		NonPlayerCharacter bram = new NonPlayerCharacter(
				"Bram", 14, 11, 18, "Let’s push back.", false);
		hero.getNpcCollection().put("bram", bram);

		// Location already has NPCs (occupied)
		NonPlayerCharacter lysa = new NonPlayerCharacter(
				"Lysa", 12, 13, 16, "Need another support blade?", false);
		inn.getNpcCollection().put("lysa", lysa);


		int playerPartySizeBefore = hero.getNpcCollection().size();
		int locationNpcSizeBefore = inn.getNpcCollection().size();

		LeavePartyCommand cmd = new LeavePartyCommand();
		ParsedInput input = new ParsedInput("leaveparty", new ArrayList<>());


		CommandResponse resp = cmd.execute(input, hero);


		assertEquals("Location occupied! You cannot leave party in this location.", resp.getMessage());


		assertEquals(playerPartySizeBefore, hero.getNpcCollection().size(),
				"Player's party should remain unchanged when location is occupied.");
		assertTrue(hero.getNpcCollection().containsKey("bram"));

		assertEquals(locationNpcSizeBefore, inn.getNpcCollection().size(),
				"Location NPCs should remain unchanged when rejecting.");
		assertTrue(inn.getNpcCollection().containsKey("lysa"));
	}
}
