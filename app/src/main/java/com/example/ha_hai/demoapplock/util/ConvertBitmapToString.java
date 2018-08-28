package com.example.ha_hai.demoapplock.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.ByteArrayOutputStream;

/**
 * Created by ha_hai on 8/27/2018.
 */

public class ConvertBitmapToString implements PropertyConverter<Bitmap, String> {

    @Override
    public Bitmap convertToEntityProperty(String databaseValue) {
        byte[] decodedString = Base64.decode(databaseValue, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    @Override
    public String convertToDatabaseValue(Bitmap entityProperty) {
        String imgString = Base64.encodeToString(getBytesFromBitmap(entityProperty), Base64.DEFAULT);
        return imgString;
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return stream.toByteArray();
    }

}