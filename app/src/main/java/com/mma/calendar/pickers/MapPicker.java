package com.mma.calendar.pickers;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.mma.calendar.constants.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by skuller on 2/18/15.
 */
public class MapPicker extends FragmentActivity
{
    private Button searchButton;
    private Button getLocation;
    private EditText search;
    private GoogleMap mMap; // Might be null if Google Play services APK is not
    // available.
    private LatLng targetLocation;
    private String address;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_picker);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        init();

        setUpMapIfNeeded();
        setCurrentLocation();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(LatLng latLng)
            {
                Log.e("", latLng.toString());
                setUpMap(latLng);
            }
        });

        String address = getIntent().getExtras().getString(Constants.ADDRESS);
        if (address != null)
        {
            search.setText(address);
            searchAddress(address);
        }

    }

    private void init()
    {
        searchButton = (Button) findViewById(R.id.search_button);
        getLocation = (Button) findViewById(R.id.get_location);
        search = (EditText) findViewById(R.id.search);

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String textToSearch = search.getText().toString();
                if (textToSearch != null && textToSearch.length() > 0)
                {
                    searchAddress(textToSearch);
                }
            }
        });

        getLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (targetLocation != null)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(Constants.LATITUDE, targetLocation.latitude);
                    returnIntent.putExtra(Constants.LONGITUDE, targetLocation.longitude);
                    returnIntent.putExtra(Constants.ADDRESS, address);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    public void searchAddress(String textToSearch)
    {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        this.address = textToSearch;
        List<Address> fromLocationName = null;
        try
        {
            fromLocationName = geocoder.getFromLocationName(textToSearch, 1);

            if (fromLocationName != null && fromLocationName.size() > 0)
            {
                Address a = fromLocationName.get(0);
                targetLocation = new LatLng(a.getLatitude(), a.getLongitude());

                setUpMap(targetLocation);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setCurrentLocation()
    {
        mMap.setMyLocationEnabled(true);
        Location myLoction = mMap.getMyLocation();
        if (myLoction != null)
        {
            LatLng location = new LatLng(myLoction.getLatitude(), myLoction.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 10);
            mMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded()
    {
        if (mMap == null)
        {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the
     * camera. In this case, we just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap}
     * is not null.
     */
    public void setUpMap(LatLng latLng)
    {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
    }
}
