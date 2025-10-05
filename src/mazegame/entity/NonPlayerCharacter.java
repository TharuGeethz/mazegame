package mazegame.entity;

import mazegame.entity.item.Armor;
import mazegame.entity.item.Shield;
import mazegame.entity.item.Weapon;

public class NonPlayerCharacter extends Character {
	private Boolean isHostile;
	private String conversation;


	public NonPlayerCharacter(String name, boolean hostile) {
		super(name);
		this.isHostile = hostile;
	}

	public NonPlayerCharacter(String name, int strength, int agility, int lifePoints, String conversation, boolean isHostile) {
		super(name, strength, agility, lifePoints);
		this.conversation = conversation;
		this.isHostile = isHostile;
	}

	public Boolean isHostile() {
		return isHostile;
	}

	public void setIsHostile(Boolean isHostile) {
		this.isHostile = isHostile;
	}

	public String getConversation() {
		return conversation;
	}

	public void setConversation(String conversation) {
		this.conversation = conversation;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NPC Name: ").append(getName()).append("\n");
		sb.append("Hostile: ").append(isHostile ? "Yes" : "No").append("\n");
		sb.append("Strength: ").append(getStrength()).append("\n");
		sb.append("Agility: ").append(getAgility()).append("\n");
		sb.append("Life Points: ").append(getLifePoints()).append("\n");

		// Show items
		sb.append(getInventory().toString());
		return sb.toString();
	}



}
