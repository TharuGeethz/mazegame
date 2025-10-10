package mazegame.control;

import mazegame.entity.Character;
import mazegame.entity.Dice;
import mazegame.entity.FiniteInventory;
import mazegame.entity.Location;
import mazegame.entity.Money;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;
import mazegame.entity.item.Item;
import mazegame.entity.item.Weapon;
import mazegame.entity.utility.StrengthTable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Runs the turn-by-turn combat flow for a location. One actor from each side
 * acts per round; can pause for player choices.
 */
public class CombatSession {

	private final Player thePlayer;
	private final Location location;
	private final Random rng = new Random();

	// very first time
	private boolean firstRound = true;
	private int round = 1;
	private String initialPreferredTargetName = null;

	private boolean awaitingPlayerAction = false;

	public boolean isAwaitingPlayerAction() {
		return awaitingPlayerAction;
	}

	// Capture player and the current location snapshot for this combat
	public CombatSession(Player thePlayer) {
		this.thePlayer = thePlayer;
		this.location = thePlayer.getCurrentLocation();
	}

	/** Set only once, before the first round resolves */
	public void setInitialPreferredTargetName(String name) {
		this.initialPreferredTargetName = (name == null || name.isBlank()) ? null : name.toLowerCase();
	}

	public boolean isFirstRound() {
		return firstRound;
	}

	// Resolve exactly one round: player side then enemy side (if still ongoing)
	public String resolveSingleRound() {
		StringBuilder log = new StringBuilder();

		if (isEnemyPartyDefeated()) {
			return "There are no hostile NPCs here.";
		}
		if (thePlayer.getLifePoints() < 1) {
			return "You cannot fight — you have fallen.";
		}

		log.append("\n\n===== Round ").append(round).append(" =====\n");

		// Player side (get one random attacker from player+allies)
		log.append(playerPartyPhase()).append("\n");

		if (isOver()) {
			log.append(endOfTurnStatus()).append("\n");
			return log.append("\n").append(getCombatConclusion()).toString();
		}

		// Enemy side (one random hostile attacker)
		log.append(enemyPartyPhase()).append("\n");
		log.append(endOfTurnStatus()).append("\n");

		if (isOver()) {
			log.append("\n").append(getCombatConclusion());
		}
		firstRound = false; // after both sides act, first round is done
		round += 1;

		return log.toString().trim();
	}

	// Summarize the final outcome and handle rewards/special endings
	public String getCombatConclusion() {
		if (isEnemyPartyDefeated()) {
//			thePlayer.setStrength(thePlayer.getStrength() + 3);
			String returnStr = "";
			if (thePlayer.getCurrentLocation().getLabel().equals("Castle Drawbridge")) {
				return "You stand victorious at the Castle Drawbridge. Gregor has fallen, and peace returns to the land. The realm hails you as its savior, and your legend will echo through the ages!";
			}

			thePlayer.setStrength(thePlayer.getStrength() + 6);
			FiniteInventory playerInventory = (FiniteInventory) thePlayer.getInventory();
			playerInventory.setStrength(thePlayer.getStrength()); // setting the new weight limit

			returnStr += "\nYour strength increased by 6 points as a reward!";
			if (thePlayer.getCurrentLocation().getInventory().getGold() != null) {
				int goldInLocation = thePlayer.getCurrentLocation().getInventory().getGold().getTotal();
				int newTotal = thePlayer.getInventory().getGold().getTotal() + goldInLocation;
				thePlayer.getInventory().setGold(new Money(newTotal));
				thePlayer.getCurrentLocation().getInventory().getGold().subtract(goldInLocation);
				returnStr += "\nYou got " + goldInLocation + " gold pieces as reward!";
			}

			if (thePlayer.getLifePoints() < 0 && !livingAllies().isEmpty()) {
				return "You were slain but your allies defeated all hostile NPCs in this location. Victory! \nHeal yourself by using a potion ASAP!"
						+ returnStr;
			} else if (thePlayer.getLifePoints() > 0 || !livingAllies().isEmpty()) {
				return "All hostile NPCs are defeated in this location. Victory!" + returnStr;
			}

			return "You stand alone, but the enemies are defeated. Victory!" + returnStr;
		}
		if (isPlayerPartyDefeated()) {
			return "You have fallen. Your party is defeated.";
		}
		return "Combat has ended.";
	}

