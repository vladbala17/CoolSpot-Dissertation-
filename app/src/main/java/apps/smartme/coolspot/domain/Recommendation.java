package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 03.07.2017.
 */

public class Recommendation {

    private String coolspotName;
    private String coolpoint;
    private int popularity;
    private int mutualFriendsNo;

    public Recommendation() {
    }

    public Recommendation(String coolspotName, String coolpoint, int popularity, int mutualFriendsNo) {
        this.coolspotName = coolspotName;
        this.coolpoint = coolpoint;
        this.popularity = popularity;
        this.mutualFriendsNo = mutualFriendsNo;
    }

    public String getCoolspotName() {
        return coolspotName;
    }

    public void setCoolspotName(String coolspotName) {
        this.coolspotName = coolspotName;
    }

    public String getCoolpoint() {
        return coolpoint;
    }

    public void setCoolpoint(String coolpoint) {
        this.coolpoint = coolpoint;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getMutualFriendsNo() {
        return mutualFriendsNo;
    }

    public void setMutualFriendsNo(int mutualFriendsNo) {
        this.mutualFriendsNo = mutualFriendsNo;
    }
}
