/*
 * This class extends InventoryDBDAO and implements database operations such as save, update, delete, get department.
 */

package de.marcus_deuss.inventorytracker.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import de.marcus_deuss.inventorytracker.db.DatabaseHelper;
import de.marcus_deuss.inventorytracker.db.InventoryDBDAO;
import de.marcus_deuss.inventorytracker.db.entity.Category;

public class CategoryDAO extends InventoryDBDAO{

    private static final String TAG = "InventoryTracker.CategoryDAO";

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
}
