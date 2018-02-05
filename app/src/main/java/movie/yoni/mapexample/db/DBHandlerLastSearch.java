package movie.yoni.mapexample.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import movie.yoni.mapexample.Cities;

/**
 * Created by User on 06/12/2017.
 */

public class DBHandlerLastSearch {


    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public DBHandlerLastSearch(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public boolean AddAll(ArrayList<Cities> cities) {


        sqLiteDatabase = dbHelper.getWritableDatabase();

        for (int i = 0; i < cities.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(DBConstants.COL_NAME_LASTSEARCH, cities.get(i).getName());
            values.put(DBConstants.COL_LOCATION_LATITUDE_LASTSEARCH, cities.get(i).getLocation().latitude);
            values.put(DBConstants.COL_LOCATION_LONGITUDE_LASTSEARCH, cities.get(i).getLocation().longitude);


            sqLiteDatabase.insert(DBConstants.TABLE_NAME_LASTSEARCH, null, values);
        }


        dbHelper.close();

        return true;
    }


    public boolean addToLastSearch(Cities city) {

        sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_NAME_LASTSEARCH, city.getName());
        values.put(DBConstants.COL_LOCATION_LATITUDE_LASTSEARCH, city.getLocation().toString());
        values.put(DBConstants.COL_LOCATION_LONGITUDE_LASTSEARCH, city.getLocation().toString());


        long result = sqLiteDatabase.insert(DBConstants.TABLE_NAME_LASTSEARCH, null, values);

        dbHelper.close();

        return result != -1;
    }

    public void deleteFromLastSearch(int id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.delete(DBConstants.TABLE_NAME_LASTSEARCH, DBConstants.COL_ID_LASTSEARCH + "=?", new String[]{String.valueOf(id)});
        } catch (SQLiteException e) {
            e.getCause();
        } finally {
            if (db.isOpen())
                db.close();
        }

    }

    public boolean deleteAllLastSearh() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.delete(DBConstants.TABLE_NAME_LASTSEARCH, null, null);
        } catch (SQLiteException e) {
            e.getCause();
            return false;
        } finally {
            if (db.isOpen())
                db.close();
        }


        return true;
    }

    public ArrayList<Cities> showAllLastSearch() {

        sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DBConstants.TABLE_NAME_LASTSEARCH, null, null, null, null, null, null);

        ArrayList<Cities> cities = new ArrayList<>();
        Cities cities1;
        String name;
        double lat, lang;
        int id;
        while (cursor.moveToNext()) {
            //  id = cursor.getInt(cursor.getColumnIndex(DBConstants.COL_ID_CITIES));
            name = cursor.getString(cursor.getColumnIndex(DBConstants.COL_NAME_LASTSEARCH));
            lat = cursor.getDouble(cursor.getColumnIndex(DBConstants.COL_LOCATION_LATITUDE_LASTSEARCH));
            lang = cursor.getDouble(cursor.getColumnIndex(DBConstants.COL_LOCATION_LONGITUDE_LASTSEARCH));
            LatLng location = new LatLng(lat, lang);
            cities1 = new Cities(name, location);
            cities.add(cities1);
        }


        return cities;
    }
}


