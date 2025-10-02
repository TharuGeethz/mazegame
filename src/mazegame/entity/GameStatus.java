package mazegame.entity;

import java.util.HashMap;

public class GameStatus {

    private static GameStatus instance;

    private HashMap<String, Location> locations;
    private HashMap<String, Blacksmith> shops;

    private GameStatus() {
        locations = new HashMap<>();
        shops = new HashMap<>();
    }

    public static GameStatus getInstance() {
        if (instance == null) {
            instance = new GameStatus();
        }
        return instance;
    }

    public GameStatus addLocation(Location location) {
        this.locations.put(location.getLabel(), location);
        return this;
    }

    public GameStatus addShop(Blacksmith shop) {
        this.shops.put(shop.getLabel(), shop);
        return this;
    }


    public HashMap<String, Location> getLocations() {
        return locations;
    }

    public HashMap<String, Blacksmith> getShops() {
        return shops;
    }
}
