package com.example.ha_hai.demoapplock.Common;

import android.app.Activity;
import android.content.Context;

import com.example.ha_hai.demoapplock.model.AppDao;
import com.example.ha_hai.demoapplock.model.DaoSession;
import com.example.ha_hai.demoapplock.model.ImageDao;
import com.example.ha_hai.demoapplock.util.ApplicationUlti;

/**
 * Created by ha_hai on 8/28/2018.
 */

public class CommonAttributte {

    private static DaoSession daoSession;

    public static final String QUESTION = "question";
    public static final String PASSWORD = "password";
    public static final String ISLAUNCHING = "isLaunching";
    public static final String NAME_PREFERENCE = "my_data";

//    public static AppDao getAppDao(Activity context) {
//        DaoSession daoSession = ((ApplicationUlti) context.getApplication()).getDaoSession();
//        AppDao appDao = daoSession.getAppDao();
//        return appDao;
//    }

    public static DaoSession get(Activity context) {
        if (daoSession == null) {
            daoSession = ((ApplicationUlti) context.getApplication()).getDaoSession();
        }
        return daoSession;
    }


    public static AppDao getAppDao() {
        AppDao appDao = daoSession.getAppDao();
        return appDao;
    }

    public static ImageDao getImageDao() {
        ImageDao imageDao = daoSession.getImageDao();
        return imageDao;
    }
}
