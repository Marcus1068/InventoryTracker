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
import de.marcus_deuss.inventorytracker.db.entity.Brand;

import static de.marcus_deuss.inventorytracker.InventoryApp.resources;


public class BrandDAO extends InventoryDBDAO{
    private static final String TAG = "InventoryTracker";

    private static final String WHERE_ID_EQUALS = DatabaseHelper.COLUMN_ID
            + " =?";

    public BrandDAO(Context context) {
        super(context);
    }

    // insert new brand
    public long saveBrand(Brand brand) {
        Log.d(TAG, "Insert brand");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BRANDNAME, brand.getBrandName());

        return database.insert(DatabaseHelper.TABLE_NAME_BRAND, null, values);
    }


    // TODO beta not tested and buggy
    public long updateBrand(Brand brand) {
        Log.d(TAG, "Update brand");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BRANDNAME, brand.getBrandName());

        // values.put(DatabaseHelper.COLUMN_ID, category.getDepartment().getId());

        long result = database.update(DatabaseHelper.TABLE_NAME_BRAND, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(brand.getId()) });
        Log.d("Update Result:", "=" + result);

        return result;
    }

    // delete a brand
    public int deleteBrand(Brand brand) {
        return database.delete(DatabaseHelper.TABLE_NAME_BRAND,
                WHERE_ID_EQUALS, new String[] { brand.getId() + "" });
    }

    // Getting single brand item
    public Brand getBrand(long id) {
        Log.d(TAG, "getBrand");

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_BRAND,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_BRANDNAME},
                DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare brand object
        Brand brand = new Brand(
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BRANDNAME))
        );

        // close the db connection
        cursor.close();

        return brand;
    }

    // Getting all brand items, sorted by brand name
    public List<Brand> getBrandList() {
        Log.d(TAG, "getBrandList");

        List<Brand> brandList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_BRAND + " ORDER BY " +
                DatabaseHelper.COLUMN_BRANDNAME + DatabaseHelper.TABLE_SORT_ORDER;


        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Brand brand = new Brand();
                brand.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                brand.setBrandName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BRANDNAME)));

                // Adding brand to list
                brandList.add(brand);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return list
        return brandList;
    }

    // Getting brand Count
    public int getBrandCount() {
        Log.d(TAG, "getBrandCount");

        String countQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_BRAND;

        Cursor cursor = database.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public Cursor createBrandListViewCursor() {
        Log.d(TAG, "createBrandListViewCursor() ...");

        String[] columns = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_BRANDNAME};
        return  database.query(DatabaseHelper.TABLE_NAME_BRAND, columns, null, null, null, null, DatabaseHelper.COLUMN_BRANDNAME);
    }

    // insert some brand data into database as initial data
    public void generateBrandData() {
        Log.d(TAG, "generateBrandData");


        Brand category = new Brand((long) 1, resources.getString(R.string.ikeaBrand));
        this.saveBrand(category);

        Brand category2 = new Brand((long) 2, resources.getString(R.string.appleBrand));
        this.saveBrand(category2);

        Brand category3 = new Brand((long) 3, resources.getString(R.string.samsungBrand));
        this.saveBrand(category3);

        Brand category4 = new Brand((long) 4, resources.getString(R.string.sonyBrand));
        this.saveBrand(category4);

        Brand category5 = new Brand((long) 5, resources.getString(R.string.weberBrand));
        this.saveBrand(category5);

        Brand category6 = new Brand((long) 6, resources.getString(R.string.unknownBrand));
        this.saveBrand(category6);
    }
}
