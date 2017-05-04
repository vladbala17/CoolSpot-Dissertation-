package apps.smartme.coolspot.domain;

/**
 * Created by vlad on 01.05.2017.
 */

public class User {
    private String name;
    private int age;
    private String like;
    private String locations;
    private String style;

    public User(String name, int age, String like, String locations, String style) {
        this.name = name;
        this.age = age;
        this.like = like;
        this.locations = locations;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
