package mazegame.entity.item;

public class Shield extends Item {
	private int bonus;

	public Shield (String label, int value, int weight, int bonus) {
		super (label, value, weight);
		this.bonus = bonus;
	}

	public int getBonus() {
		return bonus;
	}
}
