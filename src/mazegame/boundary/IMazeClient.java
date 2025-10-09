package mazegame.boundary;

/**
 * Interface for client I/O in the Maze Game. Defines how messages and input are
 * exchanged between player and game engine.
 */
public interface IMazeClient {
	// Ask a question and get the player's typed response
	public String getReply(String question);

	// Send a message or description to the player
	public void playerMessage(String message);

	// Retrieve the next player command for processing
	public String getCommand();
}
