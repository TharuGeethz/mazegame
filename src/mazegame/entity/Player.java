package mazegame.entity;

import mazegame.control.CombatSession;
import mazegame.entity.utility.NonPlayerCharacterCollection;

/**
 * Represents the player in the maze game. Extends Character and tracks current
 * location, allies and active combat sessions.
 */
public class Player extends Character {

	private Location currentLocation; // current position in the game world
	private NonPlayerCharacterCollection npcCollection; // player's allies
	private CombatSession combatSession; // active combat session, if any

	// create player with default average strength (10)
	public Player(String name) {
		super(name);
		this.setInventory(new FiniteInventory(10)); // average human strength
		this.npcCollection = new NonPlayerCharacterCollection();
	}

	// create player with specific stats
	public Player(String name, int strength, int agility, int lifePoints) {
		super(name, strength, agility, lifePoints);
		this.setInventory(new FiniteInventory(strength)); // weight limit based on strength
		this.npcCollection = new NonPlayerCharacterCollection();
	}

	// get player's current location
	public Location getCurrentLocation() {
		return currentLocation;
	}

	// set player's current location
	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	// get current combat session
	public CombatSession getCombatSession() {
		return combatSession;
	}

	// set active combat session
	public void setCombatSession(CombatSession combatSession) {
		this.combatSession = combatSession;
	}

	// check if player is in combat
	public boolean inCombat() {
		return combatSession != null && !combatSession.isOver();
	}

	// get player's ally collection
	public NonPlayerCharacterCollection getNpcCollection() {
		return npcCollection;
	}

	// set player's ally collection
	public void setNpcCollection(NonPlayerCharacterCollection npcCollection) {
		this.npcCollection = npcCollection;
	}
}
