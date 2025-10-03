package mazegame.control;

import mazegame.HardCodedData;
import mazegame.SimpleConsoleClient;

public class MazeGame {
	public static void main(String[] args) {
		DungeonMaster theDm = new DungeonMaster(new HardCodedData(), new SimpleConsoleClient());
		theDm.runGame();
	}
}
