package mazegame;

import java.util.Scanner;
import mazegame.boundary.IMazeClient;

/**
 * A simple text based client for interacting with the maze game through the
 * console. Handles reading player input and displaying messages.
 */
public class SimpleConsoleClient implements IMazeClient {

	// ask a question and return player's input
	public String getReply(String question) {
		System.out.println("\n" + question + " ");
		Scanner in = new Scanner(System.in);
		return in.nextLine();
	}

	// display a message to the player
	public void playerMessage(String message) {
		System.out.print(message);
	}

	// prompt player for the next command
	public String getCommand() {
		System.out.print("\n\n>>>\t");
		return new Scanner(System.in).nextLine();
	}
}
