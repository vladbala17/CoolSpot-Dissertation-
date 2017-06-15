package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 13.06.2017.
 */

public class CoolspotCoolpoint {
    private String coolpointFirst;
    private String coolpointSecond;

    public CoolspotCoolpoint() {
    }

    public CoolspotCoolpoint(String coolpointFirst, String coolpointSecond) {

        this.coolpointFirst = coolpointFirst;
        this.coolpointSecond = coolpointSecond;

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
