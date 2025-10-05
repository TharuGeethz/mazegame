package mazegame.entity.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mazegame.entity.NonPlayerCharacter;

public class NonPlayerCharacterCollection extends HashMap<String, NonPlayerCharacter> {

	public NonPlayerCharacterCollection() {
		super();
	}

	public boolean hasNPC(String name) {
		if (name == null)
			return false;
		return this.containsKey(name.toLowerCase());
	}

	public boolean hasHostileNPCs() {
		for (NonPlayerCharacter npc : this.values()) {
			if (npc.isHostile()) {
				return true;
			}
		}
		return false;
	}

	public List<NonPlayerCharacter> getNPCsByHostility(boolean hostile) {
		List<NonPlayerCharacter> filteredNPCs = new ArrayList<>();

		for (NonPlayerCharacter npc : this.values()) {
			if (npc.isHostile() == hostile) {
				filteredNPCs.add(npc);
			}
		}
		return filteredNPCs;
	}

	public String toString() {
		StringBuilder returnMsg = new StringBuilder();
		returnMsg.append("NP Characters ::");
		if (this.size() == 0)
			returnMsg.append(" No characters");
		else {
			for (String name : this.keySet())
				returnMsg.append(" << " + name + " >>");
		}

		return returnMsg.toString();
	}
}
