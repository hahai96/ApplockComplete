package com.example.ha_hai.demoapplock.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ha_hai.demoapplock.R;

/**
 * Created by ha_hai on 8/30/2018.
 */

public class RecyclerItemDecorationForApp extends RecyclerView.ItemDecoration {

    private final int mSpaceHeight;
    private boolean mShowLastDivider;
    private Drawable mDivider;

    public RecyclerItemDecorationForApp(Context context, int spaceHeight, boolean showLastDivider) {
        this.mSpaceHeight = spaceHeight;
        mShowLastDivider = showLastDivider;

        mDivider = context.getResources().getDrawable(R.drawable.custom_item_decoration);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        boolean isLastItem = parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1;
        if (!isLastItem || mShowLastDivider) {
            outRect.bottom = mSpaceHeight;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            int left = parent.getPaddingLeft() + child.getPaddingLeft() + params.leftMargin;
            int right = parent.getWidth() - left;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
