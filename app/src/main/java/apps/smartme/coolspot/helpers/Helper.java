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
    public static final String COOLPOINT_DRINK = "CoolpointDrink";
    public static final String COOLPOINT_SPORT = "CoolpointSport";
    public static final String COOLPOINT_CHEAP = "CoolpointCheap";
    public static final String COOLPOINT_COMPUTER = "CoolpointComputer";
    public static final String COOLPOINT_EXPENSIVE = "CoolpointExpensive";


    private List<String> likesList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    public Helper(List<String> likesList) {
        this.likesList = likesList;
        initializeCategoryList();

        splitLikesToCategory();

    }

    private void initializeCategoryList() {
        categoryList.add(new Category(COOLPOINT_DRINK, 0));
        categoryList.add(new Category(COOLPOINT_SPORT, 0));
        categoryList.add(new Category(COOLPOINT_CHEAP, 0));
        categoryList.add(new Category(COOLPOINT_COMPUTER, 0));
        categoryList.add(new Category(COOLPOINT_EXPENSIVE, 0));
    }


    private void splitLikesToCategory() {
        for (String like : likesList) {
            countCategory(like);
        }

        Log.d(TAG, "unsorted list is" + categoryList.toString());
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


    public String coolpointCategory() {
        Collections.sort(categoryList, new CategoryComparator());
        Log.d(TAG, "preffered category is" + categoryList.get(0).getName());
        return categoryList.get(0).getName();
    }
}
