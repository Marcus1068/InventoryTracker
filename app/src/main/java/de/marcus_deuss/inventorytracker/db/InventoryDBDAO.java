/*
 * To write to and read from the database we need a SQLiteDatabase object (that represents the database) which can be obtained
 * by calling getWritableDatabase() on the database helper. Then we will create DAO classes which will
 * extend this class and use SQLiteDatabase object provides methods for SQLite CRUD (Create, Read, Update, Delete) operations.

 */

package de.marcus_deuss.inventorytracker.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class InventoryDBDAO {

    protected SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;

    public InventoryDBDAO(Context context) {
        this.mContext = context;
        dbHelper = DatabaseHelper.getHelper(mContext);
        open();

    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = DatabaseHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        database = null;
    }
}
