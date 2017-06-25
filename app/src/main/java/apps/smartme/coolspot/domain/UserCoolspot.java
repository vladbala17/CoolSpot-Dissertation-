package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 13.06.2017.
 */

public class UserCoolspot {

    private String name;
    private int hits;
    private long timestamp;
    private String coolpointFirst;
    private String coolpointSecond;

    public UserCoolspot() {
    }

    public UserCoolspot(String name, int hits, long timestamp, String coolpointFirst, String coolpointSecond) {
        this.name = name;
        this.hits = hits;
        this.timestamp = timestamp;
        this.coolpointFirst = coolpointFirst;
        this.coolpointSecond = coolpointSecond;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCoolpointFirst() {
        return coolpointFirst;
    }

    public void setCoolpointFirst(String coolpointFirst) {
        this.coolpointFirst = coolpointFirst;
    }

    public String getCoolpointSecond() {
        return coolpointSecond;
    }

    public void setCoolpointSecond(String coolpointSecond) {
        this.coolpointSecond = coolpointSecond;
    }
}
