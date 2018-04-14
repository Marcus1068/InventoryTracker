package de.marcus_deuss.inventorytracker.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import de.marcus_deuss.inventorytracker.InventoryApp;
import de.marcus_deuss.inventorytracker.R;

public class AddInventoryActivity extends AppCompatActivity implements AddInventoryFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);

        // get mode for edit or add inventory
        int mode = getIntent().getIntExtra("updateMode", InventoryApp.ADD_INVENTORY);

        Bundle bundle = new Bundle();
        bundle.putInt("updateMode", mode);

        // start add inventory fragment
        AddInventoryFragment fragment = new AddInventoryFragment();
        fragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();


        fragmentTransaction.add(R.id.frame_links, fragment, "TAG");
        fragmentTransaction.commit();

    }
    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }
}
