package com.hishd.qurantime.Util;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private  int top;
    private  int bottom;
    private  int left;
    private  int right;

    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int top, @DimenRes int bottom, @DimenRes int left, @DimenRes int right) {
        this.top = context.getResources().getDimensionPixelSize(top);
        this.bottom = context.getResources().getDimensionPixelSize(bottom);
        this.left = context.getResources().getDimensionPixelSize(left);
        this.right = context.getResources().getDimensionPixelSize(right);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(left, top, right, bottom);
    }
}