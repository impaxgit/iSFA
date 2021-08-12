package com.example.pauldavies.isfa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Fragment_Maps extends Fragment  implements  OnMapReadyCallback
{
    View mview;
    MapView mapView;
    GoogleMap gMap;
    PlaceAutocompleteFragment placeAutoComplete;


    public Fragment_Maps()
    {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        mview = inflater.inflate(R.layout.fragment_fragment__maps, container, false);


        //Autocomplete

        //On Button Click
        Button search= mview.findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //  Toast.makeText(getActivity(),"OK", Toast.LENGTH_LONG).show();

                EditText locationSearch = (EditText)mview. findViewById(R.id.editText);
                String location = locationSearch.getText().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(getActivity());
                    try
                    {
                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e)
                    {
                        //e.printStackTrace();
                        Toast.makeText(getActivity(),"Address not found", Toast.LENGTH_LONG).show();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    gMap.addMarker(new MarkerOptions().position(latLng).title("Location"));
                    gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }

            }
        });
        return mview;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mapView=(MapView) mview.findViewById(R.id.maps);

        if(mapView!=null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        MapsInitializer.initialize(getContext());
        gMap=googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng kenya=new LatLng(0.5143,35.2698);

        gMap.addMarker(new MarkerOptions().position(kenya).title("Nairobi, Kenya"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(kenya));

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        gMap.setMyLocationEnabled(true);





    }
}

