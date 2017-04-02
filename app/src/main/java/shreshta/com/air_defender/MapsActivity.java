package shreshta.com.air_defender;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GeoQuery geoQuery;

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MapsActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://airhelp-aea85.firebaseio.com/admins");
        GeoFire geoFire = new GeoFire(ref);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(25.4358297, 81.7483208), 100);
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        final Map<String, MarkerOptions> markerList = new HashMap<>();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                markerList.put(key, new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onKeyExited(String key) {
                markerList.remove(key);
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                markerList.put(key, new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
                mMap.clear();
                System.out.println("All initial data has been loaded and events have been fired!");
                for (Map.Entry<String, MarkerOptions> stringMarkerOptionsEntry : markerList.entrySet()) {
                    mMap.addMarker(stringMarkerOptionsEntry.getValue());
                }
                LatLng Ald = new LatLng(25.4358297, 81.7483208);
                mMap.addMarker(new MarkerOptions().position(Ald).title("Marker in CATC"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(Ald));
            }

            @Override
            public void onGeoQueryReady() {
                mMap.clear();
                System.out.println("All initial data has been loaded and events have been fired!");
                for (Map.Entry<String, MarkerOptions> stringMarkerOptionsEntry : markerList.entrySet()) {
                    mMap.addMarker(stringMarkerOptionsEntry.getValue());
                }
                LatLng Ald = new LatLng(25.4358297, 81.7483208);
                mMap.addMarker(new MarkerOptions().position(Ald).title("Marker in CATC"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(Ald));
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });
        LatLng Ald = new LatLng(25.4358297, 81.7483208);
        mMap.addMarker(new MarkerOptions().position(Ald).title("Marker in CATC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Ald));
    }
}
