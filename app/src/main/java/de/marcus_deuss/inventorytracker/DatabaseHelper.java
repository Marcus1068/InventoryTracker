package de.marcus_deuss.inventorytracker;

/**
 * Created by marcus on 24.03.18.
 */

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "inventoryManager.db";

    private static final String TAG = "InventoryTracker";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Table
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(Inventory.CREATE_TABLE);
        }
        catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Inventory.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // closing db
    @Override
    public synchronized void close(){
        //
        super.close();
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new inventory to database
    // returns id of newly inserted row
    public long InsertInventory(Inventory inventory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Inventory.COLUMN_INVENTORYNAME, inventory.getInventoryName());
        values.put(Inventory.COLUMN_DATEOFPURCHASE, inventory.getDateOfPurchase());
        values.put(Inventory.COLUMN_PRICE, inventory.getPrice());
        values.put(Inventory.COLUMN_INVOICE, inventory.getInvoice());
        // timeStamp will be inserted automatically
        // values.put(Inventory.COLUMN_TIMESTAMP, inventory.getTimeStamp());
        values.put(Inventory.COLUMN_WARRANTY, inventory.getWarranty());
        values.put(Inventory.COLUMN_SERIALNUMBER, inventory.getSerialNumber());
        values.put(Inventory.COLUMN_IMAGE, inventory.getImage());
        values.put(Inventory.COLUMN_REMARK, inventory.getRemark());
        values.put(Inventory.COLUMN_OWNERNAME, inventory.getOwnerName());
        values.put(Inventory.COLUMN_CATEGORYNAME, inventory.getCategoryName());
        values.put(Inventory.COLUMN_BRANDNAME, inventory.getBrandName());
        values.put(Inventory.COLUMN_ROOMNAME, inventory.getRoomName());

        // Inserting Row
        long id = db.insert(Inventory.TABLE_NAME, null, values);

        db.close(); // Closing database connection

        return id;
    }

    // Getting single inventory item
    public Inventory getInventory(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Inventory.TABLE_NAME,
                new String[]{Inventory.COLUMN_ID, Inventory.COLUMN_INVENTORYNAME,
                        Inventory.COLUMN_DATEOFPURCHASE, Inventory.COLUMN_PRICE, Inventory.COLUMN_INVOICE,
                        Inventory.COLUMN_TIMESTAMP, Inventory.COLUMN_WARRANTY, Inventory.COLUMN_SERIALNUMBER,
                        Inventory.COLUMN_IMAGE, Inventory.COLUMN_REMARK, Inventory.COLUMN_OWNERNAME,
                        Inventory.COLUMN_CATEGORYNAME, Inventory.COLUMN_BRANDNAME, Inventory.COLUMN_ROOMNAME},
                Inventory.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare inventory object
        Inventory inv = new Inventory(
                cursor.getLong(cursor.getColumnIndex(Inventory.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_INVENTORYNAME)),
                cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_DATEOFPURCHASE)),
                cursor.getInt(cursor.getColumnIndex(Inventory.COLUMN_PRICE)),
                cursor.getBlob(cursor.getColumnIndex(Inventory.COLUMN_INVOICE)),
                cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_TIMESTAMP)),
                cursor.getInt(cursor.getColumnIndex(Inventory.COLUMN_WARRANTY)),
                cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_SERIALNUMBER)),
                cursor.getBlob(cursor.getColumnIndex(Inventory.COLUMN_IMAGE)),
                cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_REMARK)),
                cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_OWNERNAME)),
                cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_CATEGORYNAME)),
                cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_BRANDNAME)),
                cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_ROOMNAME))
        );

        // close the db connection
        cursor.close();

        return inv;
    }



    // Getting all Inventory items, sorted by inventory name first, then owner
    public List<Inventory> getInventoryList() {
        List<Inventory> inventoryList = new ArrayList<Inventory>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Inventory.TABLE_NAME + " ORDER BY " +
                Inventory.COLUMN_INVENTORYNAME + ", " + Inventory.COLUMN_OWNERNAME + " COLLATE NOCASE ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Inventory inv = new Inventory();
                inv.setId(cursor.getLong(cursor.getColumnIndex(Inventory.COLUMN_ID)));
                inv.setInventoryName(cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_INVENTORYNAME)));
                inv.setDateOfPurchase(cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_DATEOFPURCHASE)));
                inv.setPrice(cursor.getInt(cursor.getColumnIndex(Inventory.COLUMN_PRICE)));
                inv.setInvoice(cursor.getBlob(cursor.getColumnIndex(Inventory.COLUMN_INVOICE)));
                inv.setTimeStamp(cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_TIMESTAMP)));
                inv.setWarranty(cursor.getInt(cursor.getColumnIndex(Inventory.COLUMN_WARRANTY)));
                inv.setSerialNumber(cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_SERIALNUMBER)));
                inv.setImage(cursor.getBlob(cursor.getColumnIndex(Inventory.COLUMN_IMAGE)));
                inv.setRemark(cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_REMARK)));
                inv.setOwnerName(cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_OWNERNAME)));
                inv.setCategoryName(cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_CATEGORYNAME)));
                inv.setBrandName(cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_BRANDNAME)));
                inv.setRoomName(cursor.getString(cursor.getColumnIndex(Inventory.COLUMN_ROOMNAME)));

                // Adding inventory to list
                inventoryList.add(inv);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return inventory list
        return inventoryList;
    }



    // Updating single inventory item
    public long updateInventory(Inventory inv) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Inventory.COLUMN_INVENTORYNAME, inv.getInventoryName());
        values.put(Inventory.COLUMN_DATEOFPURCHASE, inv.getDateOfPurchase());
        values.put(Inventory.COLUMN_PRICE, inv.getPrice());
        values.put(Inventory.COLUMN_INVOICE, inv.getInvoice());
        values.put(Inventory.COLUMN_WARRANTY, inv.getWarranty());
        values.put(Inventory.COLUMN_SERIALNUMBER, inv.getSerialNumber());
        values.put(Inventory.COLUMN_IMAGE, inv.getImage());
        values.put(Inventory.COLUMN_REMARK, inv.getRemark());
        values.put(Inventory.COLUMN_OWNERNAME, inv.getOwnerName());
        values.put(Inventory.COLUMN_CATEGORYNAME, inv.getCategoryName());
        values.put(Inventory.COLUMN_BRANDNAME, inv.getBrandName());
        values.put(Inventory.COLUMN_ROOMNAME, inv.getRoomName());

        // updating row
        return db.update(Inventory.TABLE_NAME, values, Inventory.COLUMN_ID + " = ?",
                new String[] { String.valueOf(inv.getId()) });
    }


    // Deleting single inventory item
    public void deleteInventory(Inventory inv) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Inventory.TABLE_NAME, Inventory.COLUMN_ID + " = ?",
                new String[] { String.valueOf(inv.getId()) });
        db.close();
    }


    // Getting inventory Count
    public int getInventoryCount() {
        String countQuery = "SELECT * FROM " + Inventory.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int cnt = cursor.getCount();

        cursor.close();
        db.close();

        // return count
        return cnt;
    }


    // insert some data into database as initial data
    public void generateSampleData() {

        Inventory inv = new Inventory((long) 0, "Esstisch", "12-10-2017", 399, null, "", 18, "nicht definiert", null, "keine Infos", "Marcus", "Möbel", "IKEA", "Wohnzimmer" );
        this.InsertInventory(inv);

        Inventory inv2 = new Inventory((long) 0, "Macbook Pro 13", "12-10-2016", 2399, null, "", 24, "nicht definiert", null, "keine Infos", "Marcus", "Computer", "Apple", "Wohnzimmer" );
        this.InsertInventory(inv2);

        Inventory inv3 = new Inventory((long) 0, "Thermomix", "12-09-2016", 1099, null,  "", 24, "nicht definiert", null, "keine Infos", "Sandra", "Küchengerät", "Thermomix", "Küche" );
        this.InsertInventory(inv3);

        Inventory inv4 = new Inventory((long) 0, "Fritzbox 7590", "10-09-2017", 329, null,  "", 12, "nicht definiert", null, "keine Infos", "Marcus", "Technik", "AVM", "Wohnzimmer" );
        this.InsertInventory(inv4);

        Inventory inv5 = new Inventory((long) 0, "FritzWLAN 1730", "12-09-2016", 99, null,  "", 24, "nicht definiert", null, "keine Infos", "Marcus", "Technik", "AVM", "Büro" );
        this.InsertInventory(inv5);
    }

    // for debug purposes to get a dump of complete contents
    public void printDatabaseContents(){

        Log.d(TAG, "Number of rows ...");
        int count = this.getInventoryCount();
        String cnt = String.valueOf(count);
        Log.d(TAG, cnt );

        // print all rows
        List<Inventory> inventoryList = new ArrayList<Inventory>();
        for (Inventory inventory : inventoryList = getInventoryList()) {
            Log.d(TAG, "NAME - " + inventory.getInventoryName() +
                    " OWNER - " + inventory.getOwnerName() +
                    " ID - " + String.valueOf(inventory.getId()) +
                    " TIMEST. - " + inventory.getTimeStamp() +
                    " BRAND - " + inventory.getBrandName() +
                    " CAT - " + inventory.getCategoryName() +
                    "DATEOFP - " + inventory.getDateOfPurchase() +
                    " ROOM - " + inventory.getRoomName());
        }


    }

}


