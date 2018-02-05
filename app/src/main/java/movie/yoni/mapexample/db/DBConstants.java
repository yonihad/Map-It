package movie.yoni.mapexample.db;

/**
 * Created by User on 15/10/2017.
 */

public class DBConstants {

    public static final String API_KEY = "AIzaSyAGACz4gMFAGjT7XiUQY1X-VkNweJFoi9I";
    public static final String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    public static final String RestURL = "&sensor=true&type=";
    public static final String EndURL = "&rankby=distance&key=";
    public static final String IconUrl = "https://maps.gstatic.com/mapfiles/place_api/icons/";
    public static final String URL_TEXT_SEARCH = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";

    public static final String DATA_BASE_NAME = "MapExample.db";

    public static final String TABLE_NAME_CITIES = "cities";
    public static final String COL_ID_CITIES = "_id";
    public static final String COL_NAME_CITIES = "name";
    public static final String COL_LOCATION_LATITUDE = "latitude";
    public static final String COL_LOCATION_LONGITUDE = "longitude";

    public static final String TABLE_NAME_LASTSEARCH = "lastsearch";
    public static final String COL_ID_LASTSEARCH = "_id_last";
    public static final String COL_NAME_LASTSEARCH = "name_last";
    public static final String COL_LOCATION_LATITUDE_LASTSEARCH = "latitude_last";
    public static final String COL_LOCATION_LONGITUDE_LASTSEARCH = "longitude_last";


    public static final int VER = 5;
}
