package mazegame.entity.item;

public class Weapon extends Item {
	
	private String damage;

    public Weapon(String label, int value, int weight, String damage ) {
         super (label, value, weight);
         this.damage = damage;
    }

    public String getDamage() {
        return damage;
    }
}
