package de.marcus_deuss.inventorytracker.db;

/*
 * Created by marcus on 24.03.18.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "inventoryManager.db";

    // inventory table name
    public static final String TABLE_NAME_INVENTORY = "inventory";

    // category table name
    public static final String TABLE_NAME_CATEGORY = "category";

    // room table name
    public static final String TABLE_NAME_ROOM = "room";

    // sort order string
    public static final String TABLE_SORT_ORDER = " COLLATE NOCASE ASC";

    //  inventory Table Column names
    public static final String COLUMN_ID = "_id";    // should always be id, needed by some android classes
    public static final String COLUMN_INVENTORYNAME = "inventoryname";
    public static final String COLUMN_DATEOFPURCHASE = "dateofpurchase";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_INVOICE = "invoice";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_WARRANTY = "warranty";
    public static final String COLUMN_SERIALNUMBER = "serialnumber";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_REMARK = "remark";
    public static final String COLUMN_OWNERNAME = "ownername";
    public static final String COLUMN_CATEGORYNAME = "categoryname";
    public static final String COLUMN_BRANDNAME = "brandname";
    public static final String COLUMN_ROOMNAME = "roomname";
    public static final String COLUMN_INVENTORY_CATEGORY_ID = "category_id";
    public static final String COLUMN_INVENTORY_ROOM_ID = "room_id";


    // define create statements for all tables

    // Create category table SQL query string
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_NAME_CATEGORY + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATEGORYNAME + " TEXT" + ")";

    // Create room table SQL query string
    private static final String CREATE_TABLE_ROOM = "CREATE TABLE " + TABLE_NAME_ROOM + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ROOMNAME + " TEXT" + ")";

    // Create table SQL query string
    private static final String CREATE_TABLE_INVENTORY = "CREATE TABLE " + TABLE_NAME_INVENTORY + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_INVENTORYNAME + " TEXT,"
            + COLUMN_DATEOFPURCHASE  + " DATE," + COLUMN_PRICE + " INTEGER,"
            + COLUMN_INVOICE + " BLOB," + COLUMN_TIMESTAMP + " DATE DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_WARRANTY + " INTEGER," + COLUMN_SERIALNUMBER + " TEXT,"
            + COLUMN_IMAGE + " BLOB," + COLUMN_REMARK + " TEXT,"
            + COLUMN_OWNERNAME + " TEXT,"
            + COLUMN_BRANDNAME + " TEXT,"
            + COLUMN_INVENTORY_ROOM_ID + " INTEGER," + COLUMN_INVENTORY_CATEGORY_ID + " INTEGER,"
            + " FOREIGN KEY(" + COLUMN_INVENTORY_CATEGORY_ID + ") REFERENCES " + TABLE_NAME_CATEGORY + "(" + COLUMN_ID + "), "
            + " FOREIGN KEY(" + COLUMN_INVENTORY_ROOM_ID + ") REFERENCES " + TABLE_NAME_ROOM + "(" + COLUMN_ID +")) ";


    private static final String TAG = "InventoryTracker";

    // private SQLiteDatabase db;

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // TODO evtl. uncomment
        // db = this.getWritableDatabase();
    }


    // Creating Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "database onCreate");

        try {
            db.execSQL(CREATE_TABLE_ROOM);
            db.execSQL(CREATE_TABLE_CATEGORY);
            db.execSQL(CREATE_TABLE_INVENTORY);
            Log.d(TAG, "created tables ...");
        }
        catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d(TAG, "database onOpen");

        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "database onUpgrade");

        // Drop older table if existed
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INVENTORY);

            // last tables to delete are foreign key tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORY);

            // last tables to delete are foreign key tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ROOM);

            // Create tables again
            onCreate(db);

            Log.d(TAG, "upgraded tables ...");
        }
        catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }
}