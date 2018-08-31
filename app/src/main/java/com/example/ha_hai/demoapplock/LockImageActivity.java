package com.example.ha_hai.demoapplock;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ha_hai.demoapplock.Common.CommonAttributte;
import com.example.ha_hai.demoapplock.adapter.ImageAdapter;
import com.example.ha_hai.demoapplock.interfaces.SectionCallback;
import com.example.ha_hai.demoapplock.model.Image;
import com.example.ha_hai.demoapplock.model.ImageDao;
import com.example.ha_hai.demoapplock.util.RecyclerItemDecorationForImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import org.greenrobot.greendao.query.Query;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LockImageActivity extends AppCompatActivity {

    private ArrayList<Image> images;
    private RecyclerView recyclerView;
    private ImageDao imageDao;
    private Query query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_image);

        recyclerView = findViewById(R.id.recyclerView);

        imageDao = CommonAttributte.get(LockImageActivity.this).getImageDao();

        query = imageDao.queryBuilder().build();

        requestPermission();
    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
                        setAdapter();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LockImageActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void setAdapter() {
        //get images from db
        //get images from device

        images = (ArrayList<Image>) query.list();

        if (images == null || images.size() == 0)
            getAllShownImagesPath(LockImageActivity.this);

        ArrayList<Image> deleteItems = (ArrayList<Image>) imageDao.queryBuilder().where(ImageDao.Properties.Time.like("%70")).list();
        for (Image image: deleteItems) {
            imageDao.delete(image);
        }

        images = (ArrayList<Image>) query.list();

        recyclerView.setHasFixedSize(true);
        final SectionCallback sectionCallback = getSectionCallback(images);
        RecyclerItemDecorationForImage itemDecoration = new RecyclerItemDecorationForImage(getResources().getDimensionPixelSize(R.dimen.recycler_section_header_height),
                true,
                sectionCallback);
        recyclerView.addItemDecoration(itemDecoration);
        GridLayoutManager layoutManager = new GridLayoutManager(LockImageActivity.this, 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (!sectionCallback.isImageType(position)) {
                    return 4;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        ImageAdapter adapter = new ImageAdapter(images, LockImageActivity.this, sectionCallback);
        recyclerView.setAdapter(adapter);
    }

    private void getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_ADDED};

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        ArrayList<String> listTime = new ArrayList<>();
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            File file = new File(absolutePathOfImage);
            Date lastModDate = new Date(file.lastModified());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
            String time = dateFormat.format(lastModDate);

            if (!listTime.contains(time)) {
                listTime.add(time);
                Image image = new Image(null, time);
                imageDao.insert(image);
            }

            //insert to db
            Image image = new Image(absolutePathOfImage, time);
            imageDao.insert(image);
        }
    }
    private SectionCallback getSectionCallback(final List<Image> images) {
        return new SectionCallback() {

            @Override
            public boolean isImageType(int position) {
                if (images.get(position).getPath() == null) {
                    return false;
                }
                return true;
            }

            @Override
            public String getSectionHeader(int position) {
                return images.get(position).getTime();

            }
        };
    }
}
