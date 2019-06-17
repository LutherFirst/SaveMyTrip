package com.alo.savemytrip;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.alo.savemytrip.database.SaveMyTripDatabase;
import com.alo.savemytrip.models.Item;
import com.alo.savemytrip.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ItemDaoTest {

    private SaveMyTripDatabase mDatabase;

    //DATA SET FOR TEST
    private static long USER_ID = 1;
    private static User USER_DEMO = new User(USER_ID, "Luther", "https://www.google.fr");

    private static Item NEW_ITEM_PLACE_TO_VISIT = new Item("Visite cet endroit de reve !", 0, USER_ID);
    private static Item NEW_ITEM_IDEA = new Item("On pourrait faire du chien de traineau ?", 1, USER_ID);
    private static Item NEW_ITEM_RESTAURANTS = new Item("Ce restaurant a l'air sympa", 2, USER_ID);

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                SaveMyTripDatabase.class).allowMainThreadQueries().build();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void insertAndGetUser() throws InterruptedException{
        // BEFORE : Adding a new user
        this.mDatabase.userDao().createUser(USER_DEMO);

        // TEST
        User user = LiveDataTestUtil.getValue(this.mDatabase.userDao().getUser(USER_ID));
        assertTrue(user.getUsername().equals(USER_DEMO.getUsername()) && user.getId() == USER_ID);
    }

    @Test
    public void getItemsWhenNoItemInserted() throws InterruptedException {
        List<Item> items = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItems(USER_ID));
        assertTrue(items.isEmpty());
    }

    @Test
    public void insertAndGetItems() throws InterruptedException {
        this.mDatabase.userDao().createUser(USER_DEMO);
        this.mDatabase.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        this.mDatabase.itemDao().insertItem(NEW_ITEM_IDEA);
        this.mDatabase.itemDao().insertItem(NEW_ITEM_RESTAURANTS);

        List<Item> items = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItems(USER_ID));
        assertTrue(items.size() == 3);
    }

    @Test
    public void insertAndUpdateItem() throws InterruptedException {
        this.mDatabase.userDao().createUser(USER_DEMO);
        this.mDatabase.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        Item itemAdded = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItems(USER_ID)).get(0);
        itemAdded.setSelected(true);
        this.mDatabase.itemDao().updateItem(itemAdded);

        List<Item> items = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItems(USER_ID));
        assertTrue(items.size() == 1 && items.get(0).getSelected());
    }

    @Test
    public void insertAndDeleteItem() throws  InterruptedException {
        this.mDatabase.userDao().createUser(USER_DEMO);
        this.mDatabase.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        Item itemAdded = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItems(USER_ID)).get(0);
        this.mDatabase.itemDao().deleteItem(itemAdded.getId());

        List<Item> items = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItems(USER_ID));
        assertTrue(items.isEmpty());
    }
}
