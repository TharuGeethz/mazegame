package mazegame.entity;

/**
 * Represents a directional exit which is connecting two locations. Some exits
 * can be locked and require unlocking before travel.
 */
public class Exit {
	private String description; // text describing the exit
	private Location destination; // destination location this exit leads to
	private boolean isLocked; // whether the exit is locked

	// construct exit with description and destination
	public Exit(String description, Location destination) {
		this.setDescription(description);
		this.setDestination(destination);
	}

	// get destination location
	public Location getDestination() {
		return destination;
	}

	// set destination location
	public void setDestination(Location destination) {
		this.destination = destination;
	}

	// get exit description
	public String getDescription() {
		return description;
	}

	// set exit description
	public void setDescription(String description) {
		this.description = description;
	}

	// check if exit is locked
	public boolean isLocked() {
		return isLocked;
	}

	// lock or unlock the exit
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
}
