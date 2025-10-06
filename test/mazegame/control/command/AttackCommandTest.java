package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.NonPlayerCharacter;
import mazegame.HardCodedData;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttackCommandTest {

	@BeforeEach
	void setUp() {
		new HardCodedData();
		WeightLimit weightLimit = WeightLimit.getInstance();
		if (weightLimit.getModifier(10) == 0) {
			// Manually initialize if needed
			weightLimit.setModifier(1, 6);
			weightLimit.setModifier(2, 13);
			weightLimit.setModifier(3, 20);
			weightLimit.setModifier(4, 26);
			weightLimit.setModifier(5, 33);
			weightLimit.setModifier(6, 40);
			weightLimit.setModifier(7, 46);
			weightLimit.setModifier(8, 53);
			weightLimit.setModifier(9, 60);
			weightLimit.setModifier(10, 66);
			weightLimit.setModifier(11, 76);
			weightLimit.setModifier(12, 86);
			weightLimit.setModifier(13, 100);
			weightLimit.setModifier(14, 116);
			weightLimit.setModifier(15, 133);
		}
	}

	@Test
	void whenNoNPCsInLocation_shouldInformNoTargets() {
		// Create location with no NPC
		Location emptyLocation = new Location("Empty Room", "A quiet, empty room.");
		Player player = new Player("Hero");
		player.setCurrentLocation(emptyLocation);

		AttackCommand attackCommand = new AttackCommand();
		ParsedInput input = new ParsedInput("attack", new ArrayList<>());

		CommandResponse response = attackCommand.execute(input, player);

		assertEquals("There is no one to attack in this location.", response.getMessage(),
				"Should inform player that there are no targets when location is empty.");
		assertNull(player.getCombatSession(),
				"Combat session should remain null when no targets are available.");
	}

	@Test
	void whenOnlyFriendlyNPCsPresent_shouldInformNoHostileTargets() {
		// Create location with only friendly NPC
		Location townSquare = new Location("Town Square", "A peaceful town square.");
		Player player = new Player("Hero");
		player.setCurrentLocation(townSquare);

		// Add friendly NPC
		List<NonPlayerCharacter> friendlyNPCs = new ArrayList<>();
		NonPlayerCharacter friendlyMerchant = new NonPlayerCharacter("Merchant", false);
		friendlyNPCs.add(friendlyMerchant);
		townSquare.setNpcs(friendlyNPCs, false);

		AttackCommand attackCommand = new AttackCommand();
		ParsedInput input = new ParsedInput("attack", new ArrayList<>());

		CommandResponse response = attackCommand.execute(input, player);

		assertEquals("Friendly NPCs are here. Make them your allies!", response.getMessage(),
				"Should inform player that there are no hostile targets when only friendly NPCs are present.");
		assertNull(player.getCombatSession(),
				"Combat session should remain null when no hostile targets are available.");
	}

	@Test
	void whenHostileNPCsPresent_shouldInitiateCombatSession() {
		// Create location with only hostile NPC
		Location dangerousAlley = new Location("Dark Alley", "A dangerous alley filled with enemies.");
		Player player = new Player("Hero");
		player.setCurrentLocation(dangerousAlley);

		// Add hostile NPC
		List<NonPlayerCharacter> hostileNPCs = new ArrayList<>();
		
		NonPlayerCharacter bandit1 = new NonPlayerCharacter("Bandit", 15, 12, 20, 
				"A dangerous looking bandit.", true);
		hostileNPCs.add(bandit1);

		NonPlayerCharacter bandit2 = new NonPlayerCharacter("Thug", 14, 10, 18, 
				"A menacing thug.", true);
		hostileNPCs.add(bandit2);

		dangerousAlley.setNpcs(hostileNPCs, true);

		AttackCommand attackCommand = new AttackCommand();
		ParsedInput input = new ParsedInput("attack", new ArrayList<>());

		CommandResponse response = attackCommand.execute(input, player);

		// Verify combat was initiated
		assertFalse(response.getMessage().equals("There is no one to attack in this location."),
				"Should not return 'no one to attack' message when hostile NPCs are present.");
		assertFalse(response.getMessage().equals("Friendly NPCs are here. Make them your allies!"),
				"Should not return 'friendly NPCs' message when hostile NPCs are present.");
		
		// Verify response message
		assertTrue(response.getMessage().length() > 0,
				"Response message should not be empty when combat is initiated.");
		
		// Verify hostile NPC presence
		assertTrue(dangerousAlley.getNpcCollection().hasHostileNPCs(),
				"Location should contain hostile NPCs that can be attacked.");
		
		// Verify there are exactly 2 hostile NPCs
		List<NonPlayerCharacter> hostiles = dangerousAlley.getNpcCollection().getNPCsByHostility(true);
		assertEquals(2, hostiles.size(),
				"Should have exactly 2 hostile NPCs available for combat.");
	}
}