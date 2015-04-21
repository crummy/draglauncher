package com.malcolmcrum.draglauncher;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

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

    public String label = "?!";

    private Rect areaDeselected;
    private Rect areaSelected;
    private Rect areaTouch; // A larger rect is used for touch detection.

    /* Three states:
     * notSelected, if a MenuItem has not been touched at all in the current touch
     * wasSelected, if a MenuItem was touched and is a parent of the currently touched MenuItem
     * isSelected, if a MenuItem is currently selected (i.e. finger is over it)
     */
    private enum SelectedState {notSelected, wasSelected, isSelected};
    private SelectedState selected = SelectedState.notSelected;
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

    /**
     * Generates layout information for the MenuItem. Must be called before draw calls.
     *
     * @param  x    x coordinate of the center of the MenuItem
     * @param  y    y coordinate of the center of the MenuItem
     */
    public void layout(int x, int y) {
        areaDeselected = new Rect(x - sizeDeselected/2, y - sizeDeselected/2, x + sizeDeselected/2, y + sizeDeselected/2);
        areaSelected = new Rect(x - sizeSelected/2, y - sizeSelected/2, x + sizeSelected/2, y + sizeSelected/2);
        areaTouch = new Rect(x - spacing/2, y - spacing/2, x + spacing/2, y + spacing/2);

        if (north != null) north.layout(x, y - spacing);
        if (east != null) east.layout(x + spacing, y);
        if (south != null) south.layout(x, y + spacing);
        if (west != null) west.layout(x - spacing, y);
    }

    public boolean isSelected() {
        return selected == SelectedState.isSelected;
    }

    public boolean isVisible() {
        if (parent != null && parent.isSelected()) return true;
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

    public Rect getRectForDrawing() {
        assert (areaDeselected != null && areaSelected != null) : "function called before area rects initialized";
        if (isSelected()) return areaSelected;
        else return areaDeselected;
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