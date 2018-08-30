package com.example.ha_hai.demoapplock.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ha_hai on 8/29/2018.
 */

@Entity (nameInDb = "image")
public class Image {

    @Property (nameInDb = "path")
    private String path;

    @Property (nameInDb = "time")
    private String time;

    @Generated(hash = 1500677572)
    public Image(String path, String time) {
        this.path = path;
        this.time = time;
    }

    @Generated(hash = 1590301345)
    public Image() {
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
}
