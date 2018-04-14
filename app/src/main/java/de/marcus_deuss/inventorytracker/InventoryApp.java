package de.marcus_deuss.inventorytracker;

/*
 * Android Application class. Used for accessing singletons.
 *
 * add "android:name" attribute in your AndroidManifest.xml's <application> tag
 * then this class will be called before any activtity class
 */


import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import de.marcus_deuss.inventorytracker.db.dao.InventoryDAO;
import de.marcus_deuss.inventorytracker.db.dao.RoomDAO;
import de.marcus_deuss.inventorytracker.db.entity.Inventory;


public class InventoryApp extends Application {

    private static final String TAG = "InventoryTracker";

    // needed for translating string resources outside activities
    public static Resources resources;

    // chosen inventory
    public static Inventory myInventory = null;
    public static InventoryDAO myInventoryDAO = null;
    public static RoomDAO myRoomDAO = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App started!!!!!");

        // access resources from all over the app
        resources = getResources();


    }

}
