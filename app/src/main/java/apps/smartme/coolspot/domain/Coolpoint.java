package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 04.05.2017.
 */

public class Coolpoint {

    private long popularity;
    private String name;
    private double latitude;
    private double longitude;
    private long timestamp;

    public Coolpoint() {
    }

    public Coolpoint(long popularity, String name, double latitude, double longitude, long timestamp) {
        this.popularity = popularity;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Coolpoint{" +
                "popularity=" + popularity +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timestamp=" + timestamp +
                '}';
    }
}
