package mazegame.control;

import mazegame.HardCodedData;
import mazegame.SimpleConsoleClient;

/**
 * Entry point for the Maze Game. Initializes the DungeonMaster with hardcoded
 * data and a console client.
 */
public class MazeGame {
	public static void main(String[] args) {
		DungeonMaster theDm = new DungeonMaster(new HardCodedData(), new SimpleConsoleClient());
		theDm.runGame(); // Start the main game loop
	}
}
