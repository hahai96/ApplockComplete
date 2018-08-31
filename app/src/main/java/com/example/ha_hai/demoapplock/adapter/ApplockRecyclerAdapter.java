package com.example.ha_hai.demoapplock.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ha_hai.demoapplock.Common.CommonAttributte;
import com.example.ha_hai.demoapplock.R;
import com.example.ha_hai.demoapplock.databinding.ItemRowBinding;
import com.example.ha_hai.demoapplock.model.App;

import java.util.List;


/**
 * Created by ha_hai on 8/27/2018.
 */
public class ApplockRecyclerAdapter extends RecyclerView.Adapter<ApplockRecyclerAdapter.MyHolder> {
    private Activity mActivity;
    private List<App> items;
    private PackageManager packageManager;
    private LayoutInflater layoutInflater;

    public ApplockRecyclerAdapter(List<App> items, Activity activity) {
        this.items = items;
        this.mActivity = activity;
        packageManager = activity.getPackageManager();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_row, parent, false);

        return new MyHolder(binding);
    }


    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        App app = items.get(position);
        holder.binding.setApp(app);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public void setItems(List<App> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final ItemRowBinding binding;

        public MyHolder(ItemRowBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.binding = itemRowBinding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateAppClickRootView(binding, getAdapterPosition());
                }
            });

            binding.imglock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateAppClickCheckBox(binding, getAdapterPosition());
                }
            });

        }
    }

    private void updateAppClickRootView(ItemRowBinding binding, int position) {
        App app = items.get(position);
        if (binding.imglock.isChecked()) {
            binding.imglock.setChecked(false);
            app.setState(0);
        } else {
            binding.imglock.setChecked(true);
            app.setState(1);
        }
//        CommonAttributte.getAppDao(mActivity).update(app);
        CommonAttributte.get(mActivity).getAppDao().update(app);
    }

    private void updateAppClickCheckBox(ItemRowBinding binding, int position) {
        App app = items.get(position);
        if (binding.imglock.isChecked()) {
            binding.imglock.setChecked(true);
            app.setState(1);
        } else {
            binding.imglock.setChecked(false);
            app.setState(0);
        }
//        CommonAttributte.getAppDao(mActivity).update(app);
        CommonAttributte.get(mActivity).getAppDao().update(app);
    }
}