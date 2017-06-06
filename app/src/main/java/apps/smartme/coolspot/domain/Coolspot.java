package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 17.05.2017.
 */

public class Coolspot {
    private String name;

    private long popularity;
    private long Timestamp;

    public Coolspot(String name, long popularity, long timestamp) {
        this.name = name;

        this.popularity = popularity;
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

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }
}
