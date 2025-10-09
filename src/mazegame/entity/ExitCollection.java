package mazegame.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds all exits for a given location. Provides methods to add, retrieve and
 * list available exits.
 */
public class ExitCollection {
	private HashMap<String, Exit> exits; // stores exits with direction labels as keys

	// construct an empty exit collection
	public ExitCollection() {
		exits = new HashMap<String, Exit>();
	}

	// add a new exit if not already present
	public boolean addExit(String exitLabel, Exit theExit) {
		if (exits.containsKey(exitLabel))
			return false; // prevent duplicates
		exits.put(exitLabel, theExit);
		return true;
	}

	// get exit by direction label
	public Exit getExit(String exitLabel) {
		return exits.get(exitLabel);
	}

	// get map of all exits
	public Map<String, Exit> getAllExits() {
		return exits;
	}

	// check if a specific exit exists
	public boolean containsExit(String exitLabel) {
		return exits.containsKey(exitLabel);
	}

	// list available exits as formatted string
	public String availableExits() {
		StringBuilder returnMsg = new StringBuilder();
		for (Object label : this.exits.keySet()) {
			returnMsg.append("[ " + label.toString() + " ]");
		}
		return returnMsg.toString();
	}
}
