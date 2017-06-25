package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 17.05.2017.
 */

public class Coolspot {

    private String name;
    private double latitude;
    private double longitude;
    private long popularity;
    private long timestamp;

    public Coolspot(String name, long timestamp, double latitude, double longitude,long popularity) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.popularity = popularity;
        this.timestamp = timestamp;
    }

    public Coolspot() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }
}
