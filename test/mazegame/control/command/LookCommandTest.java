package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Exit;
import mazegame.entity.Location;
import mazegame.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LookCommandTest {

	@Test
	void whenNoArguments_returnsLocationToString() {
		// Location(description, name)
		Location town = new Location("The bustling heart of Mount Helenis.", "Town Square");
		Player hero = new Player("Hero");
		hero.setCurrentLocation(town);

		LookCommand cmd = new LookCommand();
		ParsedInput input = new ParsedInput("look", new ArrayList<>());

		CommandResponse resp = cmd.execute(input, hero);

		assertEquals(town.toString(), resp.getMessage(),
				"With no args, look should return the location's toString().");
	}

	@Test
	void whenExitLabelProvided_returnsThatExitDescription() {
		Location town = new Location("The bustling heart of Mount Helenis.", "Town Square");
		Location cave = new Location("Walls sparkle with embedded crystals.", "Crystal Cave");

		// Wire exit exactly like your HardCodedData
		String exitLabel = "south";
		String exitDesc  = "A narrow path leads toward a glittering cave entrance.";
		town.getExitCollection().addExit(exitLabel, new Exit(exitDesc, cave));

		Player hero = new Player("Hero");
		hero.setCurrentLocation(town);

		LookCommand cmd = new LookCommand();
		ParsedInput input = new ParsedInput("look", new ArrayList<>(List.of(exitLabel)));

		CommandResponse resp = cmd.execute(input, hero);

		assertEquals(exitDesc, resp.getMessage(),
				"Looking at a valid exit label should return that Exit's description.");
	}

	@Test
	void whenArgumentNotAnExit_returnsCantFindMessage() {
		Location town = new Location("The bustling heart of Mount Helenis.", "Town Square");
		Location road = new Location("A dusty road heading north.", "North Road");

		// Only 'north' exists
		town.getExitCollection().addExit("north",
				new Exit("The road back toward the Whispering Marsh.", road));

		Player hero = new Player("Hero");
		hero.setCurrentLocation(town);

		LookCommand cmd = new LookCommand();
		ParsedInput input = new ParsedInput("look", new ArrayList<>(List.of("west"))); // not present

		CommandResponse resp = cmd.execute(input, hero);

		assertEquals("Can't find that to look at here!", resp.getMessage(),
				"Unknown target should yield the default 'can't find' message.");
	}
}
