package tests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Direction;
import model.Item;
import model.RoomNode;

public class RoomNodeTest {

    RoomNode withNameDescription;
    
    RoomNode withItems;
    
    List<Item> items;
    
    @Before
    public void setUp() throws Exception {
        withNameDescription = new RoomNode(5, "name", "description");
        withItems = new RoomNode(1, "name");
        
        items = new LinkedList<>();
        items.add(new Item("item 1"));
        items.add(new Item("item 2"));
        items.add(new Item("item 3"));
        items.add(new Item("item 4"));
        items.add(new Item("item 5"));
        
        for (Item i : items) {
            withItems.addItem(i);
        }
        
    }

    @Test
    public void testRoomNodeIntStringString() {
        assertEquals(withNameDescription.getName(), "name");
        assertEquals(withNameDescription.getDescription(), "description");
        assertEquals(withNameDescription.getRoomID(), 5);
        assertEquals(withNameDescription.getItems().size(), 0);
        

        assertEquals(withNameDescription.getDirection(Direction.NORTH), null);
        assertEquals(withNameDescription.getDirection(Direction.EAST), null);
        assertEquals(withNameDescription.getDirection(Direction.SOUTH), null);
        assertEquals(withNameDescription.getDirection(Direction.WEST), null);
        assertEquals(withNameDescription.getDirection(Direction.UP), null);
        assertEquals(withNameDescription.getDirection(Direction.DOWN), null);
    }

    @Test(expected = NullPointerException.class)
    public void testRoomNodeIntStringString_NullName() {
        new RoomNode(0, "name", null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testRoomNodeIntStringString_NullDescription() {
        new RoomNode(0, null, "description");
    }

    @Test
    public void testSetName() {
        withNameDescription.setName("a different name");
        assertEquals(withNameDescription.getName(), "a different name");
    }

    @Test
    public void testAddItem_WithoutMethodCall_ItemIsntAdded() {
        List<Item> items = withNameDescription.getItems();
        
        assertEquals(items.size(), 0);
        
        items.add(new Item("name"));
        items = withNameDescription.getItems();
        assertEquals("Unsafe list of items returned. It was mutable.", items.size(), 0);
    }

    @Test
    public void testAddItem_WithMethodCall_ItemIsAdded() {
        List<Item> items = withNameDescription.getItems();
        
        assertEquals(items.size(), 0);
        
        withNameDescription.addItem(new Item("name"));
        items = withNameDescription.getItems();
        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getName(), "name");
    }

    @Test
    public void testAddItem_SameItemAgain_ItemIsNotAdded() {
        Item i = new Item("name");

        withNameDescription.addItem(i);
        withNameDescription.addItem(i);
        List<Item> items = withNameDescription.getItems();
        
        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getName(), "name");
    }
    
    @Test
    public void testAddItem_Null_ItemIsNotAdded() {
        withNameDescription.addItem(null);
        assertEquals(withNameDescription.getItems().size(), 0);
    }

    @Test
    public void testRemoveItem_ItemNotInRoom_NoChanges() {
        withItems.removeItem(new Item("sslfksfls"));
        List<Item> roomItems = withItems.getItems();
        assertEquals(roomItems.size(), items.size());
    }

    @Test
    public void testRemoveItem_ItemInRoom_ItemRemoved() {
        List<Item> roomItems = withItems.getItems();
        assertEquals(roomItems.size(), items.size());
        withItems.removeItem(items.get(2));

        roomItems = withItems.getItems();
        assertEquals(roomItems.size(), items.size() - 1);
        assertFalse(roomItems.contains(items.get(2)));
    }
    
    @Test
    public void testRemoveItem_Null_NoChanges() {
        withItems.removeItem(null);
        List<Item> roomItems = withItems.getItems();
        assertEquals(roomItems.size(), items.size());
    }
    
    @Test
    public void testSetDirection_ValidDirection_IsSet() {
        withNameDescription.setDirection(Direction.NORTH, new RoomNode(56, "a room name"));
        assertTrue(withNameDescription.getDirection(Direction.NORTH) != null);
    }
    
    @Test
    public void testSetDirection_NullDirection_NoChanges() {
        withNameDescription.setDirection(Direction.NORTH, new RoomNode(56, "a room name"));
        assertTrue(withNameDescription.getDirection(Direction.NORTH) != null);
        withNameDescription.setDirection(null, null);
        assertTrue(withNameDescription.getDirection(Direction.NORTH) != null);
    }
}