package de.marcus_deuss.inventorytracker.db.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import de.marcus_deuss.inventorytracker.db.entity.Category;

public class CategoryDAO {
    private static final String TAG = "InventoryTracker.CategoryDAO";

    // insert new category
    public long InsertCategory(Category category) {
        Log.d(TAG, "Insert category");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Category.COLUMN_CATEGORYNAME, category.getCategoryName());

        // Inserting Row
        long id = db.insert(Category.TABLE_NAME, null, values);

        return id;
    }
}
