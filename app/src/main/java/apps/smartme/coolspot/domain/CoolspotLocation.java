package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 23.05.2017.
 */

public class CoolspotLocation {
    private String name;
    private double latitude;
    private double longitude;

    public CoolspotLocation() {
    }

    public CoolspotLocation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
