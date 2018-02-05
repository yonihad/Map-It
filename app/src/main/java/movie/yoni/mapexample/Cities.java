package movie.yoni.mapexample;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 27/08/2017.
 */

public class Cities {

    private String name;
    private LatLng location;
    private String icon;
    private int id;


    public Cities() {
    }

    public Cities(int id, String name, LatLng location) {
        this.id = id;
        this.name = name;
        this.location = location;
        icon = null;
    }

    public Cities(String name, LatLng location) {
        this.name = name;
        this.location = location;
        icon = null;

    }

    public Cities(String name, LatLng location, String icon) {
        this.name = name;
        this.location = location;
        this.icon = icon;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public double getLatitude() {
        return location.latitude;
    }

    public double getLongitude() {
        return location.longitude;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return name;
    }
}
