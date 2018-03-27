package de.marcus_deuss.inventorytracker;

/**
 * Android Application class. Used for accessing singletons.
 *
 * add "android:name" attribute in your AndroidManifest.xml's <application> tag
 * then this class will be called before any activtity class
 */


import android.app.Application;
import android.util.Log;

public class InventoryApp extends Application {

    private static final String TAG = "InventoryTracker.App";

    // private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App started!!!!!");

        // mAppExecutors = new AppExecutors();
    }

/*        public AppDatabase getDatabase() {
            return AppDatabase.getInstance(this, mAppExecutors);
        }

        public DataRepository getRepository() {
            return DataRepository.getInstance(getDatabase());
        }
    }
*/
}
