package mazegame.entity;

/**
 * Represents the player's gold. Handles adding, spending, and displaying total
 * gold amount.
 */
public class Money {
	private int total; // total amount of gold pieces

	// default constructor with zero gold
	public Money() {
		total = 0;
	}

	// construct with specific amount
	public Money(int total) {
		this.total = total;
	}

	// add gold to total
	public void add(int amount) {
		total += amount;
	}

	// subtract gold if enough is available
	public boolean subtract(int amount) {
		if (amount > total)
			return false; // insufficient funds
		total -= amount;
		return true;
	}

	// calculate weight of gold (100 coins = 1 weight unit)
	public double getWeight() {
		return (double) total / 100;
	}

	// get total amount of gold
	public int getTotal() {
		return total;
	}

	// format gold amount as readable text
	public String toString() {
		if (total < 1)
			return "No gold pieces";
		if (total == 1)
			return "One gold piece";
		return "There are " + total + " gold pieces";
	}
}
