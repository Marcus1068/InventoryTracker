package de.marcus_deuss.inventorytracker.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.marcus_deuss.inventorytracker.R;
import de.marcus_deuss.inventorytracker.db.dao.CategoryDAO;
import de.marcus_deuss.inventorytracker.db.dao.RoomDAO;
import de.marcus_deuss.inventorytracker.db.entity.Category;
import de.marcus_deuss.inventorytracker.db.entity.Room;

public class EditInventoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "InventoryTracker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                finish();
            }
        });

        // room Spinner element
        Spinner spinnerRoom = findViewById(R.id.spinnerRoom);

        // Spinner click listener
        spinnerRoom.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        RoomDAO roomList = new RoomDAO(this);

        List<String> rooms = new ArrayList<>();

        for (Room room : roomList.getRoomList()) {
            rooms.add(room.getRoomName());
        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterRoom = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rooms);

        // Drop down layout style - list view with radio button
        dataAdapterRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerRoom.setAdapter(dataAdapterRoom);

        // category Spinner element
        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);

        // Spinner click listener
        spinnerCategory.setOnItemSelectedListener(this);

        CategoryDAO categoryList = new CategoryDAO(this);
        List<String> categories = new ArrayList<>();

        for (Category category : categoryList.getCategoryList()) {
            categories.add(category.getCategoryName());
        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCategory.setAdapter(dataAdapterCategory);
    }

    // assign chosen Spinners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.spinnerRoom:
                // On selecting a spinner item
                String room = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + room, Toast.LENGTH_SHORT).show();
                break;

            case R.id.spinnerCategory:
                // On selecting a spinner item
                String category = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + category, Toast.LENGTH_SHORT).show();

            default:
                // do nothing
        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
