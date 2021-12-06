package com.funambol.funeval;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.List;


@SuppressLint("RestrictedApi")
public class IndexedView extends AppCompatImageView {

    private int index, target;
    private List<Bitmap> indexDraw;

    public IndexedView(@NonNull Context context) {
        super(context);
    }

    public IndexedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexedView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int swapIndex(int newIndex) {
        int oldIndex = index;
        setImageBitmap(indexDraw.get(newIndex - 1));
        index = newIndex;
        return oldIndex;
    }

    public boolean isLocked() {
        return index == target;
    }

    public int getIndex() {
        return index;
    }

    public IndexedView setIndex(int index) {
        setImageBitmap(indexDraw.get(index - 1));
        this.index = index;
        return this;
    }

    public List<Bitmap> getIndexDraw() {
        return indexDraw;
    }

    public IndexedView setIndexDraw(List<Bitmap> indexDraw) {
        this.indexDraw = indexDraw;
        return this;
    }

    public void setTarget(int target) {
        this.target = target;
    }


}
