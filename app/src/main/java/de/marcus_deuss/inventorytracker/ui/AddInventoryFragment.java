package de.marcus_deuss.inventorytracker.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.marcus_deuss.inventorytracker.InventoryApp;
import de.marcus_deuss.inventorytracker.R;
import de.marcus_deuss.inventorytracker.db.dao.InventoryDAO;
import de.marcus_deuss.inventorytracker.db.entity.Room;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddInventoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddInventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddInventoryFragment extends DialogFragment implements OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;

    private static final String TAG = "InventoryTracker";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // all UI elements
    private Button takePhotoButton;
    private ImageView imageViewPhoto;
    private TextView textViewInventory;
    private TextView textViewPrice;
    private Button saveButton;
    private Spinner roomSpinner;

    // store new picture file name and path (unique name in system)
    String mCurrentPhotoPath;

    Context thiscontext;

    // all data objects
    InventoryDAO inventory = new InventoryDAO(getContext());

    private OnFragmentInteractionListener mListener;

    public AddInventoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddInventoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddInventoryFragment newInstance(String param1, String param2) {
        AddInventoryFragment fragment = new AddInventoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        thiscontext = container.getContext();

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_inventory, container, false);

        takePhotoButton = view.findViewById(R.id.button_take_photo);
        takePhotoButton.setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });

        imageViewPhoto = view.findViewById(R.id.imageView_photo);

        textViewInventory = view.findViewById(R.id.textview_inventory);
        textViewPrice = view.findViewById(R.id.textview_price);

        saveButton = view.findViewById(R.id.button_save_inventory);

        saveButton.setOnClickListener(v -> {
            saveInventory();
        });

        roomSpinner = view.findViewById(R.id.spinner_room);

        ArrayList<String> roomValues = new ArrayList<String>();

        for (Room room : InventoryApp.myRoomDAO.getRoomList()) {
            roomValues.add(room.getRoomName());
        }

        ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, roomValues);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        roomSpinner.setAdapter(roomAdapter);
        // Spinner click listener
        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int i;
                i = 1;
                i = 2 +3 ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // load database stuff
        loadFromDatabase();

        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

       if (view == imageViewPhoto) {
            //datePickerDialog.show();
        } else if (view == takePhotoButton) {
           //
       }
    }


    // picture handling stuff
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = thiscontext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // take a picture
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(thiscontext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(thiscontext,
                        "de.marcus_deuss.inventorytracker",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    // handle taking the picture, scaled to fit the imageView for display
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the dimensions of the View
            int targetW = imageViewPhoto.getWidth();
            int targetH = imageViewPhoto.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            imageViewPhoto.setImageBitmap(bitmap);

        }
    }

    private void saveInventory(){
        InventoryApp.myInventory.setInventoryName(textViewInventory.getText().toString());
        InventoryApp.myInventory.setPrice(Integer.valueOf(textViewPrice.getText().toString()));
        byte []myImage = inventory.convertImageToByte(imageViewPhoto.getDrawable());
        InventoryApp.myInventory.setImage(myImage);

        // save or update
        //InventoryApp.myInventoryDAO.saveInventory(InventoryApp.myInventory);
        InventoryApp.myInventoryDAO.updateInventory(InventoryApp.myInventory);

        // dialog fragment dismiss method to close fragment
        dismiss();
    }

    // init UI elements with data
    private void loadFromDatabase(){
        textViewInventory.setText(InventoryApp.myInventory.getInventoryName());
        textViewPrice.setText(String.valueOf(InventoryApp.myInventory.getPrice()));

        Bitmap myImage = inventory.convertByteToImage(InventoryApp.myInventory.getImage());
        imageViewPhoto.setImageBitmap(myImage);

        // fill the spinner elements
        //roomSpinner.setAdapter(roomAdapter);
    }

    // assign chosen Spinners
    // TODO does not work here, used to work in avtivity
/*    @Override
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
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
