package com.malcolmcrum.draglauncher;

/**
 * Menu object
 * Created by Malcolm Crum on 3/22/2015.
 */
public class DragMenuItem {
    // Each DragMenuItem may have two children, here referred to as left or right - these terms
    // are local to the gesture, so may potentially mean up or down, for example.
    private DragMenuItem left;
    private DragMenuItem right;
    private DragMenuItem parent; // TODO: is this necessary?

    public String packageName;

    public DragMenuItem(String name) {
        this.packageName = name;
    }

    public DragMenuItem setLeft(String name) {
        DragMenuItem newItem = new DragMenuItem(name);
        newItem.parent = this;
        left = newItem;
        return newItem;
    }

    public DragMenuItem setRight(String name) {
        DragMenuItem newItem = new DragMenuItem(name);
        newItem.parent = this;
        right = newItem;
        return newItem;
    }


}