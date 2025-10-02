package mazegame.entity.utility;


import java.util.HashMap;


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
	
	

//	public List<NonPlayerCharacter> getNPCs() {
//		return new ArrayList<>(this.values());
//	}

	
	public void removeNpc(NonPlayerCharacter npc) {
		if (npc != null) {
			this.remove(npc.getName().toLowerCase());
		}
	}

	public void removeNpcByName(String name) {
		if (name != null) {
			this.remove(name.toLowerCase());
		}
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
