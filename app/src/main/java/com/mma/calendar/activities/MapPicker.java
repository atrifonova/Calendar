package com.mma.calendar.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mma.calendar.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by skuller on 2/18/15.
 */
public class MapPicker extends FragmentActivity {
    private Button searchButton;
    private EditText search;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_picker);

        init();

        setUpMapIfNeeded();
        setCurrentLocation();


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.e("", latLng.toString());
                setUpMap(latLng);
            }
        });


    }

    private void init() {
        searchButton = (Button) findViewById(R.id.search_button);
        search = (EditText) findViewById(R.id.search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToSearch = search.getText().toString();
                if (textToSearch != null && textToSearch.length() > 0) {
                    searchAddress(textToSearch);
                }
            }
        });
    }


    public void searchAddress(String textToSearch) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> fromLocationName = null;
        try {
            fromLocationName = geocoder.getFromLocationName(textToSearch, 1);

            if (fromLocationName != null && fromLocationName.size() > 0) {
                Address a = fromLocationName.get(0);
                LatLng l = new LatLng(a.getLatitude(), a.getLongitude());
                setUpMap(l);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setCurrentLocation() {
        mMap.setMyLocationEnabled(true);
        Location myLoction = mMap.getMyLocation();
        if (myLoction != null) {
            LatLng location = new LatLng(myLoction.getLatitude(), myLoction.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 10);
            mMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
        }

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    public void setUpMap(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String lat = data.getStringExtra("lat");
                String lon = data.getStringExtra("lon");
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}
