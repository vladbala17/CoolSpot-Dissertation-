package apps.smartme.coolspot.helpers;

import java.util.Comparator;

import apps.smartme.coolspot.domain.Category;

/**
 * Created by vlad on 03.07.2017.
 */

public class CategoryComparator implements Comparator<Category> {
    @Override
    public int compare(Category c1, Category c2) {
        if (c1.getCount() > c2.getCount()) {
            return -1;
        } else if (c1.getCount() < c2.getCount()) {
            return 1;
        }
        return 0;
    }
}
