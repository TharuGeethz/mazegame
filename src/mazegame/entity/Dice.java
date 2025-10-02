package mazegame.entity;

import java.util.Random;

public class Dice {
	private int faces;
	private static final Random generator = new Random();

	public Dice(int faces) {
		this.faces = faces;
	}

	public int roll() {
		return generator.nextInt(this.faces) + 1;
	}

	public static int roll(int diceSides) {
		return generator.nextInt(diceSides) + 1; // adding one because generator omits the last number and starts from 0
	}

	public static int roll(String damageNotation) {
		String s = damageNotation.trim().toLowerCase(); // like "1d8"
		int dPosition = s.indexOf('d');
		if (dPosition <= 0 || dPosition == s.length() - 1) {
			throw new IllegalArgumentException("Bad dice: " + damageNotation);
		}

		int count = Integer.parseInt(s.substring(0, dPosition).trim());
		int sides = Integer.parseInt(s.substring(dPosition + 1).trim());
		if (count <= 0 || sides <= 0) {
			throw new IllegalArgumentException("Dice values must be > 0: " + damageNotation);
		}
		return roll(count, sides);
	}

	private static int roll(int diceCount, int diceSides) {
		int total = 0;
		for (int i = 0; i < diceCount; i++) {
			total += roll(diceSides);
		}
		return total;
	}
}
