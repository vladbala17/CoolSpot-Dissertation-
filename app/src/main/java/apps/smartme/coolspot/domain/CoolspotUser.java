package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 13.06.2017.
 */

public class CoolspotUser {
    private String name;
    private boolean present;

    public CoolspotUser(String name, boolean present) {
        this.name = name;
        this.present = present;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
