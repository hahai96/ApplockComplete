package com.example.ha_hai.demoapplock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ha_hai.demoapplock.R;
import com.example.ha_hai.demoapplock.model.Image;

import java.io.File;
import java.util.List;



/**
 * Created by ha_hai on 8/29/2018.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyHolder> {
    private Context mContext;
    private List<Image> items;
    private boolean isVisible;

    public ImageAdapter(List<Image> items, Context context) {
        this.items = items;
        this.mContext = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent,
                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_image, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        String path = items.get(position).getPath();

        File file = new File(path);
//        Picasso.get().load(file).resize(200, 200).error(R.drawable.ic_launcher_background).into(holder.imgGallery);
        Glide.with(mContext).load(file).placeholder(R.drawable.ic_launcher_background).into(holder.imgGallery);
        if (isVisible)
            holder.ckImage.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
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
                    Toast.makeText(mContext, "long click", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, ckImage.isChecked() + " + " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

                    if (ckImage.isChecked()) {
                        ckImage.setChecked(false);
                        ckImage.setVisibility(View.INVISIBLE);
                    } else {
                        ckImage.setVisibility(View.VISIBLE);
                        ckImage.setChecked(true);
                    }
                }
            });
        }
    }
}