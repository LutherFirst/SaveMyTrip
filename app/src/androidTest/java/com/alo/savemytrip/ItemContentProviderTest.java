package com.alo.savemytrip;

import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.alo.savemytrip.database.SaveMyTripDatabase;
import com.alo.savemytrip.provider.ItemContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;


@RunWith(AndroidJUnit4.class)
public class ItemContentProviderTest {

    private ContentResolver mContentResolver;

    private static  long USER_ID = 1;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                SaveMyTripDatabase.class).allowMainThreadQueries().build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Test
    public void getItemsWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(ItemContentProvider.URI_ITEM, USER_ID), null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void  insertAndGetItem(){
        final Uri userUri = mContentResolver.insert(ItemContentProvider.URI_ITEM, generateItem());
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(ItemContentProvider.URI_ITEM, USER_ID), null, null,null,null);
        assertThat(cursor,notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("text")), is("Cisite cet endroit de reve"));
    }

    private ContentValues generateItem(){
        final ContentValues values = new ContentValues();
        values.put("text", "Visite cet endroit de reve !");
        values.put("category", "0");
        values.put("isSelected", "false");
        values.put("userId", "1");
        return values;
    }
}
