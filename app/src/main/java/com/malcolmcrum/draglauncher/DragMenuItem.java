package com.malcolmcrum.draglauncher;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Menu object
 * Created by Malcolm Crum on 3/22/2015.
 */
public class DragMenuItem {
    // Connections should be one way only!!
    public DragMenuItem east = null;
    public DragMenuItem west = null;
    public DragMenuItem north = null;
    public DragMenuItem south = null;

    public String label = "?!";
    public Rect area;

    private boolean selected = false;
    private boolean root;

    private static final Paint textPaint = new Paint();
    private static final Paint backgroundPaint = new Paint();
    private static final int sizeDeselected = 128;
    private static final int sizeSelected = 196;
    private static final int spacing = 256;

    static {
        backgroundPaint.setColor(Color.BLUE);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(72);
    }

    public DragMenuItem() {
        this(false);
    }

    public DragMenuItem(boolean isRoot) {
        root = isRoot;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
        if (north != null) north.deselect();
        if (east != null) east.deselect();
        if (south != null) south.deselect();
        if (west != null) west.deselect();
    }

    public Paint getTextPaint() {
        return textPaint;
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public int getSize() {
        return isSelected() ? sizeSelected : sizeDeselected;
    }

    public int getSpacing() {
        return spacing;
    }

}