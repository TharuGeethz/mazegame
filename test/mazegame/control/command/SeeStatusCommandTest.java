package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.GameStatus;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.item.MiscellaneousItem;
import mazegame.HardCodedData;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeeStatusCommandTest {

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
	void whenPlayerAtStartWithNoBanner_shouldShowInitialGameStatus() {
		// Create basic starting scenario - player with no allies, no banner, not in combat
		Location startLocation = new Location("The beginning of your journey.", "Starting Area");
		Player player = new Player("TestHero");
		player.setCurrentLocation(startLocation);
		
		// Add location to GameStatus
		GameStatus.getInstance().addLocation(startLocation);

		SeeStatusCommand statusCommand = new SeeStatusCommand();
		ParsedInput input = new ParsedInput("status", new ArrayList<>());

		CommandResponse response = statusCommand.execute(input, player);
		String message = response.getMessage();

		// Verify header and player info
		assertTrue(message.contains("GAME STATUS REPORT"), 
				"Should contain the game status report header.");
		assertTrue(message.contains("PLAYER STATUS:"), 
				"Should contain player status section.");
		assertTrue(message.contains("Name: TestHero"), 
				"Should display the player's name.");
		assertTrue(message.contains("Location: Starting Area"), 
				"Should display the current location.");
		assertTrue(message.contains("Status: Peaceful"), 
				"Should show peaceful status when not in combat.");
		
		// Verify party status
		assertTrue(message.contains("PARTY STATUS:"), 
				"Should contain party status section.");
		assertTrue(message.contains("No allies in party"), 
				"Should indicate no allies when party is empty.");
		
		// Verify banner status
		assertTrue(message.contains("Banner: Not found"), 
				"Should show banner as not found initially.");
		assertTrue(message.contains("Primary Objective: Find the banner"), 
				"Should show primary objective when banner not obtained.");
	}

	@Test
	public void whenPlayerHasAlliesAndBanner_shouldShowAdvancedGameStatus() {
		// player with allies and banner
		Location townSquare = new Location("The bustling center of town.", "Town Square");
		Player player = new Player("HeroWithAllies");
		player.setCurrentLocation(townSquare);
		
		// Add allies to player party
		NonPlayerCharacter ally1 = new NonPlayerCharacter("Warrior", 15, 12, 25, "A brave warrior.", false);
		NonPlayerCharacter ally2 = new NonPlayerCharacter("Mage", 10, 14, 20, "A wise mage.", false);
		player.getNpcCollection().put("warrior", ally1);
		player.getNpcCollection().put("mage", ally2);
		
		// player has the banner
		MiscellaneousItem banner = new MiscellaneousItem("banner", 1000, 1);
		player.getInventory().addItem(banner);
		
		// Add location to GameStatus
		GameStatus.getInstance().addLocation(townSquare);

		SeeStatusCommand statusCommand = new SeeStatusCommand();
		ParsedInput input = new ParsedInput("status", new ArrayList<>());

		CommandResponse response = statusCommand.execute(input, player);
		String message = response.getMessage();

		// Verify player status
		assertTrue(message.contains("Name: HeroWithAllies"), 
				"Should display the player's name.");
		assertTrue(message.contains("Location: Town Square"), 
				"Should display the current location.");
		
		// Verify party status shows allies
		assertTrue(message.contains("Allies in party: 2"), 
				"Should show correct number of allies in party.");
		assertTrue(message.contains("Warrior (25 HP)") && message.contains("Mage (20 HP)"), 
				"Should list allies with their health points.");
		
		// Verify banner status
		assertTrue(message.contains("Banner: OBTAINED"), 
				"Should show banner as obtained when player has it.");
		assertTrue(message.contains("Banner obtained - ready to unlock castle entrances"), 
				"Should show advanced objective when banner is obtained.");
		assertTrue(message.contains("Go to Town Square or Crystal Cave and unlock path to castle"), 
				"Should provide next step instructions when banner is obtained.");
	}

	@Test
	public void whenPlayerAtCastleDrawbridge_shouldShowCastleStatus() {
		// player has reached the castle
		Location castleDrawbridge = new Location("The entrance to Gregor's castle.", "Castle Drawbridge");
		Player player = new Player("CastleHero");
		player.setCurrentLocation(castleDrawbridge);
		
		// Add hostile NPC in castle
		List<NonPlayerCharacter> castleGuards = new ArrayList<>();
		NonPlayerCharacter guard1 = new NonPlayerCharacter("Guard1", 16, 11, 30, "A castle guard.", true);
		NonPlayerCharacter guard2 = new NonPlayerCharacter("Guard2", 15, 12, 28, "Another castle guard.", true);
		castleGuards.add(guard1);
		castleGuards.add(guard2);
		castleDrawbridge.setNpcs(castleGuards, true);
		
		// Add location to GameStatus with the specific key
		GameStatus.getInstance().addLocation(castleDrawbridge);
		GameStatus.getInstance().getLocations().put("Castle Drawbridge", castleDrawbridge);

		SeeStatusCommand statusCommand = new SeeStatusCommand();
		ParsedInput input = new ParsedInput("status", new ArrayList<>());

		CommandResponse response = statusCommand.execute(input, player);
		String message = response.getMessage();

		// Verify player status
		assertTrue(message.contains("Name: CastleHero"), 
				"Should display the player's name.");
		assertTrue(message.contains("Location: Castle Drawbridge"), 
				"Should display the castle location.");
		
		// Verify mission progress
		assertTrue(message.contains("MISSION PROGRESS:"), 
				"Should contain mission progress section.");
		assertTrue(message.contains("Reached Gregor's Castle!"), 
				"Should indicate that the player has reached the castle.");
		assertTrue(message.contains("Castle guards remaining: 2"), 
				"Should show the number of remaining castle guards.");
		assertTrue(message.contains("Objective: Defeat Gregor and his guards!"), 
				"Should show the final objective when at the castle with enemies present.");
		
		// Verify maze status
		assertTrue(message.contains("MAZE STATUS:"), 
				"Should contain maze status section.");
		assertTrue(message.contains("Enemies in maze:"), 
				"Should show enemy count in the maze.");
	}
}