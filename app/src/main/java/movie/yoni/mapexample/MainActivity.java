package movie.yoni.mapexample;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import movie.yoni.mapexample.db.DBHandlerFavorites;

public class MainActivity extends AppCompatActivity implements
        LocationListener, NearMeRecyclerView.OnNearByAdapterItemClicked {

    public double latitude;
    public double longitude;
    public boolean isKM = true;
    DBHandlerFavorites dbHandlerFavorites;
    KMorMiles callback = null;
    private LocationManager locationManager;
    private String provider;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED))
                Toast.makeText(getBaseContext(), "The battery is charging.", Toast.LENGTH_LONG).show();

            else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED))
                Toast.makeText(getBaseContext(), "The battery is discharging.", Toast.LENGTH_LONG).show();


        }
    };

    public void KMorMileslistener(List_Fragment fragment) {
        this.callback = fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        latitude = sharedPreferences.getFloat("latitude", 0);
        longitude = sharedPreferences.getFloat("longitude", 0);


        if (isSingle()) {

            Bundle bundle = new Bundle();
            bundle.putBoolean("Single", true);


            Fragment fragment = new List_Fragment();
            fragment.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.single, fragment);
            ft.commit();
        } else {


            Bundle bundle = new Bundle();
            bundle.putBoolean("Single", false);
            Fragment fragment = new List_Fragment();
            fragment.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frag_list, fragment);
            ft.commit();

            Fragment fragment1 = new Maps_Fragment();
            FragmentManager fm1 = getSupportFragmentManager();
            FragmentTransaction ft1 = fm1.beginTransaction();
            ft1.replace(R.id.mapView, fragment1, "map");
            ft1.commit();
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, true);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1234);
            }
            return;
        }
        Location lastKnownLocation = locationManager
                .getLastKnownLocation(provider);

        if (lastKnownLocation == null) {

        } else {


        }

    }


    public boolean isSingle() {
        View layout = findViewById(R.id.single);
        return layout != null;


    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        registerReceiver(receiver, filter);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 3000, 1, this);


    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
        locationManager.removeUpdates(this);

    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //Toast.makeText(this, "Found Location", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("latitude", ((float) latitude));
        editor.putFloat("longitude", ((float) longitude));
        editor.commit();

    }

    @Override
    public void onStatusChanged(String s, int status, Bundle bundle) {

        switch (status) {
            case LocationProvider.AVAILABLE:
                break;

            case LocationProvider.OUT_OF_SERVICE:
                break;

            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                break;
        }
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onNearByAdapterItemClicked(int pos, ArrayList<Cities> list) {
        Cities citie = list.get(pos);
        Maps_Fragment map = (Maps_Fragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        if (map == null) {

            Fragment fragment = new Maps_Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("Name", citie.getName());
            bundle.putDouble("Latitude", (citie.getLocation()).latitude);
            bundle.putDouble("Longitude", (citie.getLocation()).longitude);
            fragment.setArguments(bundle);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.single, fragment);
            ft.addToBackStack(null);
            ft.commit();

        } else {

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag("map");
            if (fragment == null) {


                Bundle bundle = new Bundle();

                bundle.putString("Name", citie.getName());
                bundle.putDouble("Latitude", (citie.getLocation()).latitude);
                bundle.putDouble("Longitude", (citie.getLocation()).longitude);
                fragment.setArguments(bundle);


                FragmentTransaction ft1 = fm.beginTransaction();
                ft1.replace(R.id.mapView, fragment, "map");
                ft1.commit();
            } else {
                GoogleMap aMap = ((Maps_Fragment) fragment).getmMap();

                LatLng currentLocation = new LatLng((citie.getLocation()).latitude, (citie.getLocation()).longitude);
                aMap.clear();
                aMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in " + citie.getName()));
                aMap.animateCamera(CameraUpdateFactory.zoomIn());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));

            }
        }
    }

    @Override
    public void onNearByAdapterItemTypeClick(String type) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:

                dbHandlerFavorites = new DBHandlerFavorites(this);

                dbHandlerFavorites.deleteAllFavorites();

                break;


            case R.id.action_miles:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose what you want");
                builder.setMessage("In which distance do you want to change ?");
                builder.setPositiveButton("KM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (callback != null) {
                            if (!isKM) {
                                isKM = true;
                                callback.onKMorMilesClicked();
                            }

                        }
                    }
                }).setNegativeButton("MILES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (callback != null) {
                            if (isKM) {
                                isKM = false;
                                callback.onKMorMilesClicked();
                            }

                        }
                    }
                });
                builder.show();


                break;


        }
        return super.onOptionsItemSelected(item);
    }

    public interface KMorMiles {
        void onKMorMilesClicked();

    }

}

