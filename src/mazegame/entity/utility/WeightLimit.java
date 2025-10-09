package mazegame.entity.utility;

import java.util.Collections;
import java.util.HashMap;

/**
 * Singleton class that manages weight-carrying limits for characters. Each
 * strength level corresponds to a maximum weight capacity modifier. Used
 * primarily by FiniteInventory to restrict item carrying based on strength.
 */
public class WeightLimit {

	private static WeightLimit instance; // single shared instance
	private HashMap<Integer, Integer> lookup; // strength → max weight mapping

	// private constructor to prevent external instantiation
	private WeightLimit(HashMap<Integer, Integer> theTable) {
		lookup = theTable;
	}

	// returns the single shared instance (creates it if not already created)
	public static WeightLimit getInstance() {
		if (instance == null) {
			instance = new WeightLimit(new HashMap<>());
		}
		return instance;
	}

	// assigns a weight limit to a specific strength value
	public void setModifier(int strength, int weightLimit) {
		lookup.put(strength, weightLimit);
	}

	// retrieves the weight limit for a given strength value
	// if above known range, return highest defined value
	public int getModifier(int strength) {
		int maxStrength = Collections.max(lookup.keySet());
		if (strength > maxStrength)
			return lookup.get(maxStrength);

		if (lookup.containsKey(strength))
			return lookup.get(strength);
		return -1;
	}
}
