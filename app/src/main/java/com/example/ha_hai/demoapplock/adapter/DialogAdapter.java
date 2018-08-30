package com.example.ha_hai.demoapplock.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ha_hai.demoapplock.LockImageActivity;
import com.example.ha_hai.demoapplock.R;
import com.example.ha_hai.demoapplock.interfaces.DialogClickListener;

import java.util.List;

/**
 * Created by ha_hai on 8/30/2018.
 */
public class DialogAdapter extends BaseAdapter {
    private Context context;
    private List<String> items;
    private LayoutInflater mInflater;
    private DialogClickListener mDialogClickListener;

    private static final int TYPE_LINE = 0;
    private static final int TYPE_ITEM = 1;


    public DialogAdapter(@NonNull Context context, DialogClickListener dialogClickListener, @NonNull List<String> items) {
        this.context = context;
        this.items = items;
        this.mDialogClickListener = dialogClickListener;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MyHolder myHolder;
        int type = getItemViewType(position);
        if (convertView == null) {
            myHolder = new MyHolder();
            switch (type) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.item_row_top_dialog, null);
                    myHolder.txtSetting = convertView.findViewById(R.id.txtSetting);
                    break;
                case TYPE_LINE:
                    convertView = mInflater.inflate(R.layout.item_row_line, null);
                    break;
            }
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        if (type == TYPE_ITEM)
            myHolder.txtSetting.setText(items.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogClickListener.onDialogClick(position);
            }
        });

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).equals("l") ? TYPE_LINE : TYPE_ITEM;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class MyHolder {

        private TextView txtSetting;

    }
}