package com.example.ha_hai.demoapplock;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.anthonyfdev.dropdownview.DropDownView;
import com.example.ha_hai.demoapplock.Common.CommonAttributte;
import com.example.ha_hai.demoapplock.adapter.ApplockRecyclerAdapter;
import com.example.ha_hai.demoapplock.model.App;
import com.example.ha_hai.demoapplock.model.AppDao;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.example.ha_hai.demoapplock.Common.CommonAttributte.ISLAUNCHING;
import static com.example.ha_hai.demoapplock.Common.CommonAttributte.NAME_PREFERENCE;
import static com.example.ha_hai.demoapplock.util.ConvertDrawableToBitmap.convertToBitmap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private ApplockRecyclerAdapter adapter;
    private DropDownView dropDownView;
    private View collapsedView, expandedView;
    private SearchView searchView;
    private SharedPreferences preferences;
    private PackageManager packageManager;
    private AppDao appDao;
    private List<App> applist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupLayout();

        setData();
    }

    private void setData() {
        packageManager = getPackageManager();
        applist = new ArrayList<>();

        dropDownView.setHeaderView(collapsedView);
        dropDownView.setExpandedView(expandedView);

        collapsedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownView.isExpanded()) {
                    dropDownView.collapseDropDown();
                } else {
                    dropDownView.expandDropDown();
                }
            }
        });

        dropDownView.setDropDownListener(dropDownListener);

        //init DaoSession
        appDao = CommonAttributte.getAppDao(this);

        preferences = getSharedPreferences(NAME_PREFERENCE, MODE_PRIVATE);
        boolean isLaunching = preferences.getBoolean(ISLAUNCHING, true);

        if (isLaunching) {
            new LoadApplications().execute();
        } else {
            queryDbSortByNameAToZ(AppDao.Properties.Name);
            setAdapter();
        }
    }

    private void queryDb(Property columnName) {
        Query appQuery = appDao.queryBuilder().orderDesc(columnName).build();
        applist = appQuery.list();

        if (adapter != null)
            adapter.setItems(applist);
    }

    private void queryDbSortByNameAToZ(Property columnName) {
        Query appQuery = appDao.queryBuilder().orderAsc(columnName).build();
        applist = appQuery.list();

        if (adapter != null)
            adapter.setItems(applist);
    }

    private void setupLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        collapsedView = LayoutInflater.from(this).inflate(R.layout.layout_collapsed_view, null, false);
        expandedView = LayoutInflater.from(this).inflate(R.layout.layout_expanded_view, null, false);

        expandedView.findViewById(R.id.lnSortByNameAToZ).setOnClickListener(this);
        expandedView.findViewById(R.id.lnSortByNameZToA).setOnClickListener(this);
        expandedView.findViewById(R.id.lnSortByInstallationTime).setOnClickListener(this);
        expandedView.findViewById(R.id.lnSortByAreLocking).setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        dropDownView = findViewById(R.id.drop_down_view);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem itemSearch = menu.findItem(R.id.search_view);
        searchView = (SearchView) itemSearch.getActionView();
        //set OnQueryTextListener cho search view để thực hiện search theo text
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.it_unLockAll) {
            for (App app : applist) {
                app.setState(0);
            }
            adapter.notifyDataSetChanged();
            appDao.updateInTx(applist);
        }

        if (id == R.id.it_lockAll) {
            for (App app : applist) {
                app.setState(1);
            }
            adapter.notifyDataSetChanged();
            appDao.updateInTx(applist);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_app_lock) {
            // Handle the camera action
        } else if (id == R.id.nav_photo_video_vault) {
            startActivity(new Intent(MainActivity.this, LockImageActivity.class));
        } else if (id == R.id.nav_background) {
            showColorPickerDialog();
        } else if (id == R.id.nav_theme) {

        } else if (id == R.id.nav_fake_cover) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnSortByNameAToZ:
                queryDbSortByNameAToZ(AppDao.Properties.Name);
                setTextHeader("Sort by app name (A->Z)");
                break;
            case R.id.lnSortByNameZToA:
                queryDb(AppDao.Properties.Name);
                setTextHeader("Sort by app name (Z->A)");
                break;
            case R.id.lnSortByInstallationTime:
                queryDb(AppDao.Properties.InstalledTime);
                setTextHeader("Sort by installation time");
                break;
            case R.id.lnSortByAreLocking:
                queryDb(AppDao.Properties.State);
                setTextHeader("Sort by apps are locking");
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.setIconified(true);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<App> items = appDao.queryBuilder().where(AppDao.Properties.Name.like("%" + newText + "%")).orderAsc(AppDao.Properties.Name).list();
        adapter.setItems(items);

        return true;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            applist = checkForLaunchIntent(packageManager.getInstalledApplications
                    (PackageManager.GET_META_DATA));
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //save state launching app
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(ISLAUNCHING, false);
            editor.commit();

            setAdapter();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private List<App> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<App> applist = new ArrayList<>();
        for (ApplicationInfo info : list) {
            try {

                Drawable icon = info.loadIcon(packageManager);
                String name = info.loadLabel(packageManager).toString();
                String packageName = info.packageName;
                Bitmap bitmap = convertToBitmap(icon, 48, 48);
                long time = packageManager.getPackageInfo(packageName, 0).firstInstallTime;

                if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    App app = new App(name, packageName, bitmap, 0, time);
                    applist.add(app);
                    appDao.insert(app);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collections.sort(applist, new Comparator<App>() {
            @Override
            public int compare(App app, App t1) {
                return app.getName().compareToIgnoreCase(t1.getName());
            }
        });

        return applist;
    }

    private final DropDownView.DropDownListener dropDownListener = new DropDownView.DropDownListener() {
        @Override
        public void onExpandDropDown() {
            ObjectAnimator.ofFloat(collapsedView.findViewById(R.id.imgArrow), View.ROTATION.getName(), 180).start();
        }

        @Override
        public void onCollapseDropDown() {
            ObjectAnimator.ofFloat(collapsedView.findViewById(R.id.imgArrow), View.ROTATION.getName(), -180, 0).start();
        }
    };

    private void setAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ApplockRecyclerAdapter(applist, this);
        recyclerView.setAdapter(adapter);
    }

    private void setTextHeader(String textHeader) {
        TextView txtHeaderDropDown = collapsedView.findViewById(R.id.txtCollapsed);
        txtHeaderDropDown.setText(textHeader);
        dropDownView.collapseDropDown();
    }

    private void showColorPickerDialog() {
        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("ColorPicker Dialog");
        builder.setPreferenceName("MyColorPickerDialog");
        builder.setFlagView(new CustomFlag(this, R.layout.layout_flag));
        builder.setPositiveButton("confirm", new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM, new int[] {colorEnvelope.getColor(), colorEnvelope.getColor() + 1000});
                gd.setCornerRadius(0f);

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}