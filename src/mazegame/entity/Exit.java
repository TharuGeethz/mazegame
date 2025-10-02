package mazegame.entity;

public class Exit {
	private String description;
	private Location destination;
	private boolean isLocked;  //exits may be locked in which case the player will need to unlock the exit before proceeding.
	
	public Exit (String description, Location destination)
	{
		this.setDescription(description);
		this.setDestination(destination);
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	
	
	
	

}
