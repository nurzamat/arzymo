package org.ananasit.rekordo.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;

import org.ananasit.rekordo.R;
import org.ananasit.rekordo.model.Category;
import org.ananasit.rekordo.model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class Utils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }

    public static int getActionTypeValue(ActionType _actionType)
    {
        int x = 0;

        switch (_actionType)
        {
            case NONE: x = 0;
                break;
            case BUY: x = 1;
                break;
            case SELL: x = 2;
                break;
            case RENT_GET: x = 3;
                break;
            case RENT_GIVE: x = 4;
                break;
            case RESUME: x = 5;
                break;
            case VACANCY: x = 6;
                break;
        }

      return x;
    }

    public static ActionType getActionTypeByValue(int x)
    {
        ActionType action = null;

        switch (x)
        {
            case 0: action = ActionType.NONE;
                break;
            case 1: action = ActionType.BUY;
                break;
            case 2: action = ActionType.SELL;
                break;
            case 3: action = ActionType.RENT_GET;
                break;
            case 4: action = ActionType.RENT_GIVE;
                break;
            case 5: action = ActionType.RESUME;
                break;
            case 6: action = ActionType.VACANCY;
                break;
        }

        return action;
    }

    public static CategoryType getCategoryType(Category category)
    {
        CategoryType categoryType = null;

        if(category == null)
             return null;

        String idCategory;
        if(!category.getIdParent().equals(""))
        {
            idCategory = category.getIdParent();
        }
        else idCategory = category.getId();

        switch (idCategory)
        {
            case "0": categoryType = CategoryType.NONE;
                break;
            case "1": categoryType = CategoryType.SELL_BUY;
                break;
            case "2": categoryType = CategoryType.SERVICES;
                break;
            case "3": categoryType = CategoryType.RENT;
                break;
            case "4": categoryType = CategoryType.WORK;
                break;
            case "5": categoryType = CategoryType.REST;
                break;
            case "6": categoryType = CategoryType.EVENTS;
                break;
            case "7": categoryType = CategoryType.BUSSINES;
                break;
            case "8": categoryType = CategoryType.DATING;
                break;
        }

        return categoryType;
    }

    public static String getParams(String query, int actionTypeValue)
    {
        if(query.equals("") || query.equals("0") || query.contains(";"))
            query = "0";
        else
        {
            try
            {
                query = URLEncoder.encode(query, "UTF-8");
            }
            catch (UnsupportedEncodingException ex)
            {
                ex.printStackTrace();
            }
        }

        return query + ";" + actionTypeValue;
    }

    public static Category getCategoryByIds(String id_category, String id_subcategory)
    {
        Category category = null;
        try
        {
            for(Iterator<Category> i = GlobalVar._categories.iterator(); i.hasNext(); ) {
                Category item = i.next();

                if(!item.getIdParent().equals(""))
                {
                    if(item.getIdParent().equals(id_category) && item.getId().equals(id_subcategory))
                    {
                        category = item;
                    }
                }
                else
                {
                    if(item.getId().equals(id_category))
                    {
                        category = item;
                    }
                }
            }
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
        }

        return category;
    }

    public static boolean isConnected(Context context)
    {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
