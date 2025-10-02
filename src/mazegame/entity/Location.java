package mazegame.entity;

import java.util.List;

import mazegame.entity.utility.NonPlayerCharacterCollection;

public class Location {
	private ExitCollection exitCollection;
	private NonPlayerCharacterCollection npcCollection;
	private Inventory items;
	private String description;
	private String label;

	public Location(String description, String label) {
		this.setDescription(description);
		this.setLabel(label);

		npcCollection = new NonPlayerCharacterCollection();
		items = new Inventory();
		exitCollection = new ExitCollection();
	}

	public Inventory getInventory() {
		return items;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setNpcs(List<NonPlayerCharacter> npcs, boolean isHostile) {
		this.npcCollection.setHostileCollection(isHostile);
		for (NonPlayerCharacter npc : npcs) {
			this.npcCollection.put(npc.getName().toLowerCase(), npc);
		}
	}

	public String toString() {
		return "**********\n" + "You are now in " + this.label + "\n**********\n" + this.description + "\n**********\n"
				+ "Exits found :: " + exitCollection.availableExits() + "\n**********\n"
				+ "You can pick up items below...\n" + this.getInventory().toString() + "\n**********\n"
				+ npcCollection.toString();
	}

	public NonPlayerCharacterCollection getNpcCollection() {
		return npcCollection;
	}

	public void setNpcCollection(NonPlayerCharacterCollection npcCollection) {
		this.npcCollection = npcCollection;
	}

	public ExitCollection getExitCollection() {
		return exitCollection;
	}

}
