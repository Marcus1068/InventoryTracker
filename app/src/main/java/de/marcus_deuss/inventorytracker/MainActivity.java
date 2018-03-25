package de.marcus_deuss.inventorytracker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "InventoryTracker";

    private DatabaseHelper db;
    private SimpleCursorAdapter adapter;
    private Cursor cursor;
    private SimpleDateFormat dateFormat;

    private ListView overviewListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.toolTip, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // init the sqlite database
        db = new DatabaseHelper(this);

        // some sample data
        // TODO needs to be removed in final software
        db.generateSampleData();
        db.printDatabaseContents();

        cursor = db.createListViewCursor();

        overviewListView = (ListView) this.findViewById(R.id.listView1);

        String[] anzeigeSpalten = new String[]{ Inventory.COLUMN_INVENTORYNAME, Inventory.COLUMN_ROOMNAME, Inventory.COLUMN_TIMESTAMP};
        int[] anzeigeViews      = new int[]{ R.id.textViewInventoryName, R.id.textViewRoomName, R.id.textViewTimeStamp};
        adapter                 = new SimpleCursorAdapter(this, R.layout.listviewlayout, cursor,
                anzeigeSpalten, anzeigeViews,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER );

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == 3) {
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
                    catch(Exception ex) {}
                }

                return false; //keine Änderung
            }
        });


        overviewListView.setAdapter(adapter);

        // some sample data
        // TODO needs to be removed in final software
        db.generateSampleData();
        db.printDatabaseContents();
    }

    // close cursor and database
    @Override
    protected void onDestroy() {
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            // TODO die einzelnen Menüeinträge implementieren und die id Namen ändern
            sendEmail();
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // close navigation drawer after choosing menu entry
        drawer.closeDrawer(GravityCompat.START, true);

        return true;
    }

    // send an email and check for preconditions
    protected void sendEmail() {
        Log.i(TAG, "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i(TAG, "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, R.string.noEmailInstalled, Toast.LENGTH_SHORT).show();
        }
    }
}
