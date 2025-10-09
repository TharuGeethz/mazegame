package mazegame.entity;

/**
 * Represents a Non-Player Character (NPC) in the maze game. NPCs can be either
 * hostile (enemies) or friendly (allies) and they may engage in conversations
 * or combat with the player.
 */
public class NonPlayerCharacter extends Character {
	private Boolean isHostile; // determines if NPC is hostile
	private String conversation; // stores NPC’s dialogue line

	// minimal constructor for quick setup
	public NonPlayerCharacter(String name, boolean hostile) {
		super(name);
		this.isHostile = hostile;
	}

	// full constructor with attributes and conversation
	public NonPlayerCharacter(String name, int strength, int agility, int lifePoints, String conversation,
			boolean isHostile) {
		super(name, strength, agility, lifePoints);
		this.conversation = conversation;
		this.isHostile = isHostile;
	}

	// check hostility status
	public Boolean isHostile() {
		return isHostile;
	}

	// set hostility status
	public void setIsHostile(Boolean isHostile) {
		this.isHostile = isHostile;
	}

	// get conversation dialogue
	public String getConversation() {
		return conversation;
	}

	// set conversation dialogue
	public void setConversation(String conversation) {
		this.conversation = conversation;
	}

	// display NPC details and inventory
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NPC Name: ").append(getName()).append("\n");
		sb.append("Hostile: ").append(isHostile ? "Yes" : "No").append("\n");
		sb.append("Strength: ").append(getStrength()).append("\n");
		sb.append("Agility: ").append(getAgility()).append("\n");
		sb.append("Life Points: ").append(getLifePoints()).append("\n");
		sb.append(getInventory().toString());
		return sb.toString();
	}
}
