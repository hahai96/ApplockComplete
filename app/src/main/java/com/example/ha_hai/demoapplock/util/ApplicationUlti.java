package com.example.ha_hai.demoapplock.util;

import android.app.Application;

import com.example.ha_hai.demoapplock.model.DaoMaster;
import com.example.ha_hai.demoapplock.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ha_hai on 8/26/2018.
 */

public class ApplicationUlti extends Application {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DbOpenHelper helper = new DbOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
