package de.marcus_deuss.inventorytracker.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.marcus_deuss.inventorytracker.InventoryApp;
import de.marcus_deuss.inventorytracker.R;
import de.marcus_deuss.inventorytracker.db.DatabaseHelper;
import de.marcus_deuss.inventorytracker.db.dao.BrandDAO;
import de.marcus_deuss.inventorytracker.db.dao.CategoryDAO;
import de.marcus_deuss.inventorytracker.db.dao.InventoryDAO;
import de.marcus_deuss.inventorytracker.db.dao.OwnerDAO;
import de.marcus_deuss.inventorytracker.db.dao.RoomDAO;
import de.marcus_deuss.inventorytracker.db.entity.Brand;
import de.marcus_deuss.inventorytracker.db.entity.Category;
import de.marcus_deuss.inventorytracker.db.entity.Inventory;
import de.marcus_deuss.inventorytracker.db.entity.Owner;
import de.marcus_deuss.inventorytracker.db.entity.Room;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_PERMISSION_WRITE = 1001;   // any number

    private AddInventoryFragment addInventoryFragment;

    private static final String TAG = "InventoryTracker";

    private DatabaseHelper db;
    private SimpleCursorAdapter adapter;
    private Cursor cursor;
    private SimpleDateFormat dateFormat;
    private ListView overviewListView;

    // entity objects
    private InventoryDAO inventoryDAO;
    private CategoryDAO categoryDAO;
    private RoomDAO roomDAO;
    private OwnerDAO ownerDAO;
    private BrandDAO brandDAO;

    private boolean permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
/*        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.toolTip, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // init database elements, starting with foreign key tables like category and room table
        brandDAO = new BrandDAO(this);
        ownerDAO = new OwnerDAO(this);
        roomDAO = new RoomDAO(this);
        categoryDAO = new CategoryDAO(this);

        inventoryDAO = new InventoryDAO(this);

        InventoryApp.myInventoryDAO = inventoryDAO;
        InventoryApp.myRoomDAO = roomDAO;

        // generate initial data into database only if no data available
        if(inventoryDAO.getInventoryCount() == 0){
            ownerDAO.generateOwnerData();
            brandDAO.generateBrandData();
            roomDAO.generateRoomData();
            categoryDAO.generateCategoryData();
            inventoryDAO.generateInventoryData();
        }

        // TODO need to remove for final software
        for (Owner owner : ownerDAO.getOwnerList()) {
            Log.d(TAG, "owner id: " + owner.getId() + ", name: " + owner.getOwnerName());
        }

        for (Brand brand : brandDAO.getBrandList()) {
            Log.d(TAG, "brand id: " + brand.getId() + ", name: " + brand.getBrandName());
        }

        for (Room room : roomDAO.getRoomList()) {
            Log.d(TAG, "room id: " + room.getId() + ", name: " + room.getRoomName());
        }

        for (Category category : categoryDAO.getCategoryList()) {
            Log.d(TAG, "cat id: " + category.getId() + ", name: " + category.getCategoryName());
        }

        for (Inventory inventory : inventoryDAO.getInventoryList()) {
            Log.d(TAG, "inv id:" + inventory.getId() + ", name: " + inventory.getInventoryName() + ", cat: "
                    + inventory.getCategoryId() + ", room: " + inventory.getRoomId() + ", brand: " + inventory.getBrandId()
            + ", owner: " + inventory.getOwnerId());
        }


        cursor = inventoryDAO.createInventoryListViewCursor();

        overviewListView = this.findViewById(R.id.listView1);

        String[] anzeigeSpalten = new String[]{ DatabaseHelper.COLUMN_INVENTORYNAME, DatabaseHelper.COLUMN_TIMESTAMP};
        int[] anzeigeViews      = new int[]{ R.id.textViewInventoryName, R.id.textViewTimeStamp};
        adapter                 = new SimpleCursorAdapter(this, R.layout.listviewlayout, cursor, anzeigeSpalten, anzeigeViews,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER );

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == 2) {
                    // timeStamp umformatieren
                    try {
                        long datum = cursor.getLong(columnIndex);

                        if(datum != 0) {
                            String str = dateFormat.format(new Date(datum));
                            TextView anzeige = (TextView) view;
                            anzeige.setText(str);
                            return true;
                        }
                    }
                    catch(Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }

                return false; //keine Änderung
            }
        });


        overviewListView.setAdapter(adapter);

        // TODO bug when list is empty
        overviewListView.setSelection(0);
        overviewListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        // TODO must be changed, will crash when list is empty or first id is not 0
        InventoryApp.myInventory = inventoryDAO.getInventory(1);
        //overviewListView.getId()



        // ListView Item Click Listener
        overviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, "setOnItemClickListener");
                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                //String  itemValue    = (String) overviewListView.getItemAtPosition(position);


                InventoryApp.myInventory = inventoryDAO.getInventory(id);

                // Show Alert
                String toast = "Pos: " + String.valueOf(itemPosition) + " inv: " + InventoryApp.myInventory.getInventoryName();
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT)
                        .show();

            }

        });

        // Android permission check for external storage (save files to file system) and camera
        // will be called asynchronously and has to be called at the end of OnCreate
        if(!permissionGranted){
            checkPermissions();

            // return;

        }
    }


    // close cursor and database
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if(db != null) {
            db.close();
        }

        super.onDestroy();
        Log.d(TAG, "onDestroy() succeeded");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // TODO need to be changed
            //Shows an alert dialog on quit
            onShowQuitDialog();

            // super.onBackPressed();


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(TAG, "settings calling...");
            // settings menu selected
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d(TAG, "onNavigationItemSelected");

        int id = item.getItemId();

        if (id == R.id.edit_inventory) {
            Intent intent = new Intent(this, AddInventoryActivity.class);
            intent.putExtra("updateMode", InventoryApp.EDIT_INVENTORY);
            startActivity(intent);

        } else if (id == R.id.add_inventory) {
            Intent intent = new Intent(this, AddInventoryActivity.class);
            intent.putExtra("updateMode", InventoryApp.ADD_INVENTORY);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, R.string.notImplementedYet, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, R.string.notImplementedYet, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, R.string.notImplementedYet, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            String locale = Locale.getDefault().getLanguage();

            Toast.makeText(this, "System language:" + locale, Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // close navigation drawer after choosing menu entry
        drawer.closeDrawer(GravityCompat.START, true);

        return true;
    }

    public void onShowQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        builder.setMessage(R.string.doyouwanttoquit);
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.setNegativeButton(android.R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    // send an email and check for preconditions
    protected void sendEmail(String sendTo, String subject, String text) {
        Log.d(TAG, "sendEmail");

        String[] TO = {sendTo};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        try {
            startActivity(Intent.createChooser(emailIntent, "Sending mail..."));
            finish();
            Log.d(TAG, "Email sent...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, R.string.noEmailInstalled, Toast.LENGTH_SHORT).show();
        }
    }

    // Checks if external storage is available for read and write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // Initiate request for permissions.
    private boolean checkPermissions() {

        if (!isExternalStorageWritable()) {
            Toast.makeText(this, R.string.worksonlyonexternalstorage,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        } else {
            return true;
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "External storage permission granted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
