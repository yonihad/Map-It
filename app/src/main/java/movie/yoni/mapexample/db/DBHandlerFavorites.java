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
 * Created by User on 19/11/2017.
 */

public class DBHandlerFavorites {

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public DBHandlerFavorites(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }


    public boolean addToFavorites(Cities city) {

        sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_NAME_CITIES, city.getName());
        values.put(DBConstants.COL_LOCATION_LATITUDE, city.getLocation().toString());
        values.put(DBConstants.COL_LOCATION_LONGITUDE, city.getLocation().toString());


        long result = sqLiteDatabase.insert(DBConstants.TABLE_NAME_CITIES, null, values);

        dbHelper.close();

        return result != -1;
    }

    public void deleteFromFavorites(int id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.delete(DBConstants.TABLE_NAME_CITIES, DBConstants.COL_ID_CITIES + "=?", new String[]{String.valueOf(id)});
        } catch (SQLiteException e) {
            e.getCause();
        } finally {
            if (db.isOpen())
                db.close();
        }

    }

    public boolean deleteAllFavorites() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.delete(DBConstants.TABLE_NAME_CITIES, null, null);
        } catch (SQLiteException e) {
            e.getCause();
            return false;
        } finally {
            if (db.isOpen())
                db.close();
        }


        return true;
    }

    public ArrayList<Cities> showAllFavorites() {

        sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DBConstants.TABLE_NAME_CITIES, null, null, null, null, null, null);

        ArrayList<Cities> cities = new ArrayList<>();
        Cities cities1;
        String name;
        double lat, lang;
        int id;
        while (cursor.moveToNext()) {
            //  id = cursor.getInt(cursor.getColumnIndex(DBConstants.COL_ID_CITIES));
            name = cursor.getString(cursor.getColumnIndex(DBConstants.COL_NAME_CITIES));
            lat = cursor.getDouble(cursor.getColumnIndex(DBConstants.COL_LOCATION_LATITUDE));
            lang = cursor.getDouble(cursor.getColumnIndex(DBConstants.COL_LOCATION_LONGITUDE));
            LatLng location = new LatLng(lat, lang);
            cities1 = new Cities(name, location);
            cities.add(cities1);
        }


        return cities;
    }
}
