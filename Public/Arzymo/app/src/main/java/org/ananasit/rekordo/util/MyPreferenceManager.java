package org.ananasit.rekordo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.ananasit.rekordo.model.User;


public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // All Shared Preferences Keys
    private static final String KEY_NOTIFICATIONS = "notifications";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Constants.ARZYMO, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void saveUser(User user)
    {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        Log.i("saveUserToPreferences", "json: " + json);

        editor.putString(Constants.USER, json);
        editor.commit();
    }

    public User getUser() {
        try
        {
            Gson gson = new Gson();
            String json = pref.getString(Constants.USER, "");
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

    public void saveToken(String gcm_token)
    {
        Log.i("saveTokenToPreferences", "gcm_token: " + gcm_token);
        editor.putString(Constants.GCM_TOKEN, gcm_token);
        editor.commit();
    }

    public String getToken()
    {
        return pref.getString(Constants.GCM_TOKEN, "");
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
