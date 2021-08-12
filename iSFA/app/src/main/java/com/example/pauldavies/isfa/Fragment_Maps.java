package com.example.pauldavies.isfa;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Fragment_Maps extends Fragment  implements  OnMapReadyCallback
{
    GoogleMap gMap;
    PlaceAutocompleteFragment placeAutoComplete;
    Context context;
    MarkerOptions markerOptions;


    public Fragment_Maps()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_fragment__maps, container, false);
        MapView gMapView = view.findViewById(R.id.maps);
        gMapView.getMapAsync(this);

        gMapView.onCreate(savedInstanceState);
        gMapView.onResume(); // needed to get the map to display immediately
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        gMap = map;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        LatLng lat = new LatLng(-1.291514, 36.874260);
        gMap.addMarker(new MarkerOptions().position(lat).title("Marker"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
    }
}

