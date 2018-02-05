package movie.yoni.mapexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Maps_Fragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private String place;
    private double longitude;
    private double latitude;
    private Button myLocationButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);


        Bundle bundle = getArguments();

        if (bundle != null) {
            place = bundle.getString("Name");
            longitude = bundle.getDouble("Longitude");
            latitude = bundle.getDouble("Latitude");
            if (latitude == 0 && longitude == 0) {
                place = "Tel Aviv";
                longitude = 34.7805700;
                latitude = 32.0808800;
            }

        } else {
            place = "Tel Aviv";
            longitude = 34.7805700;
            latitude = 32.0808800;
        }


        myLocationButton = view.findViewById(R.id.myLocationButton);
        myLocationButton.setOnClickListener(this);


        MapView mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);


        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng currentLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in " + place));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
    }

    final GoogleMap getmMap() {
        return mMap;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myLocationButton:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                if (sharedPreferences.getFloat("latitude", 0) != 0 && sharedPreferences.getFloat("longitude", 0) != 0) {
                    LatLng currentLocation = new LatLng(sharedPreferences.getFloat("latitude", 0), sharedPreferences.getFloat("longitude", 0));

                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("I am here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());

                } else {
                    Toast.makeText(getActivity(), "Location Not Found", Toast.LENGTH_LONG).show();
                }


                break;
        }
    }

}
