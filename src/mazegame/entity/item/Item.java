package mazegame.entity.item;

public abstract class Item {

  
    private int value;
    private int weight;
    private String label;
    private String description;
    
    public Item() {

    } 
    
    public Item(int value) {
        this.value = value; 
    } 
    
    public Item(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public Item (String label, int value, int weight)
    {
        this.label = label;
        this.value = value;
        this.weight = weight;
    }
    
    public String getLabel() {

        return label;
    }

    public int getValue()
    {
        return value;
    }

    public int getWeight()
    {
        return weight;
    }

    public String getDescription()
    {
        return description;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
