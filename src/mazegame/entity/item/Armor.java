package mazegame.entity.item;

public class Armor extends Item {
	private int bonus;	

	public Armor (String label, int value, int weight, int bonus) {
		super (label, value, weight);
		this.bonus = bonus;
	}
	
	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
}
