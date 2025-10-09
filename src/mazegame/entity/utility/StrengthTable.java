package mazegame.entity.utility;

import java.util.Collections;
import java.util.HashMap;

/**
 * Singleton class that manages strength modifiers used in the game. Each
 * strength value maps to a numeric modifier which influences damage output,
 * weight capacity and other physical actions.
 */
public class StrengthTable {
	private static StrengthTable instance; // singleton instance
	private HashMap<Integer, Integer> lookup; // strength → modifier map

	// private constructor prevents external instantiation
	private StrengthTable(HashMap<Integer, Integer> theTable) {
		lookup = theTable;
	}

	// returns the single instance (creates it if not yet initialized)
	public static StrengthTable getInstance() {
		if (instance == null) {
			instance = new StrengthTable(new HashMap<>());
		}
		return instance;
	}

	// assigns a modifier to a given strength value
	public void setModifier(int strength, int modifier) {
		lookup.put(strength, modifier);
	}

	// retrieves the modifier for the provided strength value
	// if the value exceeds defined range, returns the max defined modifier
	public int getModifier(int strength) {
		int maxStrength = Collections.max(lookup.keySet());
		if (strength > maxStrength)
			return lookup.get(maxStrength);

		if (lookup.containsKey(strength))
			return lookup.get(strength);
		return -1;
	}
}
