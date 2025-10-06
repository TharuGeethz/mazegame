package mazegame.entity.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MiscellaneousItemTest {
    @Test
    void constructor_initializesAllFieldsCorrectly() {
        MiscellaneousItem item = new MiscellaneousItem("Banner", 1000, 5);
        assertEquals("Banner", item.getLabel());
        assertEquals(1000, item.getValue());
        assertEquals(5, item.getWeight());
    }

    @Test
    void constructor_allowsZeroValues() {
        MiscellaneousItem item = new MiscellaneousItem("Stone", 0, 0);
        assertEquals("Stone", item.getLabel());
        assertEquals(0, item.getValue());
        assertEquals(0, item.getWeight());
    }

    @Test
    void constructor_handlesLargeValues() {
        MiscellaneousItem item = new MiscellaneousItem("Golden Crown", 999999, 200);
        assertEquals("Golden Crown", item.getLabel());
        assertEquals(999999, item.getValue());
        assertEquals(200, item.getWeight());
    }

    @Test
    void getLabel_returnsCorrectLabel() {
        MiscellaneousItem item = new MiscellaneousItem("Key", 10, 1);
        assertEquals("Key", item.getLabel());
    }

    @Test
    void getValue_returnsCorrectValue() {
        MiscellaneousItem item = new MiscellaneousItem("Gem", 500, 2);
        assertEquals(500, item.getValue());
    }

    @Test
    void getWeight_returnsCorrectWeight() {
        MiscellaneousItem item = new MiscellaneousItem("Scroll", 25, 1);
        assertEquals(1, item.getWeight());
    }
}