	// The player's side acts: either the player or a random ally
	private String playerPartyPhase() {
		StringBuilder log = new StringBuilder("— Player Party Phase —\n");

		// Build party: player (if alive) + living allies
		List<Character> party = new ArrayList<>();
		if (thePlayer.getLifePoints() > 0)
			party.add(thePlayer);
		party.addAll(livingAllies());

		if (party.isEmpty()) {
			log.append("No one left to act on your side.");
			return log.toString();
		}

		// First round: player and later rounds: random actor
		Character attacker = firstRound ? thePlayer : pickRandom(party);

		if (attacker == thePlayer) {
			NonPlayerCharacter target;

			if (firstRound && initialPreferredTargetName != null) {
				// try the initial preferred target for the first turn
				target = getSpecificHostile(initialPreferredTargetName);
				initialPreferredTargetName = null;
				if (target == null) { // if player doesn't choose a valid target in first round, pick random one
					log.append("There is no hostile NPC with that name. Picking a random one for you....");
					target = pickRandom(livingHostiles());
				}
			} else {
				// always pick random hostile in later rounds
				target = pickRandom(livingHostiles());
			}

			if (target == null) {
				log.append("No valid hostile target.\n");
			} else {
				log.append(playerAttacks(target)).append("\n");
			}

		} else {
			NonPlayerCharacter ally = (NonPlayerCharacter) attacker;
			NonPlayerCharacter target = pickRandom(livingHostiles());
			if (target == null) {
				log.append("No hostiles remain.\n");
			} else {
				log.append(npcAttacksNpc(ally, target)).append("\n");
			}
		}

		return log.toString().trim();
	}

	// The enemy side acts with a single random hostile
	private String enemyPartyPhase() {
		StringBuilder log = new StringBuilder("— Enemy Party Phase —\n");

		List<NonPlayerCharacter> hostiles = livingHostiles();
		if (hostiles.isEmpty()) {
			log.append("No hostiles remain.");
			return log.toString();
		}

		NonPlayerCharacter attacker = pickRandom(hostiles);

		List<Character> targets = new ArrayList<>();
		if (thePlayer.getLifePoints() > 0)
			targets.add(thePlayer);
		targets.addAll(livingAllies());

		if (targets.isEmpty()) {
			log.append("No targets remain.");
			return log.toString();
		}

		Character victim = pickRandom(targets);
		if (victim instanceof Player) {
			log.append(npcAttacksPlayer(attacker)).append("\n");
		} else {
			log.append(npcAttacksNpc(attacker, (NonPlayerCharacter) victim)).append("\n");
		}

		return log.toString().trim();
	}

	// Autoplay until it becomes the player's turn (or combat ends)
	public String advanceCombatUntilPlayerTurn() {
		StringBuilder log = new StringBuilder();

		while (!isOver()) {
			if (isFirstRound()) {
				log.append(resolveSingleRound());
			}
			log.append("\n\n===== Round ").append(round).append(" =====\n");

			// Player side (random: player or ally)
			log.append("— Player Party Phase —\n");
			List<Character> party = new ArrayList<>();
			if (thePlayer.getLifePoints() > 0)
				party.add(thePlayer);
			party.addAll(livingAllies());

			if (party.isEmpty()) {
				// no one to act on player side so go straight to enemy side
				log.append("No one left to act on your side.");

			} else {
				Character attacker = pickRandom(party);

				if (attacker == thePlayer) {
					// pause here; let UI/command system ask the player what to do
					awaitingPlayerAction = true;
					log.append("It’s your turn! Choose: attack | equip <item> | use <potion> | flee\n");
					return log.toString().trim();
				} else {
					NonPlayerCharacter ally = (NonPlayerCharacter) attacker;
					NonPlayerCharacter target = pickRandom(livingHostiles());
					if (target == null) {
						log.append("No hostiles remain.\n");
					} else {
						log.append(npcAttacksNpc(ally, target)).append("\n");
					}
				}
			}

			if (isOver()) {
				log.append(endOfTurnStatus()).append("\n");
				break;
			}

			// Enemy side (one random hostile)
			log.append(enemyPartyPhase()).append("\n");
			log.append(endOfTurnStatus()).append("\n");

			round++;
			firstRound = false;
		}

		log.append("\n").append(getCombatConclusion());
		return log.toString().trim();
	}

