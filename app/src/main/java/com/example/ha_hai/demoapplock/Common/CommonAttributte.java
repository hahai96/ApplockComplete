package com.example.ha_hai.demoapplock.Common;

import android.app.Activity;
import android.content.Context;

import com.example.ha_hai.demoapplock.model.AppDao;
import com.example.ha_hai.demoapplock.model.DaoSession;
import com.example.ha_hai.demoapplock.util.ApplicationUlti;

/**
 * Created by ha_hai on 8/28/2018.
 */

public class CommonAttributte {

    public static final String QUESTION = "question";
    public static final String PASSWORD = "password";
    public static final String ISLAUNCHING = "isLaunching";
    public static final String NAME_PREFERENCE = "my_data";

    public static AppDao getAppDao(Activity context) {
        DaoSession daoSession = ((ApplicationUlti) context.getApplication()).getDaoSession();
        AppDao appDao = daoSession.getAppDao();
        return appDao;
    }
}
