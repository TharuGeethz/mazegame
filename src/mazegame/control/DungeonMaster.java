package mazegame.control;

import java.util.ArrayList;

import mazegame.boundary.IMazeClient;
import mazegame.boundary.IMazeData;
import mazegame.entity.Money;
import mazegame.entity.Player;

/**
 * Arrange the game loop through greet, set up the player and process turns.
 */
public class DungeonMaster {
	private IMazeClient gameClient;
	private IMazeData gameData;
	private Player thePlayer;
	private boolean continueGame;
	private CommandHandler commandHandler;

	public DungeonMaster(IMazeData gameData, IMazeClient gameClient) {
		this.gameData = gameData;
		this.gameClient = gameClient;
		commandHandler = new CommandHandler();
		this.continueGame = true;
	}

	// Show initial welcome text from the data source
	public void printWelcome() {
		gameClient.playerMessage(gameData.getWelcomeMessage());
	}

	// Ask for player name, create the player, and place them at the start
	public void setupPlayer() {
		String playerName = gameClient.getReply("What name do you choose to be known by?");
		thePlayer = new Player(playerName, 11, 11,  20); // average starting life points for a player is 20 life points
		thePlayer.setCurrentLocation(gameData.getStartingLocation());
		thePlayer.getInventory().setGold(new Money(100));

		gameClient.playerMessage("Welcome " + playerName + "\n\n");
		gameClient.playerMessage("You find yourself looking at ");
		gameClient.playerMessage(gameData.getStartingLocation().getDescription());
	}

	// Main loop to greet, set up then keep handling turns until done
	public void runGame() {
		printWelcome();
		setupPlayer();
		while (continueGame) {
			continueGame = handlePlayerTurn();
		}
	}

	// Get a command from the client, process it, print the result, and decide to
	// continue
	private boolean handlePlayerTurn() {
		CommandResponse playerResponse = commandHandler.processTurn(gameClient.getCommand(), thePlayer);
		gameClient.playerMessage(playerResponse.getMessage());
		return !playerResponse.isFinishedGame();
	}
}
