package mazegame.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerParty {

	private Map<String, NonPlayerCharacter> npcs;

	public PlayerParty() {

		this.npcs = new HashMap<>();
	}

	public void addAlly(NonPlayerCharacter npc) {
		npcs.put(npc.getName().toLowerCase(), npc);
	}

	public void removeAlly(NonPlayerCharacter npc) {
		npcs.remove(npc.getName().toLowerCase());
	}

	public boolean hasAlly(String name) {
		if (name == null)
			return false;
		return npcs.containsKey(name.toLowerCase());
	}

	// getters

	public List<NonPlayerCharacter> getNPCs() {
		return new ArrayList<>(npcs.values());
	}

}
