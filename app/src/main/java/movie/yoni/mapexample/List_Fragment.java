package movie.yoni.mapexample;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import movie.yoni.mapexample.db.DBConstants;
import movie.yoni.mapexample.db.DBHandlerFavorites;
import movie.yoni.mapexample.db.DBHandlerLastSearch;

public class List_Fragment extends Fragment implements View.OnClickListener, NearMeRecyclerView.OnNearByAdapterItemClicked, SearchView.OnQueryTextListener, MainActivity.KMorMiles/*,  View.OnTouchListener*/ {

    Boolean aBoolean;
    TextView textView;
    MainActivity main;
    ArrayList<Cities> cities;
    ArrayList<Cities> favoritesArrayList;
    private SearchView et_search;
    private Button btn_nearme;
    private Button btn_favorites;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DBHandlerFavorites dbHandlerFavorites;
    private DBHandlerLastSearch dbHandlerLastSearch;


    public List_Fragment() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_nearme:

                fillList();

                break;

            case R.id.btn_favorites:

                cities.clear();


                cities.addAll(dbHandlerFavorites.showAllFavorites());

                mAdapter.notifyDataSetChanged();

                break;
        }
    }

    public void fillList() {
        cities.clear();


        cities.add(new Cities("airport", null, DBConstants.IconUrl + "airport-71.png"));
        cities.add(new Cities("bank", null, DBConstants.IconUrl + "bank_dollar-71.png"));
        cities.add(new Cities("bar", null, DBConstants.IconUrl + "bar-71.png"));
        cities.add(new Cities("car_rental", null, DBConstants.IconUrl + "generic_business-71.png"));
        cities.add(new Cities("gym", null, DBConstants.IconUrl + "school-71.png"));
        cities.add(new Cities("hair_care", null, DBConstants.IconUrl + "generic_business-71.png"));
        cities.add(new Cities("hospital", null, DBConstants.IconUrl + "doctor-71.png"));
        cities.add(new Cities("laundry", null, DBConstants.IconUrl + "generic_business-71.png"));
        cities.add(new Cities("library", null, DBConstants.IconUrl + "school-71.png"));
        cities.add(new Cities("museum", null, DBConstants.IconUrl + "museum-71.png"));
        cities.add(new Cities("night_club", null, DBConstants.IconUrl + "bar-71.png"));
        cities.add(new Cities("parking", null, DBConstants.IconUrl + "generic_business-71.png"));
        cities.add(new Cities("pharmacy", null, DBConstants.IconUrl + "shopping-71.png"));
        cities.add(new Cities("police", null, DBConstants.IconUrl + "police-71.png"));
        cities.add(new Cities("shopping_mall", null, DBConstants.IconUrl + "shopping-71.png"));
        cities.add(new Cities("synagogue", null, DBConstants.IconUrl + "worship_jewish-71.png"));
        cities.add(new Cities("zoo", null, DBConstants.IconUrl + "zoo-71.png"));


        mAdapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, container, false);


        textView = view.findViewById(R.id.tv);

        dbHandlerFavorites = new DBHandlerFavorites(getActivity());
        dbHandlerLastSearch = new DBHandlerLastSearch(getActivity());

        if (getActivity() instanceof MainActivity) {
            main = (MainActivity) getActivity();
            main.KMorMileslistener(this);
        }

        btn_nearme = view.findViewById(R.id.btn_nearme);
        btn_nearme.setOnClickListener(this);
        btn_favorites = view.findViewById(R.id.btn_favorites);
        btn_favorites.setOnClickListener(this);
        et_search = view.findViewById(R.id.et_search);

        et_search.setOnQueryTextListener(this);
        //et_search.setOnTouchListener(this);

        cities = new ArrayList<Cities>();
        AddList();

        mRecyclerView = view.findViewById(R.id.recycler_view1);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NearMeRecyclerView(getActivity(), cities, this);
        mRecyclerView.setAdapter(mAdapter);


        Bundle bundle = getArguments();
        if (bundle != null) {
            aBoolean = bundle.getBoolean("Single");
        }


        return view;
    }

    public void AddList() {
        cities.clear();


        cities.addAll(dbHandlerLastSearch.showAllLastSearch());

    }

    public void newList(ArrayList<Cities> citiesArrayList) {
        cities.clear();

    }


    @Override
    public void onNearByAdapterItemClicked(int pos, ArrayList<Cities> cities) {

    }

    @Override
    public void onNearByAdapterItemTypeClick(String type) {
        String Address = DBConstants.URL + main.latitude + "," + main.longitude + DBConstants.RestURL + type + DBConstants.EndURL + DBConstants.API_KEY;
        MyTask myTask = new MyTask();
        myTask.execute(Address);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {


        query = query.replaceAll("\\s", "+");


        String Address = DBConstants.URL_TEXT_SEARCH + query + DBConstants.EndURL + DBConstants.API_KEY;
        MyTask myTask = new MyTask();
        myTask.execute(Address);

        return true;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onKMorMilesClicked() {

        mAdapter.notifyDataSetChanged();


    }


//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        final int DRAWABLE_LEFT = 0;
//        final int DRAWABLE_TOP = 1;
//        final int DRAWABLE_RIGHT = 2;
//        final int DRAWABLE_BOTTOM = 3;
//
//        if(event.getAction() == MotionEvent.ACTION_UP) {
//            if(event.getRawX() >= (et_search.getRight() - et_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                // your action here
//                String str =textView.getText().toString();
//                str.replace(" ","+");
//                String Address = DBConstants.URL_TEXT_SEARCH + str+  DBConstants.EndURL + DBConstants.API_KEY;
//                MyTask myTask = new MyTask();
//                myTask.execute(Address);
//
//                return true;
//            }
//        }
//        return false;
//
//    }

    public class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            if (!haveNetworkConnection()) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("No Internet Connection");
                alertDialogBuilder.setMessage("You are offline please check your internet connection");
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }

        }


        @Override
        protected String doInBackground(String... strings) {
            URL url;
            BufferedReader bufferedReader = null;
            HttpURLConnection connection = null;
            StringBuilder builder = new StringBuilder();

            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line + "\n");
                    }

                }


            } catch (MalformedURLException e) {
                Toast.makeText(main, "There is problem ! You are not connected to internet", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(main, "There is problem ! You are not connected to internet", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }

                } catch (IOException e) {
                    Toast.makeText(main, "There is problem ! You are not connected to internet", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }


            return builder.toString();


        }

        @Override
        protected void onPostExecute(String res) {
            if (res.length() == 0 || res == null) {
            } else {
                try {
                    JSONObject object = new JSONObject(res);
                    JSONArray array = object.getJSONArray("results");
                    dbHandlerLastSearch.deleteAllLastSearh();
                    cities.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        JSONObject geometry = object1.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        LatLng latLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
                        String icon = object1.getString("icon");
                        String name = object1.getString("name");
                        Cities city = new Cities(name, latLng, icon);
                        cities.add(city);
                    }

                    dbHandlerLastSearch.AddAll(cities);
                    mAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        private boolean haveNetworkConnection() {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();

            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        }

    }

}
