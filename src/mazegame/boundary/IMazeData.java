package mazegame.boundary;

import mazegame.entity.Location;

/**
 * Interface which defines how game data (like the map and intro text) is
 * provided. Gives the starting location and the welcome message for the Maze
 * Game.
 */
public interface IMazeData {
	// Returns the initial starting location of the player
	Location getStartingLocation();

	// Returns the introductory text displayed at game start
	String getWelcomeMessage();
}
