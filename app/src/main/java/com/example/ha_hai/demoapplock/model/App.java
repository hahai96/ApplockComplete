package com.example.ha_hai.demoapplock.model;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.example.ha_hai.demoapplock.util.ConvertBitmapToString;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by ha_hai on 8/26/2018.
 */

@Entity (nameInDb = "app")
public class App {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "package_name")
    private String packageName;

    @Property(nameInDb = "image")
    @Convert(converter = ConvertBitmapToString.class, columnType = String.class)
    private Bitmap image;

    @Property(nameInDb = "state")
    private int state;

    @Property(nameInDb = "installedTime")
    private long installedTime;

    @Generated(hash = 1976313250)
    public App(Long id, String name, String packageName, Bitmap image, int state,
            long installedTime) {
        this.id = id;
        this.name = name;
        this.packageName = packageName;
        this.image = image;
        this.state = state;
        this.installedTime = installedTime;
    }

    @Generated(hash = 407064589)
    public App() {
    }

    public App(String name, String packageName, Bitmap image, int state, long installedTime) {
        this.name = name;
        this.packageName = packageName;
        this.image = image;
        this.state = state;
        this.installedTime = installedTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @BindingAdapter({"profileImage"})
    public static void loadImage(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter({"lockImage"})
    public static void loadImage(CheckBox ck, int state) {
        if (state == 1)
            ck.setChecked(true);
        else
            ck.setChecked(false);
    }

    public long getInstalledTime() {
        return this.installedTime;
    }

    public void setInstalledTime(long installedTime) {
        this.installedTime = installedTime;
    }
}
