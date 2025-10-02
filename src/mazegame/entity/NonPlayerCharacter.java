package mazegame.entity;

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
	


}
