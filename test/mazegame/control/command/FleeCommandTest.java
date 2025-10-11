package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.control.CombatSession;
import mazegame.entity.Exit;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FleeCommandTest {
	@BeforeEach
	void setUp() {
		WeightLimit weightLimitTable = WeightLimit.getInstance();
		weightLimitTable.setModifier(10, 66);
		weightLimitTable.setModifier(20, 266);
	}

	@Test
	void fleeingFromSingleExit_movesToThatDestination_andMessageMentionsIt() {

		Location origin = new Location("Start area", "Origin");
		Location meadow = new Location("A quiet meadow.", "Meadow");
		origin.getExitCollection().addExit("east", new Exit("A small path to a meadow.", meadow));

		Player hero = new Player("Hero");
		hero.setCurrentLocation(origin);

		FleeCommand flee = new FleeCommand();
		ParsedInput input = new ParsedInput("flee", new ArrayList<>());


		CommandResponse resp = flee.execute(input, hero);


		assertSame(meadow, hero.getCurrentLocation(), "With one exit, fleeing must move to that destination.");
		assertTrue(resp.getMessage().contains("Meadow"), "Message should include destination label.");
		assertTrue(resp.getMessage().contains(hero.getCurrentLocation().toString()),
				"Message should include current location's description.");
	}

	@Test
	void fleeingFromMultipleExits_movesToOneOfThem_andNotStayAtOrigin() {

		Location origin = new Location("Crossroads", "Origin");
		Location north = new Location("Cold wind blows.", "North Ridge");
		Location south = new Location("Warm fields lie ahead.", "South Fields");
		origin.getExitCollection().addExit("north", new Exit("Trail to the ridge.", north));
		origin.getExitCollection().addExit("south", new Exit("Road to the fields.", south));

		Player hero = new Player("Hero");
		hero.setCurrentLocation(origin);

		FleeCommand flee = new FleeCommand();
		ParsedInput input = new ParsedInput("flee", new ArrayList<>());


		CommandResponse resp = flee.execute(input, hero);


		Location newLoc = hero.getCurrentLocation();
		assertNotSame(origin, newLoc, "Fleeing should move the player away from the origin.");
		assertTrue(Set.of(north, south).contains(newLoc),
				"New location must be one of the available exit destinations.");
		assertTrue(resp.getMessage().contains(newLoc.getLabel()),
				"Message should mention the actual destination chosen.");
	}

	@Test
	void fleeing_clearsCombatSession() {

		Location origin = new Location("Arena", "Origin");
		Location escape = new Location("Dark alley", "Alley");
		origin.getExitCollection().addExit("west", new Exit("An alley to the west.", escape));

		Player hero = new Player("Hero");
		hero.setCurrentLocation(origin);
		// Give the player a not null combat session
		hero.setCombatSession(new CombatSession(hero));
		assertNotNull(hero.getCombatSession(), "Precondition: combat session should be set.");

		FleeCommand flee = new FleeCommand();
		ParsedInput input = new ParsedInput("flee", new ArrayList<>());


		flee.execute(input, hero);

		assertNull(hero.getCombatSession(), "Fleeing should set the player's combat session to null.");
		assertSame(escape, hero.getCurrentLocation(), "Player should have moved to an exit destination.");
	}
}
