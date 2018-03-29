package de.marcus_deuss.inventorytracker.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.marcus_deuss.inventorytracker.db.DatabaseHelper;
import de.marcus_deuss.inventorytracker.db.InventoryDBDAO;
import de.marcus_deuss.inventorytracker.db.entity.Inventory;


public class InventoryDAO extends InventoryDBDAO{

    private static final String TAG = "InventoryTracker.InventoryDAO";

    public InventoryDAO(Context context) {
        super(context);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new inventory to database
    // returns id of newly inserted row
    public long saveInventory(Inventory inventory) {
        Log.d(TAG, "save Inventory");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_INVENTORYNAME, inventory.getInventoryName());
        values.put(DatabaseHelper.COLUMN_DATEOFPURCHASE, inventory.getDateOfPurchase());
        values.put(DatabaseHelper.COLUMN_PRICE, inventory.getPrice());
        values.put(DatabaseHelper.COLUMN_INVOICE, inventory.getInvoice());
        // timeStamp will be inserted automatically
        // values.put(Inventory.COLUMN_TIMESTAMP, inventory.getTimeStamp());
        values.put(DatabaseHelper.COLUMN_WARRANTY, inventory.getWarranty());
        values.put(DatabaseHelper.COLUMN_SERIALNUMBER, inventory.getSerialNumber());
        values.put(DatabaseHelper.COLUMN_IMAGE, inventory.getImage());
        values.put(DatabaseHelper.COLUMN_REMARK, inventory.getRemark());
        values.put(DatabaseHelper.COLUMN_OWNERNAME, inventory.getOwnerName());
        values.put(DatabaseHelper.COLUMN_CATEGORYNAME, inventory.getCategoryName());
        values.put(DatabaseHelper.COLUMN_BRANDNAME, inventory.getBrandName());
        values.put(DatabaseHelper.COLUMN_ROOMNAME, inventory.getRoomName());

        return database.insert(DatabaseHelper.TABLE_NAME_INVENTORY, null, values);
    }



    // Getting single inventory item
    public Inventory getInventory(long id) {
        Log.d(TAG, "getInventory");

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_INVENTORY,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_INVENTORYNAME,
                        DatabaseHelper.COLUMN_DATEOFPURCHASE, DatabaseHelper.COLUMN_PRICE, DatabaseHelper.COLUMN_INVOICE,
                        DatabaseHelper.COLUMN_TIMESTAMP, DatabaseHelper.COLUMN_WARRANTY, DatabaseHelper.COLUMN_SERIALNUMBER,
                        DatabaseHelper.COLUMN_IMAGE, DatabaseHelper.COLUMN_REMARK, DatabaseHelper.COLUMN_OWNERNAME,
                        DatabaseHelper.COLUMN_CATEGORYNAME, DatabaseHelper.COLUMN_BRANDNAME, DatabaseHelper.COLUMN_ROOMNAME},
                DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare inventory object
        Inventory inv = new Inventory(
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVENTORYNAME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATEOFPURCHASE)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE)),
                cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVOICE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WARRANTY)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SERIALNUMBER)),
                cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_REMARK)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_OWNERNAME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORYNAME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BRANDNAME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ROOMNAME))
        );

        // close the db connection
        cursor.close();

        return inv;
    }



    // Getting all Inventory items, sorted by inventory name first, then owner
    public List<Inventory> getInventoryList() {
        Log.d(TAG, "getInventoryList");

        List<Inventory> inventoryList = new ArrayList<Inventory>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_INVENTORY + " ORDER BY " +
                DatabaseHelper.COLUMN_INVENTORYNAME + ", " + DatabaseHelper.COLUMN_OWNERNAME + " COLLATE NOCASE ASC";


        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Inventory inv = new Inventory();
                inv.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                inv.setInventoryName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVENTORYNAME)));
                inv.setDateOfPurchase(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATEOFPURCHASE)));
                inv.setPrice(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE)));
                inv.setInvoice(cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVOICE)));
                inv.setTimeStamp(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP)));
                inv.setWarranty(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WARRANTY)));
                inv.setSerialNumber(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SERIALNUMBER)));
                inv.setImage(cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE)));
                inv.setRemark(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_REMARK)));
                inv.setOwnerName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_OWNERNAME)));
                inv.setCategoryName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORYNAME)));
                inv.setBrandName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BRANDNAME)));
                inv.setRoomName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ROOMNAME)));

                // Adding inventory to list
                inventoryList.add(inv);
            } while (cursor.moveToNext());
        }

        // return inventory list
        return inventoryList;
    }

    // Updating single inventory item
    public long updateInventory(Inventory inv) {
        Log.d(TAG, "updateInventory");

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_INVENTORYNAME, inv.getInventoryName());
        values.put(DatabaseHelper.COLUMN_DATEOFPURCHASE, inv.getDateOfPurchase());
        values.put(DatabaseHelper.COLUMN_PRICE, inv.getPrice());
        values.put(DatabaseHelper.COLUMN_INVOICE, inv.getInvoice());
        values.put(DatabaseHelper.COLUMN_WARRANTY, inv.getWarranty());
        values.put(DatabaseHelper.COLUMN_SERIALNUMBER, inv.getSerialNumber());
        values.put(DatabaseHelper.COLUMN_IMAGE, inv.getImage());
        values.put(DatabaseHelper.COLUMN_REMARK, inv.getRemark());
        values.put(DatabaseHelper.COLUMN_OWNERNAME, inv.getOwnerName());
        values.put(DatabaseHelper.COLUMN_CATEGORYNAME, inv.getCategoryName());
        values.put(DatabaseHelper.COLUMN_BRANDNAME, inv.getBrandName());
        values.put(DatabaseHelper.COLUMN_ROOMNAME, inv.getRoomName());

        // updating row
        return database.update(DatabaseHelper.TABLE_NAME_INVENTORY, values, DatabaseHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(inv.getId()) });
    }


    // Deleting single inventory item
    public void deleteInventory(Inventory inv) {
        Log.d(TAG, "deleteInventory");

        //DatabaseHelper db = DatabaseHelper.getWritableDatabase();
        database.delete(DatabaseHelper.TABLE_NAME_INVENTORY, DatabaseHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(inv.getId()) });

    }


    // Getting inventory Count
    public int getInventoryCount() {
        Log.d(TAG, "getInventoryCount");

        String countQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_INVENTORY;
        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);

        int cnt = cursor.getCount();

        cursor.close();

        // return count
        return cnt;
    }


    /**
     * Liefert Cursor zum Zugriff auf alle Einträge, alphabetisch geordnet nach Spalte Inventory.COLUMN_INVENTORYNAME
     * @return
     */
    public Cursor createListViewCursor() {
        Log.d(TAG, "createListViewCursor() ...");

        String[] columns = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_INVENTORYNAME, DatabaseHelper.COLUMN_ROOMNAME, DatabaseHelper.COLUMN_TIMESTAMP};
        return  database.query(DatabaseHelper.TABLE_NAME_INVENTORY, columns, null, null, null, null, DatabaseHelper.COLUMN_INVENTORYNAME);
    }

