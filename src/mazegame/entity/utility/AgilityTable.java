package mazegame.entity.utility;

import java.util.Collections;
import java.util.HashMap;

/**
 * Singleton class that stores and provides agility modifiers which are used to
 * calculate character armor class and other agility based effects.
 */
public class AgilityTable {
	private static AgilityTable instance; // single shared instance
	private HashMap<Integer, Integer> lookup; // agility to modifier map

	// private constructor for singleton pattern
	private AgilityTable(HashMap<Integer, Integer> theTable) {
		lookup = theTable;
	}

	// get or create the single shared instance
	public static AgilityTable getInstance() {
		if (instance == null)
			instance = new AgilityTable(new HashMap<>());
		return instance;
	}

	// set a modifier value for a specific agility level
	public void setModifier(int agility, int modifier) {
		lookup.put(agility, modifier);
	}

	// get modifier for a given agility value
	// if agility exceeds known range, return highest available modifier
	public int getModifier(int agility) {
		int maxAgility = Collections.max(lookup.keySet());
		if (agility > maxAgility)
			return lookup.get(maxAgility);

		if (lookup.containsKey(agility))
			return lookup.get(agility);
		return -1;
	}
}