	// Player chooses to attack now, then the round continues
	public String playerAttackAndAdvance(String maybeTargetName) {
		StringBuilder log = new StringBuilder();

		if (!awaitingPlayerAction) {
			// not your turn: just advance until it is or combat ends
			return advanceCombatUntilPlayerTurn();
		}

		// choose target
		NonPlayerCharacter target = null;
		if (firstRound) {
			// first turn: allow preferred target by name (either passed now or set earlier)
			String useName = (maybeTargetName != null && !maybeTargetName.isBlank()) ? maybeTargetName.toLowerCase()
					: this.initialPreferredTargetName;

			if (useName != null) {
				target = getSpecificHostile(useName);
				if (target == null) {
					log.append("No hostile named '").append(useName).append("'. Picking a random target.\n");
				}
				// consume the initial preference once used/attempted
				this.initialPreferredTargetName = null;
			}
		} else if (maybeTargetName != null && !maybeTargetName.isBlank()) {
			log.append("You can't pick a target now; a hostile will be chosen at random.\n");
		}

		if (target == null)
			target = pickRandom(livingHostiles());
		if (target == null) {
			awaitingPlayerAction = false;
			log.append("No valid hostile target.\n");
			log.append("\n").append(getCombatConclusion());
			return log.toString().trim();
		}

		// perform player's attack
		log.append(playerAttacks(target)).append("\n");

		if (isOver()) {
			log.append(endOfTurnStatus()).append("\n");
			awaitingPlayerAction = false;
			log.append("\n").append(getCombatConclusion());
			return log.toString().trim();
		}

		// enemy phase
		awaitingPlayerAction = false; // we’ve consumed the player’s action
		firstRound = false;
		log.append(enemyPartyPhase()).append("\n");
		log.append(endOfTurnStatus()).append("\n");

		if (isOver()) {
			log.append("\n").append(getCombatConclusion());
			return log.toString().trim();
		}

		round++;

		// continue auto until next time it's the player's turn (or end)
		String tail = advanceCombatUntilPlayerTurn();
		if (!tail.isBlank()) {
			log.append("\n").append(tail);
		}
		return log.toString().trim();
	}

	// Player spends the turn (e.g., after equip/use/flee attempt), then continue
	public String playerSkipTurnAndAdvance(String reason) {
		StringBuilder log = new StringBuilder();

		if (!awaitingPlayerAction) {
			// not your turn: just advance until it is or ends
			return advanceCombatUntilPlayerTurn();
		}

		log.append("\n\n===== Round ").append(round).append(" =====\n");
		if (reason != null && !reason.isBlank())
			log.append(reason).append("\n");
		log.append("You spend your turn.\n");

		// enemy phase
		awaitingPlayerAction = false;
		firstRound = false;
		log.append(enemyPartyPhase()).append("\n");
		log.append(endOfTurnStatus()).append("\n");

		if (isOver()) {
			log.append("\n").append(getCombatConclusion());
			return log.toString().trim();
		}

		round++;

		// continue auto until next player turn (or end)
		String tail = advanceCombatUntilPlayerTurn();
		if (!tail.isBlank()) {
			log.append("\n").append(tail);
		}
		return log.toString().trim();
	}

