package com.zarra.uberclone2;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class ViewLocationsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_locations_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnRide=findViewById(R.id.btnRide);
        btnRide.setText("Give a ride to "+getIntent().getStringExtra("rUsername"));
        btnRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> requestCar=ParseQuery.getQuery("RequestCar");
                requestCar.whereEqualTo("username",getIntent().getStringExtra("rUsername"));
                requestCar.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(objects.size()>0 && e==null){
                            for(ParseObject object:objects){

                                object.put("requestAccepted",true);
                                object.put("driver", ParseUser.getCurrentUser().getUsername());
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null){
                                            Intent googleIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/?saddr="+getIntent().getDoubleExtra("dLatitude",0)+","+getIntent().getDoubleExtra("dLongitude",0)+"&daddr="+getIntent().getDoubleExtra("pLatitude",0)+","+getIntent().getDoubleExtra("pLongitude",0)));
                                            startActivity(googleIntent);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        intent.putExtra("dLatitude",driverLatitude);
//        intent.putExtra("dLongitude",driverLongitude);
//        intent.putExtra("pLatitude",requestLatitude);
//        intent.putExtra("pLongitude",requestLongitude);

        // Add a marker in Sydney and move the camera

        LatLng dLocation=new LatLng(getIntent().getDoubleExtra("dLatitude",0),getIntent().getDoubleExtra("dLongitude",0));
        LatLng pLocation=new LatLng(getIntent().getDoubleExtra("pLatitude",0),getIntent().getDoubleExtra("pLongitude",0));


        LatLngBounds.Builder builder=new LatLngBounds.Builder();


        Marker driverMarker=mMap.addMarker(new MarkerOptions().position(dLocation));
        driverMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car));

        Marker passengerMarker=mMap.addMarker(new MarkerOptions().position(pLocation));

        builder.include(driverMarker.getPosition());
        builder.include(passengerMarker.getPosition());

        LatLngBounds bounds=builder.build();
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngBounds(bounds,120);
        mMap.animateCamera(cameraUpdate);


    }
}