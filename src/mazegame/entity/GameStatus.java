package mazegame.entity;

import java.util.HashMap;

/**
 * Singleton class that maintains global game state. Stores references to all
 * locations and shops in the maze game.
 */
public class GameStatus {

	private static GameStatus instance; // single shared instance
	private HashMap<String, Location> locations; // all game locations
	private HashMap<String, Blacksmith> shops; // all shop locations

	// private constructor for singleton
	private GameStatus() {
		locations = new HashMap<>();
		shops = new HashMap<>();
	}

	// get or create the single GameStatus instance
	public static GameStatus getInstance() {
		if (instance == null) {
			instance = new GameStatus();
		}
		return instance;
	}

	// register a new location in the world
	public GameStatus addLocation(Location location) {
		this.locations.put(location.getLabel(), location);
		return this;
	}

	// register a new shop in the world
	public GameStatus addShop(Blacksmith shop) {
		this.shops.put(shop.getLabel(), shop);
		return this;
	}

	// get all registered locations
	public HashMap<String, Location> getLocations() {
		return locations;
	}

	// get all registered shops
	public HashMap<String, Blacksmith> getShops() {
		return shops;
	}
}