/*
    public void loadInventories() {
        Log.d(TAG, "loadInventories");

        Inventory department = new Inventory((long) 0, "Esstisch", "12-10-2017", 399, null, "", 18, "nicht definiert", null, "keine Infos", "Marcus", "Möbel", "IKEA", "Wohnzimmer" );
        Inventory department1 = new Inventory((long) 1, "Macbook Pro 13", "12-10-2016", 2399, null, "", 24, "nicht definiert", null, "keine Infos", "Marcus", "Computer", "Apple", "Wohnzimmer" );
        Inventory department2 = new Inventory();
        Inventory department3 = new Inventory();
        Inventory department4 = new Inventory();
        Inventory department5 = new Inventory();

        List<Inventory> departments = new ArrayList<Inventory>();
        departments.add(department);
        departments.add(department1);
        departments.add(department2);
        departments.add(department3);
        departments.add(department4);
        departments.add(department5);
        for (Inventory inv : departments) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.TABLE_NAME_INVENTORY, inv.getName());
            database.insert(DatabaseHelper.TABLE_NAME_INVENTORY, null, values);
        }
    }
*/
    // insert some inventory data into database as initial data
    public void generateInventoryData() {
        Log.d(TAG, "generateSampleData");

        Inventory inv = new Inventory((long) 0, "Esstisch", "12-10-2017", 399, null, "", 18, "nicht definiert", null, "keine Infos", "Marcus", "Möbel", "IKEA", "Wohnzimmer" );
        this.saveInventory(inv);

        Inventory inv2 = new Inventory((long) 0, "Macbook Pro 13", "12-10-2016", 2399, null, "", 24, "nicht definiert", null, "keine Infos", "Marcus", "Computer", "Apple", "Wohnzimmer" );
        this.saveInventory(inv2);

        Inventory inv3 = new Inventory((long) 0, "Thermomix", "12-09-2016", 1099, null,  "", 24, "nicht definiert", null, "keine Infos", "Sandra", "Küchengerät", "Thermomix", "Küche" );
        this.saveInventory(inv3);

        Inventory inv4 = new Inventory((long) 0, "Fritzbox 7590", "10-09-2017", 329, null,  "", 12, "nicht definiert", null, "keine Infos", "Marcus", "Technik", "AVM", "Wohnzimmer" );
        this.saveInventory(inv4);

        Inventory inv5 = new Inventory((long) 0, "FritzWLAN 1730", "12-09-2016", 99, null,  "", 24, "nicht definiert", null, "keine Infos", "Marcus", "Technik", "AVM", "Büro" );
        this.saveInventory(inv5);
    }
}
