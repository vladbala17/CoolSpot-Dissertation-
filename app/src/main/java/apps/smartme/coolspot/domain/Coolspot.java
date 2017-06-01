package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 17.05.2017.
 */

public class Coolspot {
    private String name;
    private String Latitude;
    private String Longitude;
    private String Poplarity;
    private String Timestamp;

    public Coolspot(String name, String latitude, String longitude, String poplarity, String timestamp) {
        this.name = name;
        Latitude = latitude;
        Longitude = longitude;
        Poplarity = poplarity;
        Timestamp = timestamp;
    }

    public Coolspot() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getPoplarity() {
        return Poplarity;
    }

    public void setPoplarity(String poplarity) {
        Poplarity = poplarity;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
