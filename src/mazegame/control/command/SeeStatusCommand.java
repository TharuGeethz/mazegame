package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.GameStatus;
import mazegame.entity.Location;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;

/**
 * Command to print a concise, formatted snapshot of the game state: player
 * stats, party, world summary, key items and mission progress.
 */
public class SeeStatusCommand implements Command {

	public CommandResponse execute(ParsedInput userInput, Player thePlayer) {
		StringBuilder response = new StringBuilder();

		// Header
		response.append("═══════════════════════════════════════\n");
		response.append("           GAME STATUS REPORT           \n");
		response.append("═══════════════════════════════════════\n\n");

		// Player Status
		response.append("PLAYER STATUS:\n");
		response.append("   Name: ").append(thePlayer.getName()).append("\n");
		response.append("   Location: ").append(thePlayer.getCurrentLocation().getLabel()).append("\n");
		response.append("   Health: ").append(thePlayer.getLifePoints()).append(" HP\n");
		response.append("   Strength: ").append(thePlayer.getStrength()).append("\n");
		response.append("   Agility: ").append(thePlayer.getAgility()).append("\n");
		response.append("   Armor Class: ").append(thePlayer.getArmorClass()).append("\n");
		response.append("   Gold: ").append(thePlayer.getInventory().getGold().getTotal()).append(" pieces\n");

		// Combat Status
		if (thePlayer.inCombat()) {
			response.append("   Status: IN COMBAT\n");
		} else {
			response.append("   Status: Peaceful\n");
		}
		response.append("\n");

		// Party Status
		response.append("PARTY STATUS:\n");
		if (thePlayer.getNpcCollection().isEmpty()) {
			response.append("   No allies in party\n");
		} else {
			int aliveAllies = 0;
			StringBuilder allyNames = new StringBuilder();

			for (NonPlayerCharacter ally : thePlayer.getNpcCollection().values()) {
				if (!ally.isHostile()) {
					if (ally.getLifePoints() > 0) {
						aliveAllies++;
						if (allyNames.length() > 0)
							allyNames.append(", ");
						allyNames.append(ally.getName()).append(" (").append(ally.getLifePoints()).append(" HP)");
					}
				}
			}

			response.append("   Allies in party: ").append(aliveAllies).append("\n");
			if (aliveAllies > 0) {
				response.append("   Current party: ").append(allyNames.toString()).append("\n");
			}
		}
		response.append("\n");

		// Maze stsatus
		response.append("MAZE STATUS:\n");
		int locationCount = GameStatus.getInstance().getLocations().size();
		int shopCount = GameStatus.getInstance().getShops().size();
		response.append("   Total locations: ").append(locationCount).append("\n");
		response.append("   Total shops: ").append(shopCount).append("\n");

		// Count NPCs across all locations
		int aliveAllies = 0;
		int aliveEnemies = 0;

		for (Location location : GameStatus.getInstance().getLocations().values()) {
			for (NonPlayerCharacter npc : location.getNpcCollection().values()) {
				if (npc.isHostile()) {
					if (npc.getLifePoints() > 0) {
						aliveEnemies++;
					}
				} else {
					if (npc.getLifePoints() > 0) {
						aliveAllies++;
					}
				}
			}
		}

		response.append("   Allies in maze: ").append(aliveAllies).append(" alive\n");
		response.append("   Enemies in maze: ").append(aliveEnemies).append(" alive\n");
		response.append("\n");

		// Key Items Status
		response.append("IMPORTANT ITEMS:\n");
		boolean hasBanner = thePlayer.hasItem("banner");
		response.append("   Banner: ").append(hasBanner ? "OBTAINED" : "Not found").append("\n");

		if (!hasBanner) {
			// Check if Philip and gang are defeated at Inn of the Boar
			Location innOfTheBoar = GameStatus.getInstance().getLocations().get("Inn of the Boar");
			if (innOfTheBoar != null) {
				boolean philipDefeated = true;
				int hostilesAlive = 0;
				for (NonPlayerCharacter npc : innOfTheBoar.getNpcCollection().values()) {
					if (npc.isHostile() && npc.getLifePoints() > 0) {
						philipDefeated = false;
						hostilesAlive++;
					}
				}
				if (!philipDefeated) {
					response.append("   Hint: Defeat Philip's gang at Inn of the Boar (").append(hostilesAlive)
							.append(" enemies remain)\n");
				} else {
					response.append("   Hint: Banner is now available at Inn of the Boar!\n");
				}
			}
		}
		response.append("\n");

		// Mission Progress
		response.append("MISSION PROGRESS:\n");
		Location castleDrawbridge = GameStatus.getInstance().getLocations().get("Castle Drawbridge");
		boolean reachedCastle = thePlayer.getCurrentLocation().equals(castleDrawbridge);

		if (reachedCastle) {
			response.append("   Reached Gregor's Castle!\n");
			// Check if there are still enemies at the castle
			int castleEnemies = 0;
			for (NonPlayerCharacter npc : castleDrawbridge.getNpcCollection().values()) {
				if (npc.isHostile() && npc.getLifePoints() > 0) {
					castleEnemies++;
				}
			}
			if (castleEnemies > 0) {
				response.append("   Castle guards remaining: ").append(castleEnemies).append("\n");
				response.append("   Objective: Defeat Gregor and his guards!\n");
			} else {
				response.append("   Castle cleared! Victory may be within reach!\n");
			}
		} else {
			if (hasBanner) {
				response.append("   Banner obtained - ready to unlock castle entrances!\n");
				response.append("   Objective: Go to Town Square or Crystal Cave and unlock path to castle\n");
			} else {
				response.append("   Primary Objective: Find the banner to access Gregor's castle\n");
				response.append("   Next step: Defeat Philip and his gang at Inn of the Boar\n");
			}
		}

		response.append("\n═══════════════════════════════════════\n");

		return new CommandResponse(response.toString());
	}
}
