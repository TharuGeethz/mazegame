package mazegame.entity;


import java.util.HashMap;

import mazegame.entity.item.Shield;

public class ShieldFactory {
	
	   private static ShieldFactory instance;
	   private HashMap<String, Shield> shields;
	
	   private ShieldFactory(HashMap<String, Shield> shields){
		   this.shields = shields;
	    }

	    public static ShieldFactory getInstance() {
	        if (instance == null) {
	            instance = new ShieldFactory(new HashMap<String, Shield>());
	        }
	        return instance;
	    }

	    public boolean addShield(Shield shiled) {
	    	shields.put(shiled.getLabel(), shiled);
	        return true;
	    }

	    public Shield getShield(String shieldLabel) {
	        return shields.get(shieldLabel);
	    }
	    
	    

	    public HashMap<String, Shield> getAllShileds() {
	        return shields;
	    }
	

}
