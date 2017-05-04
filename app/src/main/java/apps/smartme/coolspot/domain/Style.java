package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 04.05.2017.
 */

public class Style {
    private String name;

    public Style(String name) {
        this.name = name;
    }

    public Style() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
