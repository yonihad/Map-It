package movie.yoni.mapexample.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 19/11/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBConstants.DATA_BASE_NAME, null, DBConstants.VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + DBConstants.TABLE_NAME_CITIES + "(";
        sql += DBConstants.COL_ID_CITIES + " INTEGER PRIMARY KEY AUTOINCREMENT ,";
        sql += DBConstants.COL_NAME_CITIES + " TEXT ,";
        sql += DBConstants.COL_LOCATION_LATITUDE + " REAL ,";
        sql += DBConstants.COL_LOCATION_LONGITUDE + " REAL ";
        sql += ")";

        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE " + DBConstants.TABLE_NAME_LASTSEARCH + "(";
        sql += DBConstants.COL_ID_LASTSEARCH + " INTEGER PRIMARY KEY AUTOINCREMENT ,";
        sql += DBConstants.COL_NAME_LASTSEARCH + " TEXT ,";
        sql += DBConstants.COL_LOCATION_LATITUDE_LASTSEARCH + " REAL ,";
        sql += DBConstants.COL_LOCATION_LONGITUDE_LASTSEARCH + " REAL ";
        sql += ")";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_NAME_CITIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_NAME_LASTSEARCH);


        onCreate(sqLiteDatabase);

    }
}
