package mazegame.entity;

import java.util.ArrayList;
import java.util.List;

public class NPCParty {
	
              
    private List<NonPlayerCharacter> npcs;
    private Location currentLocation;
    boolean movable;

    public NPCParty(Location startLocation, boolean movable) {

        this.currentLocation = startLocation;
        this.movable = movable;
        this.npcs = new ArrayList<>();
    }

    public void addNPC(NonPlayerCharacter npc) {
    	npcs.add(npc);
    }

    public void removeNPC(NonPlayerCharacter npc) {
    	npcs.remove(npc);
    }

    public void moveTo(Location newLocation) {
    	//check movable? 
        this.currentLocation = newLocation;
    }

    // getters

    public List<NonPlayerCharacter> getNPCs() { return npcs; }
    public Location getCurrentLocation() { return currentLocation; }
	
	
	
	
	
	
	
	

}


