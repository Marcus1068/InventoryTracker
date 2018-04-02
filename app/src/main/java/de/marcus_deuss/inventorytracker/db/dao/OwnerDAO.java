package de.marcus_deuss.inventorytracker.db.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.marcus_deuss.inventorytracker.R;
import de.marcus_deuss.inventorytracker.db.DatabaseHelper;
import de.marcus_deuss.inventorytracker.db.InventoryDBDAO;
import de.marcus_deuss.inventorytracker.db.entity.Owner;

import static de.marcus_deuss.inventorytracker.InventoryApp.resources;


public class OwnerDAO extends InventoryDBDAO{

    private static final String TAG = "InventoryTracker";

    private static final String WHERE_ID_EQUALS = DatabaseHelper.COLUMN_ID
            + " =?";

    public OwnerDAO(Context context) {
        super(context);
    }

    // insert new owner
    public long saveOwner(Owner owner) {
        Log.d(TAG, "Insert owner");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_OWNERNAME, owner.getOwnerName());

        return database.insert(DatabaseHelper.TABLE_NAME_OWNER, null, values);
    }


    // TODO beta not tested and buggy
    public long updateOwner(Owner owner) {
        Log.d(TAG, "Update owner");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_OWNERNAME, owner.getOwnerName());

        // values.put(DatabaseHelper.COLUMN_ID, category.getDepartment().getId());

        long result = database.update(DatabaseHelper.TABLE_NAME_OWNER, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(owner.getId()) });
        Log.d("Update Result:", "=" + result);

        return result;
    }

    // delete an owner
    public int deleteOwner(Owner owner) {
        return database.delete(DatabaseHelper.TABLE_NAME_OWNER,
                WHERE_ID_EQUALS, new String[] { owner.getId() + "" });
    }

    // Getting single owner item
    public Owner getOwner(long id) {
        Log.d(TAG, "getOwner");

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_OWNER,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_OWNERNAME},
                DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare category object
        Owner owner = new Owner(
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_OWNERNAME))
        );

        // close the db connection
        cursor.close();

        return owner;
    }

    // Getting all owner items, sorted by owner name
    public List<Owner> getOwnerList() {
        Log.d(TAG, "getOwnerList");

        List<Owner> ownerList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_OWNER + " ORDER BY " +
                DatabaseHelper.COLUMN_OWNERNAME + DatabaseHelper.TABLE_SORT_ORDER;


        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Owner owner = new Owner();
                owner.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                owner.setOwnerName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_OWNERNAME)));

                // Adding to list
                ownerList.add(owner);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return list
        return ownerList;
    }

    // Getting owner Count
    public int getOwnerCount() {
        Log.d(TAG, "getOwnerCount");

        String countQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_OWNER;

        Cursor cursor = database.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public Cursor createOwnerListViewCursor() {
        Log.d(TAG, "createOwnerListViewCursor() ...");

        String[] columns = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_OWNERNAME};
        return  database.query(DatabaseHelper.TABLE_NAME_OWNER, columns, null, null, null, null, DatabaseHelper.COLUMN_OWNERNAME);
    }

    // insert some owner data into database as initial data
    public void generateOwnerData() {
        Log.d(TAG, "generateOwnerData");


        Owner category = new Owner((long) 1, resources.getString(R.string.person1Owner));
        this.saveOwner(category);

        Owner category2 = new Owner((long) 2, resources.getString(R.string.person2Owner));
        this.saveOwner(category2);

        Owner category3 = new Owner((long) 3, resources.getString(R.string.person3Owner));
        this.saveOwner(category3);

        Owner category4 = new Owner((long) 4, resources.getString(R.string.person4Owner));
        this.saveOwner(category4);

        Owner category5 = new Owner((long) 5, resources.getString(R.string.unknownOwner));
        this.saveOwner(category5);

    }
}
