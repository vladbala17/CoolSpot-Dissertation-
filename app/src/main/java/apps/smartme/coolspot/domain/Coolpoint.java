package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 04.05.2017.
 */

public class Coolpoint {
    private int popularity;
    private String pointName;

    public Coolpoint(String styleName, int popularity) {
        this.popularity = popularity;
        this.pointName = styleName;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }
}
