package mazegame.control;

import java.util.ArrayList;

import mazegame.boundary.IMazeClient;
import mazegame.boundary.IMazeData;
import mazegame.entity.Money;
import mazegame.entity.Player;

public class DungeonMaster {
	private IMazeClient gameClient;
	private IMazeData gameData;
	private Player thePlayer;
	private boolean continueGame;
	private ArrayList<String> commands;
	private CommandHandler commandHandler;

	public DungeonMaster(IMazeData gameData, IMazeClient gameClient) {
		this.gameData = gameData;
		this.gameClient = gameClient;
		commandHandler = new CommandHandler();
		this.continueGame = true;
		commands = new ArrayList<String>();
		commands.add("quit");
		commands.add("move");
		commands.add("go");
		commands.add("look");
	}

	public void printWelcome() {
		gameClient.playerMessage(gameData.getWelcomeMessage());
	}

	public void setupPlayer() {
		String playerName = gameClient.getReply("What name do you choose to be known by?");
		thePlayer = new Player(playerName, 20, 26, 20); // average starting life points for a player is 20 life points
		thePlayer.setCurrentLocation(gameData.getStartingLocation());
		thePlayer.getInventory().setGold(new Money(100));

		gameClient.playerMessage("Welcome " + playerName + "\n\n");
		gameClient.playerMessage("You find yourself looking at ");
		gameClient.playerMessage(gameData.getStartingLocation().getDescription());

		// gameClient.getReply("<<Hit Enter to exit>>");
	}

	public void runGame() {
		printWelcome();
		setupPlayer();
		while (continueGame) {
			continueGame = handlePlayerTurn();
		}
	}

	private boolean handlePlayerTurn() {
		CommandResponse playerResponse = commandHandler.processTurn(gameClient.getCommand(), thePlayer);
		gameClient.playerMessage(playerResponse.getMessage());
		return !playerResponse.isFinishedGame();
	}
}
