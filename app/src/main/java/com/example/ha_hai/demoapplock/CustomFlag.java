package com.example.ha_hai.demoapplock;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.FlagView;

/**
 * Created by ha_hai on 8/29/2018.
 */
public class CustomFlag extends FlagView {

    private TextView textView;
    private View view;

    public CustomFlag(Context context, int layout) {
        super(context, layout);
        textView = findViewById(R.id.flag_color_code);
        view = findViewById(R.id.flag_color_layout);
    }

    @Override
    public void onRefresh(ColorEnvelope colorEnvelope) {
        textView.setText("#" + String.format("%06X", (0xFFFFFF & colorEnvelope.getColor())));
        view.setBackgroundColor(colorEnvelope.getColor());
    }

}