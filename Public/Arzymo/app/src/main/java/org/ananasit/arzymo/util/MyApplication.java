package org.ananasit.arzymo.util;

import android.app.Application;

import org.ananasit.arzymo.model.User;

/**
 * Created by nurzamat on 5/14/15.
 */
public class MyApplication extends Application
{
    public static MyApplication _Instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _Instance=this;

    }

    private User _user = null;

    public User getUser()
    {
        return _user;
    }

    public void setUser(User user)
    {
        this._user = user;
    }
}
