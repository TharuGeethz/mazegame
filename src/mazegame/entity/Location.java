package mazegame.entity;

import java.util.List;
import mazegame.entity.utility.NonPlayerCharacterCollection;

/**
 * Represents a location in the maze game. Holds exits, items and NPCs that can
 * be found within this area.
 */
public class Location {
	private final ExitCollection exitCollection; // exits available from this location
	private NonPlayerCharacterCollection npcCollection; // NPCs in this location
	private Inventory items; // items available to pick up
	private String description; // location description
	private String label; // location name

	// construct a location with description and label
	public Location(String description, String label) {
		this.setDescription(description);
		this.setLabel(label);
		npcCollection = new NonPlayerCharacterCollection();
		items = new Inventory();
		exitCollection = new ExitCollection();
	}

	// get inventory for this location
	public Inventory getInventory() {
		return items;
	}

	// get description
	public String getDescription() {
		return description;
	}

	// set description
	public void setDescription(String description) {
		this.description = description;
	}

	// get label
	public String getLabel() {
		return label;
	}

	// set label
	public void setLabel(String label) {
		this.label = label;
	}

	// set NPCs present at this location
	public void setNpcs(List<NonPlayerCharacter> npcs, boolean isHostile) {
		for (NonPlayerCharacter npc : npcs) {
			this.npcCollection.put(npc.getName().toLowerCase(), npc); // store by lowercase name
		}
	}

	// return location info as formatted text
	public String toString() {
		return "**********\n" + "You are now in " + this.label + "\n**********\n" + this.description + "\n**********\n"
				+ "Exits found :: " + exitCollection.availableExits() + "\n**********\n"
				+ "You can pick up items below...\n" + this.getInventory().toString() + "\n**********\n"
				+ npcCollection.toString();
	}

	// get NPC collection
	public NonPlayerCharacterCollection getNpcCollection() {
		return npcCollection;
	}

	// get exit collection
	public ExitCollection getExitCollection() {
		return exitCollection;
	}
}
