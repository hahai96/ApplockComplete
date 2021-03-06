package com.example.ha_hai.demoapplock.util;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ha_hai.demoapplock.R;
import com.example.ha_hai.demoapplock.interfaces.SectionCallback;

/**
 * Created by paetztm on 2/6/2017.
 */

public class RecyclerItemDecorationForImage extends RecyclerView.ItemDecoration {

    private final int headerOffset;
    private final boolean sticky;
    private final SectionCallback sectionCallback;

    private View headerView;
    private TextView header;

    public RecyclerItemDecorationForImage(int headerHeight, boolean sticky, @NonNull SectionCallback sectionCallback) {
        headerOffset = headerHeight;
        this.sticky = sticky;
        this.sectionCallback = sectionCallback;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

//        int pos = parent.getChildAdapterPosition(view);
//        if (!sectionCallback.isImageType(pos)) {
//            outRect.top = headerOffset;
//        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (headerView == null) {
            headerView = inflateHeaderView(parent);
            header = headerView.findViewById(R.id.txtHeader);
            fixLayoutSize(headerView, parent);
        }

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);

            String title = sectionCallback.getSectionHeader(position);
            header.setText(title);
            if (sectionCallback.isImageType(position)) {
                drawHeader(c, child, headerView);
            }
        }
    }

    private void drawHeader(Canvas c, View child,  View headerView) {
        c.save();
        if (child.getBottom() > 0 && (child.getTop() - headerView.getHeight()) < 0) {
            Log.d("AAA", "getTop: " + child.getTop() + " - getBottom: " + child.getBottom() );
            c.translate(0, Math.max(0, child.getTop() - headerView.getHeight()));
        }
//        if (sticky) {
//            Log.d("AAA", "getTop: " + child.getTop() );
//            c.translate(0, Math.max(0, child.getTop()));
//        }
//        else {
//            c.translate(0, child.getTop() - headerView.getHeight());
//        }
        headerView.draw(c);
        c.restore();
    }

    private View inflateHeaderView(RecyclerView parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_section_header, parent, false);
    }

    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */
    private void fixLayoutSize(View view, ViewGroup parent) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(),
                View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(),
                View.MeasureSpec.UNSPECIFIED);

        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                parent.getPaddingLeft() + parent.getPaddingRight(),
                view.getLayoutParams().width);
        int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                parent.getPaddingTop() + parent.getPaddingBottom(),
                view.getLayoutParams().height);

        view.measure(childWidth, childHeight);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }


}
