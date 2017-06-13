package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 13.06.2017.
 */

public class CoolspotCoolpoint {
    private String name;
    private String coolpointFirst;
    private String coolpointSecond;
    private long timestamp;

    public CoolspotCoolpoint() {
    }

    public CoolspotCoolpoint(String name, String coolpointFirst, String coolpointSecond, long timestamp) {
        this.name = name;
        this.coolpointFirst = coolpointFirst;
        this.coolpointSecond = coolpointSecond;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
