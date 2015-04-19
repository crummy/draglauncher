package com.malcolmcrum.draglauncher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * View for selectable menu items
 * Created by Malcolm Crum on 3/22/2015.
 */
public class DragMenuItemView extends View {
    private int size;
    private int spacing;
    private static @IdRes int lastID = 6969;
    private Paint textPaint;
    public DragMenuItemView north, east, south, west;
    private DragMenu.Item item;

    public DragMenuItemView(Context context, DragMenu.Item item) {
        super(context);
        size = 128;
        spacing = 64;
        this.item = item;

        lastID++;
        this.setId(lastID);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(72);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public int getSize() {
        return size;
    }

    public int getSpacing() {
        return spacing;
    }

    public void hide() {
        setVisibility(View.INVISIBLE);
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    public void pressed() {
        size *= 2;
        invalidate();
        if (north != null) north.show();
        if (east != null) east.show();
        if (south != null) south.show();
        if (west != null) west.show();
    }

    public void released() {
        size *= 0.5;
        invalidate();
        if (north != null) north.hide();
        if (east != null) east.hide();
        if (south != null) south.hide();
        if (west != null) west.hide();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(item.label != null && textPaint != null) {
            canvas.drawText(item.label, size*0.5f, size*0.75f, textPaint);
        }
    }

}
