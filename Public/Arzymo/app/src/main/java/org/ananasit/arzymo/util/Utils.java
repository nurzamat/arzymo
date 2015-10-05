package org.ananasit.arzymo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.Log;

import com.google.gson.Gson;
import org.ananasit.arzymo.R;
import org.ananasit.arzymo.model.User;

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

    public static void saveUserToPreferences(Context context, User user)
    {
        SharedPreferences sp = context.getSharedPreferences(Constants.ARZYMO, 0);
        SharedPreferences.Editor editor = sp.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        Log.i("saveUserToPreferences", "json: " + json);

        editor.putString(Constants.USER, json);
        editor.commit();
    }

    public static User getUserFromPreferences(Context context)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences(Constants.ARZYMO, 0);
            Gson gson = new Gson();
            String json = sp.getString(Constants.USER, "");
            Log.i("getUserFromPreferences", "json: " + json);
            User obj = gson.fromJson(json, User.class);
            return obj;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.i("getPref exception", "message: " + ex.getMessage());
            return null;
        }
    }
}
