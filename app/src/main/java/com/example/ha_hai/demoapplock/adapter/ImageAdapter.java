package com.example.ha_hai.demoapplock.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ha_hai.demoapplock.Common.CommonAttributte;
import com.example.ha_hai.demoapplock.ImageDetailActivity;
import com.example.ha_hai.demoapplock.R;
import com.example.ha_hai.demoapplock.interfaces.SectionCallback;
import com.example.ha_hai.demoapplock.model.Image;

import java.io.File;
import java.util.List;



/**
 * Created by ha_hai on 8/29/2018.
 */
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Image> items;
    private boolean isVisible;
    private SectionCallback sectionCallback;

    private static final int IMAGE_TYPE = 0;
    private static final int HEADER_TYPE = 1;

    public ImageAdapter(List<Image> items, Context context, @NonNull SectionCallback sectionCallback) {
        this.items = items;
        this.mContext = context;
        this.sectionCallback = sectionCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        if (viewType == IMAGE_TYPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_image, parent, false);
            return new MyHolder(v);
        } else if (viewType == HEADER_TYPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_section_header, parent, false);
            return new HeaderViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == IMAGE_TYPE) {
            String path = items.get(position).getPath();

            File file = new File(path);
            if (file == null) {
                CommonAttributte.get((Activity) mContext).getImageDao().delete(items.get(position));
                items.remove(position);
                notifyItemRemoved(position);
            }

            Glide.with(mContext).load(file).placeholder(R.drawable.ic_launcher_background).into(((MyHolder)holder).imgGallery);

            if (isVisible)
                ((MyHolder) holder).ckImage.setVisibility(View.VISIBLE);
        } else if (getItemViewType(position) == HEADER_TYPE) {
            ((HeaderViewHolder) holder).txtHeader.setText(items.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (sectionCallback.isImageType(position)) {
            return IMAGE_TYPE;
        }
        return HEADER_TYPE;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private ImageView imgGallery;
        private CheckBox ckImage;

        public MyHolder(View itemView) {
            super(itemView);

            imgGallery = itemView.findViewById(R.id.imgGallery);
            ckImage = itemView.findViewById(R.id.ckImage);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    isVisible = true;
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, ckImage.isChecked() + " + " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

//                    if (ckImage.isChecked()) {
//                        ckImage.setChecked(false);
//                        ckImage.setVisibility(View.INVISIBLE);
//                    } else {
//                        ckImage.setVisibility(View.VISIBLE);
//                        ckImage.setChecked(true);
//                    }

                    Toast.makeText(mContext, items.get(getAdapterPosition()).getTime() + " - path: " + items.get(getAdapterPosition()).getPath().substring(items.get(getAdapterPosition()).getPath().length() - 8), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, ImageDetailActivity.class);
                    intent.putExtra("path", items.get(getAdapterPosition()).getPath());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, imgGallery, "shareImage");
                    mContext.startActivity(intent, optionsCompat.toBundle());
                }
            });
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView txtHeader;
        Button btnSelectAll;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            txtHeader = itemView.findViewById(R.id.txtHeader);
            btnSelectAll = itemView.findViewById(R.id.btnSelectAll);
        }
    }
}