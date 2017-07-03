package apps.smartme.coolspot.domain;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by vlad on 03.07.2017.
 */

public class Category{
    private String name;
    private int count;

    public Category(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }



}
