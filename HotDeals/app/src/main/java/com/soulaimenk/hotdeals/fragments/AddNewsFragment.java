package com.soulaimenk.hotdeals.fragments;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schibstedspain.leku.LocationPickerActivity;
import com.soulaimenk.hotdeals.Constants;
import com.soulaimenk.hotdeals.R;
import com.soulaimenk.hotdeals.Utils;
import com.soulaimenk.hotdeals.wrappers.DiscountArticle;
import com.soulaimenk.hotdeals.wrappers.NewArticle;

import java.util.Date;
import java.util.List;

/**
 * Created by Soulaimen on 22/07/2016.
 */
public class AddNewsFragment extends Fragment implements View.OnClickListener, LocationListener {

    private final int PLACE_PICKER_REQUEST = 1;
    private final int PICTURE_PICKER_REQUEST = 2;
    private final int TAKE_PICTURE_REQUEST = 3;
    private final int CROP_PICTURE_REQUEST = 4;
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private LocationManager mLocationManager;
    private Uri picUri;
    //Views
    private Button mSignOutBtn;
    private Button mSaveBtn;
    private EditText mNewArticleTitleET;
    private EditText mNewArticleDescriptionET;
    private Button mCurrentPostionBtn;
    private Button mChoosePostionBtn;
    private Button mTakePictureBtn;
    private Button mChoosePictureBtn;
    private Button mSaveNewArticleBtn;

    //To save
    private Location mLocation;
    private String mNewArticleTitleStr;
    private String mNewArticleDescriptionStr;
    private String mLatitudeStr;
    private String mLongitudeStr;
    private String mNewArticlePictureStr;

    public AddNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getBaseContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_add_news, container, false);
        // Get views
        mSignOutBtn = (Button) view.findViewById(R.id.signOut);
        mSaveBtn = (Button) view.findViewById(R.id.save);

        mNewArticleTitleET = (EditText) view.findViewById(R.id.newArticleTitleET);
        mNewArticleDescriptionET = (EditText) view.findViewById(R.id.newArticleDescriptionET);
        mCurrentPostionBtn = (Button) view.findViewById(R.id.currentPositionBtn);
        mChoosePostionBtn = (Button) view.findViewById(R.id.choosePositionBtn);
        mChoosePictureBtn = (Button) view.findViewById(R.id.choosePictureBtn);
        mTakePictureBtn = (Button) view.findViewById(R.id.takePictureBtn);
        mSaveNewArticleBtn = (Button) view.findViewById(R.id.saveNewArticleBtn);

        //Buttons OnClickListener
        mCurrentPostionBtn.setOnClickListener(this);
        mChoosePostionBtn.setOnClickListener(this);
        mChoosePictureBtn.setOnClickListener(this);
        mTakePictureBtn.setOnClickListener(this);
        mSaveNewArticleBtn.setOnClickListener(this);


        // SignOut Btn action
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(context, "Sign out", Toast.LENGTH_SHORT).show();
            }
        });

        // SaveBtn action
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewArticle newArticle = new NewArticle("Mag AZERTY", "Description  ", new Date(), Constants.LATITUDE_TEST, Constants.LONGITUDE_TEST, Constants.IMAGE_BASE64_TEST);
                String key = mDatabase.child(Constants.FB_NEW_ARTICLE).push().getKey();
                newArticle.setUid(key);
                mDatabase.child(Constants.FB_NEW_ARTICLE).child(key).setValue(newArticle);

                DiscountArticle discountArticle = new DiscountArticle("Mag AZERTY Discount", Constants.IMAGE_BASE64_TEST, "Description  ", new Date(), 15.5d, 40, 2, Constants.LATITUDE_TEST, Constants.LONGITUDE_TEST);
                String key1 = mDatabase.child(Constants.FB_DISCOUNT_ARTICLE).push().getKey();
                discountArticle.setUid(key1);
                mDatabase.child(Constants.FB_DISCOUNT_ARTICLE).child(key1).setValue(discountArticle);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.currentPositionBtn:
                mLocation = getLastKnownLocation();
                if (mLocation != null) {
                    Log.v("Current Location", "Latitude :" + mLocation.getLatitude() + "Longitude :" + mLocation.getLongitude());
                } else {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
                break;
            case R.id.choosePositionBtn:

                /*PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }*/

                Intent i = new Intent(context, LocationPickerActivity.class);
                startActivityForResult(i, PLACE_PICKER_REQUEST);
                break;
            case R.id.choosePictureBtn:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 256);
                intent.putExtra("outputY", 256);
                intent.putExtra("aspectX", 1.5);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, PICTURE_PICKER_REQUEST);
                break;
            case R.id.takePictureBtn:
                Intent cameraIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, TAKE_PICTURE_REQUEST);
                break;
            case R.id.saveNewArticleBtn:
                mNewArticleTitleStr = mNewArticleTitleET.getText().toString();
                mNewArticleDescriptionStr = mNewArticleDescriptionET.getText().toString();

                NewArticle newArticle = new NewArticle(mNewArticleTitleStr, mNewArticleDescriptionStr, new Date(), String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()), mNewArticlePictureStr);
                String key = mDatabase.child(Constants.FB_NEW_ARTICLE).push().getKey();
                newArticle.setUid(key);
                mDatabase.child(Constants.FB_NEW_ARTICLE).child(key).setValue(newArticle);
                break;
        }
    }

    private Location getLastKnownLocation() {

        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.v("Current Location", "Latitude :" + mLocation.getLatitude() + "Longitude :" + mLocation.getLongitude());
        mLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
              /*  Place place = PlacePicker.getPlace(context, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();*/

                mLatitudeStr = String.valueOf(data.getDoubleExtra(LocationPickerActivity.LATITUDE, 0));
                Log.d("LATITUDE****", mLatitudeStr);
                mLongitudeStr = String.valueOf(data.getDoubleExtra(LocationPickerActivity.LONGITUDE, 0));
                Log.d("LONGITUDE****", mLongitudeStr);
            }
        }

        if (requestCode == PICTURE_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bitmapPic = extras.getParcelable("data");
                    mNewArticlePictureStr = Utils.encodeToBase64(bitmapPic, Bitmap.CompressFormat.JPEG, 100);
                }
            }}
            if (requestCode == TAKE_PICTURE_REQUEST) {
                if (resultCode == getActivity().RESULT_OK) {
                    final Bundle extras = data.getExtras();
                    if (extras != null) {
                        // Bitmap bitmapPic = (Bitmap) data.getExtras().get("data");
                        // mNewArticlePictureStr = Utils.encodeToBase64(bitmapPic, Bitmap.CompressFormat.JPEG, 100);
                        Toast
                                .makeText(context, "This device  support the crop action!", Toast.LENGTH_SHORT).show();
                        picUri = data.getData();
                        performCrop();
                    }
                }
            }
            if (requestCode == CROP_PICTURE_REQUEST) {
                if (resultCode == getActivity().RESULT_OK) {
                    final Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bitmapPic = (Bitmap) data.getExtras().get("data");
                        mNewArticlePictureStr = Utils.encodeToBase64(bitmapPic, Bitmap.CompressFormat.JPEG, 100);

                    }
                }
            }

    }

    private void performCrop() {

        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("scale", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1.5);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PICTURE_REQUEST);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(context, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
