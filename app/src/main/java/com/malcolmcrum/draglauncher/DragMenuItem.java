package com.malcolmcrum.draglauncher;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Menu object
 * TODO: Separate UI into separate class (or DragView?)
 * Created by Malcolm Crum on 3/22/2015.
 */
public class DragMenuItem {
    // Connections should be one way only!!
    private DragMenuItem east = null;
    private DragMenuItem west = null;
    private DragMenuItem north = null;
    private DragMenuItem south = null;
    // In other words, parent should not point to the same object as east, west, north, or south.
    private DragMenuItem parent = null;

    public String packageName = "com.malcolmcrum.draglauncher";

    private Rect areaDeselected;
    private Rect areaSelected;
    private Rect areaTouch; // A larger rect is used for touch detection.
    private Drawable icon;

    /* Three states:
     * notSelected, if a MenuItem has not been touched at all in the current touch
     * wasSelected, if a MenuItem was touched and is a parent of the currently touched MenuItem
     * isSelected, if a MenuItem is currently selected (i.e. finger is over it)
     */
    private enum SelectedState {notSelected, wasSelected, isSelected};
    private SelectedState selected = SelectedState.notSelected;
    private boolean root;

    private static final Paint textPaint = new Paint();
    private static final Paint paintNotSelected = new Paint();
    private static final Paint paintSelected = new Paint();
    private static final Paint paintWasSelected = new Paint();
    private static final int sizeNotSelected = 196;
    private static final int sizeWasSelected = 256;
    private static final int sizeSelected = 256;
    private static final int spacing = 256;

    static {
        paintSelected.setColor(Color.BLUE);
        paintNotSelected.setColor(Color.MAGENTA);
        paintWasSelected.setColor(Color.GRAY);
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

    /**
     * Generates layout information for the MenuItem. Must be called before draw calls.
     *
     * @param  x    x coordinate of the center of the MenuItem
     * @param  y    y coordinate of the center of the MenuItem
     */
    public void layout(int x, int y, PackageManager pm) {
        areaDeselected = new Rect(x - sizeNotSelected /2, y - sizeNotSelected /2, x + sizeNotSelected /2, y + sizeNotSelected /2);
        areaSelected = new Rect(x - sizeSelected/2, y - sizeSelected/2, x + sizeSelected/2, y + sizeSelected/2);
        areaTouch = new Rect(x - spacing/2, y - spacing/2, x + spacing/2, y + spacing/2);
        try {
            icon = pm.getApplicationIcon(packageName);
            icon.setBounds(areaSelected);
        } catch (PackageManager.NameNotFoundException e) {
            icon = null;
        }

        if (north != null) north.layout(x, y - spacing, pm);
        if (east != null) east.layout(x + spacing, y, pm);
        if (south != null) south.layout(x, y + spacing, pm);
        if (west != null) west.layout(x - spacing, y, pm);
    }

    public boolean isSelected() {
        return selected == SelectedState.isSelected;
    }

    public boolean wasSelected() {
        return selected == SelectedState.wasSelected;
    }

    public boolean isVisible() {
        if (root) return true;
        else if (parent != null && parent.isSelected()) return true; // TODO: simplify this?
        else return selected != SelectedState.notSelected;
    }

    public void select() {
        selected = SelectedState.isSelected;
        if (parent != null) parent.selected = SelectedState.wasSelected;
    }

    public void deselect() {
        selected = SelectedState.notSelected;
        deselectChildren();
    }

    public void deselectChildren() {
        if (north != null) north.deselect();
        if (east != null) east.deselect();
        if (south != null) south.deselect();
        if (west != null) west.deselect();
    }

    public Paint getBackgroundPaint() {
        if (isSelected()) return paintSelected;
        else if (wasSelected()) return paintWasSelected;
        else return paintNotSelected;
    }

    public int getSize() {
        return isSelected() ? sizeSelected : sizeNotSelected;
    }

    public int getSpacing() {
        return spacing;
    }

    public Drawable getIcon() {
        return icon;
    }

    public Rect getRectForDrawing() {
        assert (areaDeselected != null && areaSelected != null) : "function called before area rects initialized";
        if (isSelected() || wasSelected()) return areaSelected;
        //else return areaDeselected;
        else return new Rect(0,0,0,0);
    }

    public Rect getRectForTouching() {
        assert areaTouch != null : "function called before touch rect initialized";
        return areaTouch;
    }

    public DragMenuItem getNorth() {
        return north;
    }

    public DragMenuItem getEast() {
        return east;
    }

    public DragMenuItem getSouth() {
        return south;
    }

    public DragMenuItem getWest() {
        return west;
    }

    public void setNorth(DragMenuItem item) {
        item.parent = this;
        north = item;
    }

    public void setEast(DragMenuItem item) {
        item.parent = this;
        east = item;
    }

    public void setSouth(DragMenuItem item) {
        item.parent = this;
        south = item;
    }

    public void setWest(DragMenuItem item) {
        item.parent = this;
        west = item;
    }

}