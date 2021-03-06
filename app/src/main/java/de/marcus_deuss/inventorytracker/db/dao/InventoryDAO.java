package de.marcus_deuss.inventorytracker.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.marcus_deuss.inventorytracker.InventoryApp;
import de.marcus_deuss.inventorytracker.R;
import de.marcus_deuss.inventorytracker.db.DatabaseHelper;
import de.marcus_deuss.inventorytracker.db.InventoryDBDAO;
import de.marcus_deuss.inventorytracker.db.entity.Inventory;
import static de.marcus_deuss.inventorytracker.InventoryApp.resources;

public class InventoryDAO extends InventoryDBDAO{

    private static final String TAG = "InventoryTracker";

    public InventoryDAO(Context context) {
        super(context);
    }

    /*
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


        // reference to foreign keys
        values.put(DatabaseHelper.COLUMN_INVENTORY_OWNER_ID, inventory.getOwnerId());
        values.put(DatabaseHelper.COLUMN_INVENTORY_BRAND_ID, inventory.getBrandId());
        values.put(DatabaseHelper.COLUMN_INVENTORY_CATEGORY_ID, inventory.getCategoryId());    // reference foreign key
        values.put(DatabaseHelper.COLUMN_INVENTORY_ROOM_ID, inventory.getRoomId());
        return database.insert(DatabaseHelper.TABLE_NAME_INVENTORY, null, values);
    }



    // Getting single inventory item
    public Inventory getInventory(long id) {
        Log.d(TAG, "getInventory");

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_INVENTORY,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_INVENTORYNAME,
                        DatabaseHelper.COLUMN_DATEOFPURCHASE, DatabaseHelper.COLUMN_PRICE, DatabaseHelper.COLUMN_INVOICE,
                        DatabaseHelper.COLUMN_TIMESTAMP, DatabaseHelper.COLUMN_WARRANTY, DatabaseHelper.COLUMN_SERIALNUMBER,
                        DatabaseHelper.COLUMN_IMAGE, DatabaseHelper.COLUMN_REMARK,
                        DatabaseHelper.COLUMN_INVENTORY_CATEGORY_ID, DatabaseHelper.COLUMN_INVENTORY_ROOM_ID,
                        DatabaseHelper.COLUMN_INVENTORY_BRAND_ID, DatabaseHelper.COLUMN_INVENTORY_OWNER_ID},
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
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVENTORY_CATEGORY_ID)),
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVENTORY_ROOM_ID)),
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVENTORY_OWNER_ID)),
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVENTORY_BRAND_ID))
        );

        // close the db connection
        cursor.close();

        return inv;
    }



    // Getting all Inventory items, sorted by inventory name
    public List<Inventory> getInventoryList() {
        Log.d(TAG, "getInventoryList");

        List<Inventory> inventoryList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_INVENTORY + " ORDER BY " +
                DatabaseHelper.COLUMN_INVENTORYNAME + DatabaseHelper.TABLE_SORT_ORDER;


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

                inv.setOwnerId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVENTORY_OWNER_ID)));
                inv.setBrandId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVENTORY_BRAND_ID)));
                inv.setCategoryId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVENTORY_CATEGORY_ID)));
                inv.setRoomId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_INVENTORY_ROOM_ID)));

                // Adding inventory to list
                inventoryList.add(inv);
            } while (cursor.moveToNext());
        }

        cursor.close();

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

        values.put(DatabaseHelper.COLUMN_INVENTORY_OWNER_ID, inv.getOwnerId());
        values.put(DatabaseHelper.COLUMN_INVENTORY_BRAND_ID, inv.getBrandId());
        values.put(DatabaseHelper.COLUMN_INVENTORY_CATEGORY_ID, inv.getCategoryId());
        values.put(DatabaseHelper.COLUMN_INVENTORY_ROOM_ID, inv.getRoomId());

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

        Cursor cursor = database.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


    /*
     * Liefert Cursor zum Zugriff auf alle Einträge, alphabetisch geordnet nach Spalte Inventory.COLUMN_INVENTORYNAME
     *
     */
    public Cursor createInventoryListViewCursor() {
        Log.d(TAG, "createInventoryListViewCursor() ...");

        String[] columns = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_INVENTORYNAME, DatabaseHelper.COLUMN_TIMESTAMP};
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
        Log.d(TAG, "generateInventoryData");

        // TODO muss noch überarbeitet werden
        Drawable image = resources.getDrawable(R.drawable.ic_cloudfolder,null);

        byte[] myImage = convertImageToByte(image);

        Inventory inv1 = new Inventory((long) 0, "Esstisch", "12-10-2017", 399, null, "",
                18, "nicht definiert", myImage, "keine Infos", 1, 1, 5, 1);
        this.saveInventory(inv1);

        Inventory inv2 = new Inventory((long) 0, "Macbook Pro 13", "12-10-2016", 2399, null, "",
                24, "nicht definiert", myImage, "keine Infos", 1, 2, 5, 5 );
        this.saveInventory(inv2);

        Inventory inv3 = new Inventory((long) 0, "Thermomix", "12-09-2016", 1099, null,  "",
                24, "nicht definiert", myImage, "keine Infos", 3, 2, 1, 2 );
        this.saveInventory(inv3);

        Inventory inv4 = new Inventory((long) 0, "Fritzbox 7590", "10-09-2017", 329, null,  "",
                12, "nicht definiert", myImage, "keine Infos", 1, 2, 5, 2 );
        this.saveInventory(inv4);

        Inventory inv5 = new Inventory((long) 0, "FritzWLAN 1730", "12-09-2016", 99, null,  "",
                24, "nicht definiert", myImage, "keine Infos", 2, 2, 2, 2);
        this.saveInventory(inv5);
    }

    public byte[] convertImageToByte(Drawable myPhoto){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) myPhoto).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] photo = baos.toByteArray();

        return photo;
    }


    public Bitmap convertByteToImage(byte[] image){
        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        Bitmap theImage= BitmapFactory.decodeStream(imageStream);

        return theImage;
    }
}
