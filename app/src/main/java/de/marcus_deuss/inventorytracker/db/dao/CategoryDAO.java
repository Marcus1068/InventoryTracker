/*
 * This class extends InventoryDBDAO and implements database operations such as save, update, delete, get department.
 */

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
import de.marcus_deuss.inventorytracker.db.entity.Category;

import static de.marcus_deuss.inventorytracker.InventoryApp.resources;


public class CategoryDAO extends InventoryDBDAO{

    private static final String TAG = "InventoryTracker";

    private static final String WHERE_ID_EQUALS = DatabaseHelper.COLUMN_ID
            + " =?";

    public CategoryDAO(Context context) {
        super(context);
    }

    // insert new category
    public long saveCategory(Category category) {
        Log.d(TAG, "Insert category");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORYNAME, category.getCategoryName());

        return database.insert(DatabaseHelper.TABLE_NAME_CATEGORY, null, values);
    }


    // TODO beta not tested and buggy
    public long updateCategory(Category category) {
        Log.d(TAG, "Update category");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORYNAME, category.getCategoryName());

        // values.put(DatabaseHelper.COLUMN_ID, category.getDepartment().getId());

        long result = database.update(DatabaseHelper.TABLE_NAME_CATEGORY, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(category.getId()) });
        Log.d("Update Result:", "=" + result);

        return result;
    }

    // delete a category
    public int deleteCategory(Category category) {
        return database.delete(DatabaseHelper.TABLE_NAME_CATEGORY,
                WHERE_ID_EQUALS, new String[] { category.getId() + "" });
    }

    // Getting single category item
    public Category getCategory(long id) {
        Log.d(TAG, "getCategory");

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_CATEGORY,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_CATEGORYNAME},
                DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare inventory object
        Category category = new Category(
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORYNAME))
        );

        // close the db connection
        cursor.close();

        return category;
    }

    // Getting all category items, sorted by category name
    public List<Category> getCategoryList() {
        Log.d(TAG, "getCategoryList");

        List<Category> categoryList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_CATEGORY + " ORDER BY " +
                DatabaseHelper.COLUMN_CATEGORYNAME + DatabaseHelper.TABLE_SORT_ORDER;


        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category inv = new Category();
                inv.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                inv.setCategoryName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORYNAME)));

                // Adding category to list
                categoryList.add(inv);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return inventory list
        return categoryList;
    }

    // Getting category Count
    public int getCategoryCount() {
        Log.d(TAG, "getCategoryCount");

        String countQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_CATEGORY;

        Cursor cursor = database.rawQuery(countQuery, null);

        int cnt = cursor.getCount();

        cursor.close();

        // return count
        return cnt;
    }

    public Cursor createCategoryListViewCursor() {
        Log.d(TAG, "createCategoryListViewCursor() ...");

        String[] columns = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_CATEGORYNAME};
        return  database.query(DatabaseHelper.TABLE_NAME_CATEGORY, columns, null, null, null, null, DatabaseHelper.COLUMN_CATEGORYNAME);
    }

    // insert some category data into database as initial data
    public void generateCategoryData() {
        Log.d(TAG, "generateCategoryData");


        Category category = new Category((long) 1, resources.getString(R.string.furniture));
        this.saveCategory(category);

        Category category2 = new Category((long) 2, resources.getString(R.string.device));
        this.saveCategory(category2);

        Category category3 = new Category((long) 3, resources.getString(R.string.toys));
        this.saveCategory(category3);

        Category category4 = new Category((long) 4, resources.getString(R.string.juwelry));
        this.saveCategory(category4);

        Category category5 = new Category((long) 5, resources.getString(R.string.computer));
        this.saveCategory(category5);

        Category category6 = new Category((long) 6, resources.getString(R.string.mobile));
        this.saveCategory(category6);

    }
}
