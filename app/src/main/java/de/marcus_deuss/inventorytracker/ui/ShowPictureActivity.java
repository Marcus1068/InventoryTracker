package de.marcus_deuss.inventorytracker.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import de.marcus_deuss.inventorytracker.R;
import de.marcus_deuss.inventorytracker.db.dao.InventoryDAO;

public class ShowPictureActivity extends AppCompatActivity {

    private ImageView imageViewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        // all data objects
        InventoryDAO inventory = new InventoryDAO(this);

        imageViewPhoto = findViewById(R.id.imageView_picture_fullscreen);

        byte[] bytes = getIntent().getByteArrayExtra("bitmapbytes");

        Bitmap myImage = inventory.convertByteToImage(bytes);
        imageViewPhoto.setImageBitmap(myImage);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
