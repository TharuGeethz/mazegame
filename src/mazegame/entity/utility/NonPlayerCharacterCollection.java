package mazegame.entity.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mazegame.entity.NonPlayerCharacter;

/**
 * A specialized HashMap for managing Non Player Characters (NPCs) present
 * either in a location or in the player’s ally party.
 */
public class NonPlayerCharacterCollection extends HashMap<String, NonPlayerCharacter> {

	public NonPlayerCharacterCollection() {
		super();
	}

	// check if an NPC with the given name exists
	public boolean hasNPC(String name) {
		if (name == null)
			return false;
		return this.containsKey(name.toLowerCase());
	}

	// check if there are any hostile NPCs in the collection
	public boolean hasHostileNPCs() {
		for (NonPlayerCharacter npc : this.values()) {
			if (npc.isHostile()) {
				return true;
			}
		}
		return false;
	}

	// return a list of NPCs filtered by hostility (true = hostile, false =
	// friendly)
	public List<NonPlayerCharacter> getNPCsByHostility(boolean hostile) {
		List<NonPlayerCharacter> filteredNPCs = new ArrayList<>();
		for (NonPlayerCharacter npc : this.values()) {
			if (npc.isHostile() == hostile) {
				filteredNPCs.add(npc);
			}
		}
		return filteredNPCs;
	}

	@Override
	public String toString() {
		StringBuilder returnMsg = new StringBuilder();
		returnMsg.append("NPC Characters: ");
		if (this.isEmpty()) {
			returnMsg.append("None present");
		} else {
			for (String name : this.keySet()) {
				returnMsg.append(" << ").append(name).append(" >>");
			}
		}
		return returnMsg.toString();
	}
}
