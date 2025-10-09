package mazegame.entity;

import java.util.Random;

/**
 * Dice rolling helper for the game. Supports single die, arbitrary sides, and
 * "XdY" notation.
 */
public class Dice {
	private final int faces; // number of faces for this die instance
	private static final Random generator = new Random(); // shared random number generator

	// construct a die with given number of faces
	public Dice(int faces) {
		this.faces = faces;
	}

	// roll this die once, returns 1..faces
	public int roll() {
		return generator.nextInt(this.faces) + 1; // plus one to shift 0..faces-1 into 1..faces
	}

	// roll a die with given sides once, returns 1..diceSides
	public static int roll(int diceSides) {
		return generator.nextInt(diceSides) + 1; // plus one to shift 0..sides-1 into 1..sides
	}

	// roll using damage notation like "1d8" or "2d6"
	public static int roll(String damageNotation) {
		String s = damageNotation.trim().toLowerCase(); // normalized form like "1d8"
		int dPosition = s.indexOf('d'); // index of separator
		if (dPosition <= 0 || dPosition == s.length() - 1) {
			throw new IllegalArgumentException("Bad dice: " + damageNotation); // must be <count>d<sides>
		}

		int count = Integer.parseInt(s.substring(0, dPosition).trim()); // number of dice
		int sides = Integer.parseInt(s.substring(dPosition + 1).trim()); // faces per die
		if (count <= 0 || sides <= 0) {
			throw new IllegalArgumentException("Dice values must be > 0: " + damageNotation); // validate inputs
		}
		return roll(count, sides); // delegate to multi-die roller
	}

	// roll multiple dice and sum the total
	private static int roll(int diceCount, int diceSides) {
		int total = 0; // running sum
		for (int i = 0; i < diceCount; i++) {
			total += roll(diceSides); // accumulate single die result
		}
		return total; // final total
	}
}