	public boolean isOver() {
		return isEnemyPartyDefeated() || isPlayerPartyDefeated();
	}

	// Helpers to get alive allies/hostiles
	private List<NonPlayerCharacter> livingAllies() {
		return thePlayer.getNpcCollection().values().stream().filter(n -> !n.isHostile() && n.getLifePoints() > 0)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private List<NonPlayerCharacter> livingHostiles() {
		return location.getNpcCollection().values().stream().filter(n -> n.isHostile() && n.getLifePoints() > 0)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private boolean isPlayerPartyDefeated() {
		return thePlayer.getLifePoints() < 1 && livingAllies().isEmpty();
	}

	private boolean isEnemyPartyDefeated() {
		return livingHostiles().isEmpty();
	}

	// Player attack roll and damage resolution against a single target
	private String playerAttacks(NonPlayerCharacter target) {
		Weapon weaponUsed = getWeaponForAttack(thePlayer);
		int strMod = StrengthTable.getInstance().getModifier(thePlayer.getStrength());
		int attackRoll = Dice.roll("1d20");
		int attackScore = attackRoll + strMod;
		int targetAC = target.getArmorClass();

		if (attackScore >= targetAC) {
			int baseDamage = Dice.roll(weaponUsed.getDamage());
			int totalDamage = Math.max(0, baseDamage + strMod);
			int oldHP = target.getLifePoints();
			int newHP = oldHP - totalDamage;
			target.setLifePoints(newHP);

			if (newHP < 1) {
				thePlayer.getCurrentLocation().getNpcCollection().remove(target.getName().toLowerCase());
				thePlayer.getNpcCollection().remove(target.getName().toLowerCase());
				return String.format(
						"You attack %s with %s — HIT! (roll %d + STR %d = %d vs AC %d)\n"
								+ "Damage: %s + STR %d = %d. %s is slain.",
						target.getName(), weaponUsed.getLabel(), attackRoll, strMod, attackScore, targetAC,
						weaponUsed.getDamage(), strMod, totalDamage, target.getName());
			}
			return String.format(
					"You attack %s with %s — HIT! (roll %d + STR %d = %d vs AC %d)\n"
							+ "Damage: %s + STR %d = %d. %s HP: %d → %d.",
					target.getName(), weaponUsed.getLabel(), attackRoll, strMod, attackScore, targetAC,
					weaponUsed.getDamage(), strMod, totalDamage, target.getName(), oldHP, newHP);
		} else {
			return String.format("You attack %s with %s — MISS. (roll %d + STR %d = %d vs AC %d).", target.getName(),
					weaponUsed.getLabel(), attackRoll, strMod, attackScore, targetAC);
		}
	}

	// NPC vs player attack resolution
	private String npcAttacksPlayer(NonPlayerCharacter attacker) {
		Weapon weaponUsed = getWeaponForAttack(attacker);
		int strMod = StrengthTable.getInstance().getModifier(attacker.getStrength());
		int attackRoll = Dice.roll("1d20");
		int attackScore = attackRoll + strMod;
		int targetAC = thePlayer.getArmorClass();

		if (attackScore >= targetAC) {
			int baseDamage = Dice.roll(weaponUsed.getDamage());
			int totalDamage = Math.max(0, baseDamage + strMod);
			int oldHP = thePlayer.getLifePoints();
			int newHP = oldHP - totalDamage;
			thePlayer.setLifePoints(newHP);

			return String.format(
					"%s strikes YOU with %s — HIT! (roll %d + STR %d = %d vs AC %d)\n"
							+ "Damage: %s + STR %d = %d. Your HP: %d → %d.",
					attacker.getName(), weaponUsed.getLabel(), attackRoll, strMod, attackScore, targetAC,
					weaponUsed.getDamage(), strMod, totalDamage, oldHP, newHP);
		} else {
			return String.format("%s attacks YOU — MISS. (roll %d + STR %d = %d vs AC %d).", attacker.getName(),
					attackRoll, strMod, attackScore, targetAC);
		}
	}

	// NPC vs NPC attack resolution
	private String npcAttacksNpc(NonPlayerCharacter attacker, NonPlayerCharacter victim) {
		Weapon weaponUsed = getWeaponForAttack(attacker);
		int strMod = StrengthTable.getInstance().getModifier(attacker.getStrength());
		int attackRoll = Dice.roll("1d20");
		int attackScore = attackRoll + strMod;
		int targetAC = victim.getArmorClass();

		if (attackScore >= targetAC) {
			int baseDamage = Dice.roll(weaponUsed.getDamage());
			int totalDamage = Math.max(0, baseDamage + strMod);
			int oldHP = victim.getLifePoints();
			int newHP = oldHP - totalDamage;
			victim.setLifePoints(newHP);

			if (newHP < 1) {
				thePlayer.getCurrentLocation().getNpcCollection().remove(victim.getName().toLowerCase());
				thePlayer.getNpcCollection().remove(victim.getName().toLowerCase());
				return String.format(
						"%s strikes %s with %s — HIT! (roll %d + STR %d = %d vs AC %d)\n"
								+ "Damage: %s + STR %d = %d. %s is slain.",
						attacker.getName(), victim.getName(), weaponUsed.getLabel(), attackRoll, strMod, attackScore,
						targetAC, weaponUsed.getDamage(), strMod, totalDamage, victim.getName());
			}
			return String.format(
					"%s strikes %s with %s — HIT! (roll %d + STR %d = %d vs AC %d)\n"
							+ "Damage: %s + STR %d = %d. %s HP: %d → %d.",
					attacker.getName(), victim.getName(), weaponUsed.getLabel(), attackRoll, strMod, attackScore,
					targetAC, weaponUsed.getDamage(), strMod, totalDamage, victim.getName(), oldHP, newHP);
		} else {
			return String.format("%s attacks %s — MISS. (roll %d + STR %d = %d vs AC %d).", attacker.getName(),
					victim.getName(), attackRoll, strMod, attackScore, targetAC);
		}
	}

	// Utility: pick a random element or null
	private <T> T pickRandom(List<T> list) {
		if (list == null || list.isEmpty())
			return null;
		return list.get(rng.nextInt(list.size()));
	}

	// Lookup a specific hostile by lowercase key, ensuring it’s alive and hostile
	private NonPlayerCharacter getSpecificHostile(String keyNameLower) {
		NonPlayerCharacter target = location.getNpcCollection().get(keyNameLower);
		if (target == null)
			return null;
		if (!target.isHostile() || target.getLifePoints() < 1)
			return null;
		return target;
	}

	// Prefer equipped weapon; otherwise first weapon in inventory; fallback to 1d2
	private Weapon getWeaponForAttack(Character character) {
		Weapon equipped = character.getEquippedWeapon();
		if (equipped != null) {
			return equipped;
		}

		if (character.getInventory() != null) {
			for (Item item : character.getInventory().getItemList().values()) {
				if (item instanceof Weapon)
					return (Weapon) item;
			}
		}
		return new Weapon("No Weapon", 0, 0, "1d2"); // no weapon
	}

	// Print compact end-of-turn HP for player and all NPCs (hostiles first)
	private String endOfTurnStatus() {
		StringBuilder sb = new StringBuilder("— Status —\n");
		sb.append(String.format("Player: %d HP\n", thePlayer.getLifePoints()));

		List<NonPlayerCharacter> all = new ArrayList<>(location.getNpcCollection().values());
		all.addAll(thePlayer.getNpcCollection().values());
		all.sort(Comparator.comparing(NonPlayerCharacter::isHostile).reversed()
				.thenComparing(NonPlayerCharacter::getName));
		for (NonPlayerCharacter n : all) {
			sb.append(String.format("%s [%s]: %d HP\n", n.getName(), n.isHostile() ? "Hostile" : "Ally",
					n.getLifePoints()));
		}
		return sb.toString().trim();
	}
}
