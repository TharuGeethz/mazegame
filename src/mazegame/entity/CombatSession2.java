
package mazegame.entity;

import mazegame.entity.item.Item;
import mazegame.entity.item.Weapon;
import mazegame.entity.utility.StrengthTable;

import java.util.*;
import java.util.stream.Collectors;

public class CombatSession2 {

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

	public CombatSession2(Player thePlayer) {
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

	// ---------- ONE round per call ----------
	public String resolveSingleRound() {
		StringBuilder log = new StringBuilder();

		if (!hasAnyHostileAlive()) {
			return "There are no hostile NPCs here.";
		}
		if (thePlayer.getLifePoints() < 1) {
			return "You cannot fight — you have fallen.";
		}

		log.append("===== Round ").append(round).append(" =====\n");

		// Player side (get one random attacker from player+allies)
		log.append(playerPartyPhase()).append("\n");
		log.append(endOfTurnStatus()).append("\n");
		if (isOver()) {
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

	// Player skipped their action (e.g., equip, use potion)
	public String skipPlayerTurn() {
		StringBuilder log = new StringBuilder();
		log.append("===== Round ").append(round).append(" =====\n");
		log.append("You spend your turn doing something else...\n");

		// Now enemy phase
		log.append(enemyPartyPhase()).append("\n");
		log.append(endOfTurnStatus()).append("\n");

		if (isOver()) {
			log.append("\n").append(getCombatConclusion());
		} else {
			round++;
		}

		firstRound = false;
		return log.toString().trim();
	}

	public boolean isOver() {
		return isEnemyPartyDefeated() || isPlayerPartyDefeated();
	}

	public String getCombatConclusion() {
		if (isEnemyPartyDefeated()) {
			if (thePlayer.getLifePoints() > 0 || !livingAllies().isEmpty()) {
				return "All hostile NPCs are defeated. Victory!";
			}
			return "You stand alone, but the enemies are defeated.";
		}
		if (isPlayerPartyDefeated()) {
			return "You have fallen. Your party is defeated.";
		}
		return "Combat has ended.";
	}

	// ---------- Phases: pick ONE attacker each side ----------

	private String playerPartyPhase() {
		StringBuilder log = new StringBuilder("— Player Party Phase —\n");

		List<Character> party = new ArrayList<>();
		if (thePlayer.getLifePoints() > 0)
			party.add(thePlayer);
		party.addAll(livingAllies());

		if (party.isEmpty()) {
			log.append("No one left to act on your side.");
			return log.toString();
		}

		Character attacker;
		
		if (firstRound) {
			attacker = thePlayer;
		} else {
			attacker = pickRandom(party);
		}

		if (attacker == thePlayer) {
			NonPlayerCharacter target;

			if (firstRound && initialPreferredTargetName != null) {
				// try the initial preferred target for the first turn
				target = getSpecificHostile(initialPreferredTargetName);
				initialPreferredTargetName = null;
				if (target == null) { // if player doeswn't choose a target in first round, pick random one
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

	public String advanceCombatUntilPlayerTurn() {
		StringBuilder log = new StringBuilder();

		while (!isOver()) {
			if (isFirstRound()) {
				log.append(resolveSingleRound());
			}
			log.append("===== Round ").append(round).append(" =====\n");

			// --- Player side (random: player or ally) ---
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

			log.append(endOfTurnStatus()).append("\n");
			if (isOver())
				break;

			// --- Enemy side (one random hostile) ---
			log.append(enemyPartyPhase()).append("\n");
			log.append(endOfTurnStatus()).append("\n");

			round++;
			firstRound = false;
		}

		log.append("\n").append(getCombatConclusion());
		return log.toString().trim();
	}

	public String playerAttackAndAdvance(String maybeTargetName) {
		StringBuilder log = new StringBuilder();

		if (!awaitingPlayerAction) {
			// not your turn: just advance until it is or combat ends
			return advanceCombatUntilPlayerTurn();
		}

		log.append("===== Round ").append(round).append(" =====\n");

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
		log.append(endOfTurnStatus()).append("\n");

		if (isOver()) {
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

	public String playerSkipTurnAndAdvance(String reason) {
		StringBuilder log = new StringBuilder();

		if (!awaitingPlayerAction) {
			// not your turn: just advance until it is or ends
			return advanceCombatUntilPlayerTurn();
		}

		log.append("===== Round ").append(round).append(" =====\n");
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

	private List<NonPlayerCharacter> livingAllies() {
		return thePlayer.getNpcCollection().values().stream().filter(n -> !n.isHostile() && n.getLifePoints() > 0)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private List<NonPlayerCharacter> livingHostiles() {
		return location.getNpcCollection().values().stream().filter(n -> n.isHostile() && n.getLifePoints() > 0)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private boolean hasAnyHostileAlive() {
		return !livingHostiles().isEmpty();
	}

	private boolean isPlayerPartyDefeated() {
		return thePlayer.getLifePoints() < 1 && livingAllies().isEmpty();
	}

	private boolean isEnemyPartyDefeated() {
		return livingHostiles().isEmpty();
	}

	private String playerAttacks(NonPlayerCharacter target) {
		Weapon weaponUsed = resolveWeaponForAttack(thePlayer);
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

	private String npcAttacksPlayer(NonPlayerCharacter attacker) {
		Weapon weaponUsed = resolveWeaponForAttack(attacker);
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

	private String npcAttacksNpc(NonPlayerCharacter attacker, NonPlayerCharacter victim) {
		Weapon weaponUsed = resolveWeaponForAttack(attacker);
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

	private <T> T pickRandom(List<T> list) {
		if (list == null || list.isEmpty())
			return null;
		return list.get(rng.nextInt(list.size()));
	}

	private NonPlayerCharacter getSpecificHostile(String keyNameLower) {
		NonPlayerCharacter target = location.getNpcCollection().get(keyNameLower);
		if (target == null)
			return null;
		if (!target.isHostile() || target.getLifePoints() < 1)
			return null;
		return target;
	}

	private Weapon resolveWeaponForAttack(Character character) {
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
		return new Weapon("No Weapon", 0, 0, "1d2"); // fallback
	}

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
