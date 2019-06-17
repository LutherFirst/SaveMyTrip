package com.alo.savemytrip.repositories;

import android.arch.lifecycle.LiveData;

import com.alo.savemytrip.database.dao.ItemDao;
import com.alo.savemytrip.models.Item;

import java.util.List;

public class ItemDataRepository {

    private final ItemDao itemDao;

    public ItemDataRepository(ItemDao itemDao) { this.itemDao = itemDao; }

    // --- GET ---

    public LiveData<List<Item>> getItems(long userId){ return this.itemDao.getItems(userId); }

    // --- CREATE ---

    public void createItem(Item item){ itemDao.insertItem(item); }

    // --- DELETE ---
    public void deleteItem(long itemId){ itemDao.deleteItem(itemId); }

    // --- UPDATE ---
    public void updateItem(Item item){ itemDao.updateItem(item); }

}
