package mazegame.entity;

import mazegame.control.CombatSession;
import mazegame.entity.utility.NonPlayerCharacterCollection;

public class Player extends Character {

	private Location currentLocation;
	private NonPlayerCharacterCollection npcCollection;

	private CombatSession combatSession;

	public Player(String name) {
		super(name);
		npcCollection = new NonPlayerCharacterCollection();
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}
	
	public CombatSession getCombatSession() {
	    return combatSession;
	}

	public void setCombatSession(CombatSession combatSession) {
	    this.combatSession = combatSession;
	}

	public boolean inCombat() {
	    return combatSession != null && !combatSession.isOver();
	}

	
    public NonPlayerCharacterCollection getNpcCollection() {
        return npcCollection;
    }

    public void setNpcCollection(NonPlayerCharacterCollection npcCollection) {
        
        this.npcCollection = npcCollection;
    }



	
	
}
