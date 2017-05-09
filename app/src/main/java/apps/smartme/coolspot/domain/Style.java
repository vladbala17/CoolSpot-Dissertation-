package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 04.05.2017.
 */

public class Style {
    private int styleImage;
    private String styleName;

    public Style(String styleName, int styleImage) {
        this.styleImage = styleImage;
        this.styleName = styleName;
    }

    public int getStyleImage() {
        return styleImage;
    }

    public void setStyleImage(int styleImage) {
        this.styleImage = styleImage;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
}
