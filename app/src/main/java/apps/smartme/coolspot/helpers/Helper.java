package apps.smartme.coolspot.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import apps.smartme.coolspot.domain.Category;

/**
 * Created by vlad on 03.07.2017.
 */

public class Helper {

    private static final String TAG = Helper.class.getSimpleName();
    private static final String drinkList = "Captain Morgan Jack Daniels Bacardi Alcohol Long drink";
    private static final String sportList = "Rafael Nadal Roger Federer Sport Tennis Football Ball Adidas Puma Nike Wimbledon World Cup";
    private static final String cheapList = "China Free Discount Cheap Original eBay Amazon Ali";
    private static final String computerList = "Dell IT Android iOS nVidia Nerd Sony LG Computers Samsung Huawei";
    private static final String expensiveList = "Dell IT Android iOS nVidia Nerd Sony LG Computers Samsung Huawei";


    private List<String> likesList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    public Helper(List<String> likesList) {
        this.likesList = likesList;
        categoryList.add(new Category("Drink", 0));
        categoryList.add(new Category("Sport", 0));
        categoryList.add(new Category("Cheap", 0));
        categoryList.add(new Category("Computer", 0));
        categoryList.add(new Category("Expensive", 0));
        likesList.add("Captain");
        likesList.add("Jack Daniels");
        likesList.add("Jack Drink");
        likesList.add("sport");
        likesList.add("captain morgan");
        likesList.add("Rafael Nadal");
        likesList.add("Tennis");
        likesList.add("Ball");
        likesList.add("Roger Federer");
        splitLikesToCategory();

    }


    private void splitLikesToCategory() {
        for (String like : likesList) {
            countCategory(like);
        }

        Log.d(TAG, "unsorted list is" + categoryList.toString());
        coolpointCategory(categoryList);
    }

    private void countCategory(String like) {
        if (drinkList.contains(like)) {
            Log.d(TAG, like + " is contained in drink list");
            int drinkCount = categoryList.get(0).getCount();
            categoryList.get(0).setCount(drinkCount + 1);
        } else if (sportList.contains(like)) {
            int sportCount = categoryList.get(1).getCount();
            categoryList.get(1).setCount(sportCount + 1);
            Log.d(TAG, like + " is contained in sport list");
        } else if (cheapList.contains(like)) {
            int cheapCount = categoryList.get(2).getCount();
            categoryList.get(2).setCount(cheapCount + 1);
            Log.d(TAG, like + " is contained in cheap list");
        } else if (computerList.contains(like)) {
            int computerCount = categoryList.get(3).getCount();
            categoryList.get(3).setCount(computerCount + 1);
            Log.d(TAG, like + " is contained in computer list");
        } else if (expensiveList.contains(like)) {
            int expensiveCount = categoryList.get(4).getCount();
            categoryList.get(4).setCount(expensiveCount + 1);
            Log.d(TAG, like + " is contained in expensive list");
        } else {
            Log.d(TAG, like + " Could not be placed in category");
        }

    }


    private String coolpointCategory(List<Category> categoryList) {
        Collections.sort(categoryList, new CategoryComparator());
        Log.d(TAG, "preffered category is" + categoryList.get(0).getName());
        return categoryList.get(0).getName();
    }
}
