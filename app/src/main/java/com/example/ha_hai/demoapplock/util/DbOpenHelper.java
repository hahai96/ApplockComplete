package com.example.ha_hai.demoapplock.util;

import android.content.Context;


import com.example.ha_hai.demoapplock.model.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ha_hai on 8/26/2018.
 */

public class DbOpenHelper extends DaoMaster.OpenHelper {

    Context mContext;

    public DbOpenHelper(Context context, String name) {
        super(context, name);
        this.mContext = context;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        switch (oldVersion) {}
    }
}
